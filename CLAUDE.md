# Claude - Bootloader de Sessão | Schedule

> **INSTRUÇÃO FIXA**: Leia este arquivo na inicialização. Sua prioridade é eficiência de tokens e precisão técnica.

---

## Estratégia de Sessão e Eficiência (Token Economy)

- **Contexto Local**: Para detalhes de implementação de um pacote, SEMPRE leia o `CLAUDE.md` dentro do diretório correspondente.
- **Respostas Diretas**: Evite introduções longas ou "conversas". Vá direto ao código ou solução.
- **Subagentes**: Use para exploração massiva ou análise paralela, mantendo a janela principal limpa.
- **Incremental**: Não re-explique conceitos globais se já foram discutidos. Use referências breves.

## Protocolo de Trabalho

1. **Ambiente**: Confirme se está na branch correta antes de iniciar alterações (o usuário deve entregar a branch pronta — não execute operações git).
2. **Design**: Priorize simplicidade. Use o modo de planejamento para decisões de arquitetura.
3. **Verificação**: Nunca finalize sem provar que funciona (build, logs).
4. **Auto-Aperfeiçoamento**: Errou? Salve uma memória do tipo `feedback` via sistema de memória do Claude.
5. **NUNCA faça commit**: Não faça nenhum commit sem pedido explícito do usuário.
6. **Dúvidas sobre nova feature?**: Pergunte ao usuário qual feature/classe pode servir de exemplo.
7. **Elegância (Equilibrada)**: Mudanças não triviais → pergunte "existe solução mais elegante?". Correções óbvias → pule.
8. **Bugs**: Recebeu relatório → conserte direto. Analise logs e stack trace. Mínimo de context switching.

## Convenção de Datas (CRÍTICO)

Use `OffsetDateTime` em **todas** as entidades e campos de timestamp.

- **Entities**: herdam de `BaseEntity` → `createdAt` e `updatedAt` gerenciados via `@PrePersist` / `@PreUpdate` automaticamente.
- **Banco**: tipo `TIMESTAMPTZ` no PostgreSQL — nunca use `TIMESTAMP` sem timezone.
- **Nunca defina** `createdAt`/`updatedAt` manualmente — `BaseEntity` cuida disso.

## Regras de Código (Mandatórias)

- **Completo**: Sempre entregue o arquivo/classe INTEGRAL (imports, anotações, métodos).
- **Stack**: Java 17 | Spring Boot 4.0.3 | PostgreSQL | Liquibase | Spring Security | JWT (jjwt 0.12.6) | Lombok.
- **Lombok**: `@RequiredArgsConstructor`, `@Slf4j`, `@Getter`, `@Setter`, `@NoArgsConstructor` (conforme o caso).
- **DTOs**: Records Java para requests e responses — separados em `dto/<domínio>/request/` e `dto/<domínio>/response/`.
- **Padrões de Nomenclatura**: Pacotes em `snake_case`. Classes em `PascalCase`.
- **Roles**: `ADMIN` e `USER`. Endpoints admin usam `@PreAuthorize("hasRole('ADMIN')")` no controller.
- **Erros**: Lance sempre `ApiException(HttpStatus.XYZ, "mensagem")` — `GlobalExceptionHandler` trata automaticamente.
- **Migrações**: Toda alteração de schema via Liquibase XML em `src/main/resources/db/changelog/schema/`. Nunca use `ddl-auto: create` ou similar.

## Arquitetura

```
src/main/java/dev/rokku/schedule/
└── domain/
    ├── controller/   # Endpoints REST (ver CLAUDE.md local)
    ├── dto/          # Records de request/response por domínio (ver CLAUDE.md local)
    ├── exception/    # ApiException + GlobalExceptionHandler (ver CLAUDE.md local)
    ├── model/        # Entidades JPA + enums (ver CLAUDE.md local)
    ├── repository/   # Spring Data JPA repositories (ver CLAUDE.md local)
    ├── security/     # JWT, SecurityConfig, filtros (ver CLAUDE.md local)
    └── service/      # Lógica de negócio + upload de imagens (ver CLAUDE.md local)
src/main/resources/
├── application.yml
└── db/changelog/     # Liquibase XML (master + schema/)
```

## Localização de Conhecimento (CLAUDE.md Locais)

- `controller`: Endpoints REST, roles e contratos de entrada/saída.
- `dto`: Records de request/response por domínio.
- `exception`: Hierarquia de erros e tratamento global.
- `model`: Entidades JPA, BaseEntity e enums.
- `repository`: Repositórios Spring Data JPA.
- `security`: Filtro JWT, SecurityConfig e utilitários de autenticação.
- `service`: Lógica de negócio, padrões internos e FileStorageService.

## Banco de Dados

- **PostgreSQL** via Docker em `localhost:8091` (mapeado de `5432`).
- **Database/User/Password**: todos `schedule`.
- **Migrations**: Liquibase XML — master em `db.changelog-master-public.xml`, scripts em `schema/`.
- **Schema**: `public`, contexto Liquibase: `local`.

```bash
docker compose up -d db        # Subir apenas o banco
docker compose up -d           # Subir banco + mailhog
```

## Variáveis de Ambiente

| Variável          | Default                             | Descrição                                      |
|-------------------|-------------------------------------|------------------------------------------------|
| `APP_BASE_URL`    | `http://192.168.15.7:8090/`         | URL base da app (monta URLs de imagens)        |
| `JWT_SCRET_KEY`   | `CHAVE-JWT-COM-MAIS-DE-256-BIT-...` | Chave de assinatura JWT (mínimo 256 bits)      |
| `FRONT_URL`       | `http://localhost:3000`             | URL do frontend liberada no CORS               |
| `app.upload.dir`  | `uploads`                           | Diretório raiz de upload de imagens            |

## Comandos

```bash
./gradlew bootRun           # Subir a aplicação (porta 8090)
./gradlew test              # Rodar todos os testes
./gradlew build             # Build completo (compila + testa)
./gradlew build -x test     # Build sem testes
```

---
**Pacote Base**: `dev.rokku.schedule`

---

## Finalizando uma Tarefa

- **Build**: Rode `./gradlew build` antes de considerar a tarefa concluída.
- **Documentação**: Atualize os CLAUDE.md se a mudança introduzir novos padrões ou gotchas.
- **Sem commit**: Nunca commite sem pedido explícito do usuário.

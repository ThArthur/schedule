# Claude - Contexto Local | service

> Lógica de negócio da aplicação Schedule. Leia este arquivo antes de criar ou modificar services.

---

## Services

| Service               | Responsabilidade                                              |
|-----------------------|---------------------------------------------------------------|
| `AuthService`         | Registro, login, CRUD de usuários, geração de JWT            |
| `BuildingService`     | CRUD de prédios + upload/deleção de imagem                   |
| `RoomService`         | CRUD de salas + upload/deleção de imagem                     |
| `RoomPriceService`    | CRUD de preços por período para uma sala                     |
| `RoomBlockService`    | CRUD de bloqueios de período para uma sala                   |
| `ReservationService`  | CRUD de reservas (room + user + período + preço + status)    |
| `UsersService`        | Operações adicionais sobre usuários (se existir)             |
| `FileStorageService`  | Armazenamento e deleção de arquivos no sistema de arquivos   |

## Padrão Interno (CRÍTICO)

Todos os services CRUD seguem o mesmo padrão:
Para criar um novo service CRUD, siga este padrão:
./BuildingService.java

Siga este padrão ao criar novos services ou adicionar operações.

## FileStorageService

- Salva arquivos em `<upload.dir>/<subDir>/<uuid>.<ext>`.
- Retorna o **caminho relativo** (ex: `rooms/uuid.jpg`) para salvar no banco.
- `deleteFile(String filePath)` silencia `IOException` — não lança exceção.
- Subdiretórios usados: `rooms/`, `buildings/`.
- A URL pública é montada no service consumidor: `baseUrl + "/uploads/" + imageUrl`.

```java
String path = fileStorageService.storeFile(image, "rooms");  // retorna "rooms/uuid.jpg"
room.setImageUrl(path);
```

## Imagens — Ciclo de Vida

- **Create**: se `image != null && !image.isEmpty()`, salva e armazena o caminho na entidade.
- **Update**: se nova imagem enviada, **deleta a antiga** antes de salvar a nova.
- **Delete**: se entidade tem `imageUrl`, **deleta o arquivo** antes de remover do banco.

## Transações

- Operações de leitura: `@Transactional(readOnly = true)`.
- Operações de escrita: `@Transactional`.
- `FileStorageService` não é transacional — I/O de arquivo é fora do escopo do banco.

## AuthService — Detalhes

- `register()`: força `role = USER` — nunca deixe o usuário escolher role no cadastro público.
- `createUser()` (admin): aceita `role` explícita via `CreateUserRequest`.
- `validarEmailDisponivel()`: verifica com `existsByEmail` antes de salvar para evitar `DataIntegrityViolationException`.

## Gotcha

- `RoomService` e `BuildingService` injetam `@Value("${app.base-url}")` para montar a URL pública de imagens. Se mudar a env, as URLs mudam automaticamente.
- `ReservationService` não valida sobreposição de horários — se necessário, adicione query no `ReservationRepository` para checar conflitos.
- `FileStorageService` cria o diretório `uploads/` no startup — não crie manualmente.

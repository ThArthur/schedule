# Claude - Contexto Local | controller

> Endpoints REST da aplicação Schedule. Leia este arquivo antes de criar ou modificar controllers.

---

## Responsabilidade

Controllers são finos: delegam 100% da lógica para o `Service` correspondente. Sem regra de negócio aqui.

## Controllers e Endpoints

| Controller              | Base Path           | Role necessária |
|-------------------------|---------------------|-----------------|
| `AuthController`        | `/api/auth`         | Público          |
| `AdminUserController`   | `/api/user-admin`   | `ADMIN`          |
| `BuildingController`    | `/api/buildings`    | Autenticado      |
| `RoomController`        | `/api/rooms`        | Autenticado      |
| `RoomPriceController`   | `/api/room-prices`  | Autenticado      |
| `RoomBlockController`   | `/api/room-blocks`  | Autenticado      |
| `ReservationController` | `/api/reservations` | Autenticado      |

## Rotas Públicas (sem JWT)

Definidas em `UriShouldNotFilter.JWT_AUTH_FILTER_EXCLUDED_PATHS`:

```
POST /api/auth/login
POST /api/auth/register
GET  /api/buildings/{id}/image
GET  /api/rooms/{id}/image
GET  /uploads/**
```

## Autorização por Role

- `AdminUserController` usa `@PreAuthorize("hasRole('ADMIN')")` no nível da classe — todos os endpoints do controller exigem `ADMIN`.
- Demais controllers exigem apenas autenticação (qualquer role válida).
- Para restringir um endpoint específico: `@PreAuthorize("hasRole('ADMIN')")` no método.

## Padrão de HTTP Status

| Situação              | Status              | Como declarar                         |
|-----------------------|---------------------|---------------------------------------|
| Criação bem-sucedida  | `201 Created`       | `@ResponseStatus(HttpStatus.CREATED)` |
| Deleção bem-sucedida  | `204 No Content`    | `@ResponseStatus(HttpStatus.NO_CONTENT)` |
| Busca/atualização     | `200 OK`            | (padrão — não declarar)               |
| Erro de negócio       | variado             | Lance `ApiException` no service       |

## Upload de Imagens (Multipart)

Controllers de `Building` e `Room` recebem imagem via `@RequestPart`:

```java
@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public BuildingResponse create(
    @RequestPart("data") @Valid BuildingRequest request,
    @RequestPart(value = "image", required = false) MultipartFile image
)
```

- A imagem é **opcional** — passe `required = false`.
- O service (`FileStorageService`) cuida do armazenamento em `uploads/<subdir>/`.

## Convenção de Nomenclatura

- Métodos: `findAll`, `findById`, `create`, `update`, `delete` (CRUD padrão).
- Path variables: `{id}` tipado como `Long` via `@PathVariable Long id`.
- Request body: `@RequestBody @Valid` para validação automática.

## Gotcha

- `AdminUserController` tem um import não utilizado (`ScriptTargetOutputToFile`) — ignore ao ler, remova se refatorar.
- Endpoints de imagem (`/api/buildings/*/image`, `/api/rooms/*/image`) são públicos — útil para o frontend exibir imagens sem autenticação.

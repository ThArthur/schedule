# Claude - Contexto Local | dto

> Records de request e response da aplicação Schedule. Leia este arquivo antes de criar ou modificar DTOs.

---

## Estrutura

```
dto/
├── auth/
│   ├── request/   LoginRequest, RegisterRequest, CreateUserRequest, UpdateUserRequest
│   └── response/  AuthResponse, UserResponse
├── building/
│   ├── request/   BuildingRequest
│   └── response/  BuildingResponse
├── reservation/
│   ├── request/   ReservationRequest
│   └── response/  ReservationResponse
├── room/
│   ├── request/   RoomRequest
│   └── response/  RoomResponse
├── room_block/
│   ├── request/   RoomBlockRequest
│   └── response/  RoomBlockResponse
└── room_price/
    ├── request/   RoomPriceRequest
    └── response/  RoomPriceResponse
```

## Regras

- **Sempre Records Java** — nunca classes comuns para DTOs.
- **Separação obrigatória**: um pacote `request/` e um `response/` por domínio.
- **Validação**: use anotações Jakarta Validation (`@NotNull`, `@NotBlank`, `@Valid`, etc.) diretamente nos campos do record.
- **Nomenclatura**: `<Domínio>Request` e `<Domínio>Response`. Para variações (criar vs atualizar): `Create<Domínio>Request`, `Update<Domínio>Request`.

## Tipos de Data/Hora

- Campos de timestamp em requests/responses: `OffsetDateTime` (não `LocalDate` nem `LocalDateTime`).
- O banco usa `TIMESTAMPTZ` — mantenha coerência no DTO.

## AuthResponse

```java
record AuthResponse(String token, String type, Instant expiresAt)
```

- `type` é sempre `"Bearer"`.
- `expiresAt` é calculado por `JwtUtil.getExpiresAt()`.

## Gotcha

- `UserResponse` inclui `role` — nunca inclua `passwordHash` em nenhum response.
- `CreateUserRequest` aceita `role` explícita (uso admin), enquanto `RegisterRequest` força `role = USER` no service.

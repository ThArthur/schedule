# Claude - Contexto Local | model

> Entidades JPA e enums da aplicação Schedule. Leia este arquivo antes de criar ou modificar modelos.

---

## Entidades

| Entidade      | Tabela        | Descrição                                      |
|---------------|---------------|------------------------------------------------|
| `BaseEntity`  | (superclasse) | `createdAt` + `updatedAt` gerenciados via JPA  |
| `Building`    | `building`    | Prédio com nome, número e complemento          |
| `Room`        | `room`        | Sala vinculada a um `Building`, com imagem     |
| `Users`       | `users`       | Usuário da aplicação com role e senha hash     |
| `RoomPrice`   | `room_price`  | Preço por período de horas para uma sala       |
| `RoomBlock`   | `room_block`  | Bloqueio de período de uma sala                |
| `Reservation` | `reservation` | Reserva de sala por um usuário                 |

## BaseEntity (CRÍTICO)

```java
@MappedSuperclass
public abstract class BaseEntity {
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    // @PrePersist e @PreUpdate gerenciam automaticamente
}
```

- **Toda entidade nova deve estender `BaseEntity`**.
- Nunca defina `createdAt` ou `updatedAt` manualmente — o JPA cuida disso.

## Enums

```java
enum UserRole   { ADMIN, USER }
enum ReservationStatus { PENDING, CONFIRMED, CANCELLED }
```

- Ambos persistidos como `VARCHAR` via `@Enumerated(EnumType.STRING)`.
- Constraints de banco definidas em `007-add-checks` no Liquibase.

## Relacionamentos

```
Building (1) ──< Room (N)
Room     (1) ──< RoomPrice (N)
Room     (1) ──< RoomBlock (N)
Room     (N) >── Reservation (N) ──< Users
```

- Todos os `@ManyToOne` usam `FetchType.LAZY` — nunca altere para `EAGER`.
- `room_number` é único por `building_id` (constraint `uk_room_building_number`).

## Tipos de Data

- `OffsetDateTime` em todos os campos de data/hora — banco usa `TIMESTAMPTZ`.
- `BigDecimal` para valores monetários (`totalPrice`, `value` em `RoomPrice`) — precisão `NUMERIC(12,2)`.

## Imagens (Room e Building)

- Campo `imageUrl` em `Room` e `Building` armazena **caminho relativo** dentro de `uploads/`, ex: `rooms/uuid.jpg`.
- A URL completa é montada no service: `baseUrl + "/uploads/" + imageUrl`.
- Nunca armazene a URL completa no banco.

## Gotcha

- A entidade se chama `Users` (plural) para evitar conflito com a palavra reservada `user` do PostgreSQL.
- `RoomPrice` não tem `updatedAt` (não estende `BaseEntity`) — apenas `createdAt`.

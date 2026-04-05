# Claude - Contexto Local | repository

> Repositórios Spring Data JPA da aplicação Schedule. Leia este arquivo antes de criar ou modificar repositórios.

---

## Repositórios

| Interface               | Entidade      | Métodos customizados                       |
|-------------------------|---------------|--------------------------------------------|
| `BuildingRepository`    | `Building`    | Nenhum (apenas CRUD padrão)                |
| `RoomRepository`        | `Room`        | `findByBuildingId(Long buildingId)`        |
| `UsersRepository`       | `Users`       | `findByEmail`, `existsByEmail`             |
| `RoomPriceRepository`   | `RoomPrice`   | Nenhum (apenas CRUD padrão)                |
| `RoomBlockRepository`   | `RoomBlock`   | Nenhum (apenas CRUD padrão)                |
| `ReservationRepository` | `Reservation` | Nenhum (apenas CRUD padrão)                |

## Regras

- Todos estendem `JpaRepository<Entidade, Long>`.
- **Nunca use `findAll()` sem paginação** em repositórios que podem crescer muito (ex: `ReservationRepository`). Adicione `Pageable` se o volume justificar.
- Queries derivadas do nome do método são preferidas para casos simples. Use `@Query` apenas quando a query derivada não for legível.
- Transações são gerenciadas no **service** — não adicione `@Transactional` nos repositórios.

## Convenção de Novos Métodos

Siga a nomenclatura Spring Data:

```java
List<Room> findByBuildingId(Long buildingId);
boolean existsByEmail(String email);
Optional<Users> findByEmail(String email);
```

## Gotcha

- `RoomRepository.findByBuildingId` usa o campo `building.id` da entidade `Room` — o Hibernate gera o JOIN automaticamente.
- `UsersRepository` tem `existsByEmail` para checar duplicata antes de salvar, evitando capturar `DataIntegrityViolationException`.

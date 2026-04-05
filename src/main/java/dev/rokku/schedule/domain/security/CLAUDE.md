# Claude - Contexto Local | security

> Autenticação, autorização e filtros JWT da aplicação Schedule. Leia este arquivo antes de modificar qualquer coisa de segurança.

---

## Classes Principais

| Classe                    | Responsabilidade                                                   |
|---------------------------|--------------------------------------------------------------------|
| `SecurityConfig`          | Configura `SecurityFilterChain`, CORS e providers de autenticação  |
| `UriShouldNotFilter`      | Lista de paths que ignoram o filtro JWT                            |
| `WebConfig`               | Configuração de recursos estáticos (servir `uploads/`)             |
| `JwtAuthFilter`           | Filtro que extrai e valida o JWT a cada requisição                 |
| `JwtUtil`                 | Gera e parseia tokens JWT (jjwt 0.12.6)                           |
| `DefaultJwtSecret`        | Converte a chave secreta string → `SecretKey` HMAC-SHA             |
| `UserDetailsImpl`         | Implementação de `UserDetails` com `id`, `name`, `email`, `role`  |
| `UserDetailsServiceImpl`  | Carrega `UserDetails` pelo email via `UsersRepository`             |
| `HttpFilterHelper`        | Extrai o Bearer token do header `Authorization`                    |

## Fluxo JWT

```
Request → JwtAuthFilter
  → extrai "Bearer <token>" do header Authorization
  → JwtUtil.getIdUsuario(token) → Long userId
  → UserDetailsServiceImpl.loadUserByUsername(userId)
  → seta Authentication no SecurityContextHolder
  → continua a chain
```

## Paths Públicos (sem JWT)

Definidos em `UriShouldNotFilter.JWT_AUTH_FILTER_EXCLUDED_PATHS`:

```java
"/api/auth/login"
"/api/auth/register"
"/api/buildings/*/image"
"/api/rooms/*/image"
"/uploads/**"
```

Para adicionar um novo path público: adicione à lista em `UriShouldNotFilter` — o `JwtAuthFilter` e o `SecurityConfig` a consomem automaticamente.

## CORS

Configurado em `SecurityConfig.corsConfigurationSource()`:

- **Origins permitidas**: `http://localhost:3000` + `${front-url}` (env `FRONT_URL`).
- **Métodos**: GET, POST, PUT, DELETE, PATCH, OPTIONS.
- **Credentials**: `true`.
- Para adicionar uma nova origem, altere a env `FRONT_URL` — não hardcode na config.

## JWT — Payload do Token

```json
{
  "sub": "<userId>",
  "user": "<email>",
  "name": "<nome>",
  "authorities": [...],
  "iat": ...,
  "exp": ...
}
```

- `sub` é o ID do usuário (String) — o filtro usa isso para carregar o `UserDetails`.
- Expiração configurada em `jwt.expirationMs` (default: 4 horas = 14.400.000 ms).

## Autorização por Role

Uso via `@PreAuthorize` (habilitado por `@EnableMethodSecurity`):

```java
@PreAuthorize("hasRole('ADMIN')")
```

- Spring adiciona o prefixo `ROLE_` internamente — não use `ROLE_ADMIN` no código.
- `UserDetailsImpl` retorna `GrantedAuthority` via `getAuthorities()` baseado em `UserRole`.

## Gotcha

- O subject do JWT é o **ID do usuário** (não o email) — `JwtUtil.getIdUsuario()` faz o parse para `Long`.
- `JwtAuthFilter` não lança exceção se o token for inválido — simplesmente não seta autenticação e a request cai em `401` pelo Spring Security.
- `DefaultJwtSecret` converte a chave em `SecretKey` usando `Keys.hmacShaKeyFor` — a chave deve ter no mínimo 256 bits (32 chars).

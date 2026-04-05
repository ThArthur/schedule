# Claude - Contexto Local | exception

> Tratamento de erros da aplicação Schedule. Leia este arquivo antes de criar ou modificar exceções.

---

## Classes

| Classe                  | Responsabilidade                                              |
|-------------------------|---------------------------------------------------------------|
| `ApiException`          | Exceção de negócio com `HttpStatus` e mensagem customizada    |
| `GlobalExceptionHandler`| `@RestControllerAdvice` — captura e formata todas as exceções |

## Como Lançar Erros

Sempre use `ApiException` dentro dos services:

```java
throw new ApiException(HttpStatus.NOT_FOUND, "Room not found");
throw new ApiException(HttpStatus.CONFLICT, "E-mail ja cadastrado.");
throw new ApiException(HttpStatus.BAD_REQUEST, "Mensagem descritiva.");
```

Nunca lance `RuntimeException` ou `IllegalArgumentException` diretamente — o `GlobalExceptionHandler` não os trata com status semântico.

## Formato da Resposta de Erro

```json
{
  "timestamp": "2026-04-05T10:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Room not found"
}
```

Para erros de validação (`MethodArgumentNotValidException`), o campo `details` é adicionado:

```json
{
  "timestamp": "...",
  "status": 400,
  "error": "Bad Request",
  "message": "Dados invalidos.",
  "details": {
    "email": "must not be blank",
    "name": "must not be null"
  }
}
```

## Mapeamentos do GlobalExceptionHandler

| Exceção                          | Status HTTP            |
|----------------------------------|------------------------|
| `ApiException`                   | Definido no construtor |
| `MethodArgumentNotValidException`| `400 Bad Request`      |
| `AuthenticationException`        | `401 Unauthorized`     |
| `AuthorizationDeniedException`   | `403 Forbidden`        |
| `AccessDeniedException`          | `403 Forbidden`        |
| `Exception` (genérico)           | `500 Internal Server Error` |

## Gotcha

- Mensagens de erro da `AuthenticationException` são sempre substituídas por `"Credenciais invalidas."` — não vaze detalhes de autenticação.
- O handler genérico `Exception` retorna `"Erro interno."` sem expor stack trace.

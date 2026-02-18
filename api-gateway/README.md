# api-gateway

API Gateway для микросервисной системы регистрации доменов.

## Описание

api-gateway — это Spring Cloud Gateway, который служит единой точкой входа для всех клиентских запросов. Он выполняет маршрутизацию запросов к соответствующим микросервисам, обрабатывает CORS, обеспечивает rate limiting через Redis и реализует circuit breaker для отказоустойчивости.

## Основные функции

- Маршрутизация запросов по путям к соответствующим микросервисам
- Обработка CORS запросов (только в Docker профиле)
- Rate limiting с помощью Redis
- Circuit breaker (Resilience4j) для защиты от каскадных отказов
- Удаление префикса пути (`StripPrefix${DB_USER:***REMOVED***}1`) при проксировании

## Технологический стек

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Cloud Gateway** (на базе WebFlux, неблокирующий)
- **Spring Cloud 2023.0.0**
- **Spring Data Redis Reactive**
- **Resilience4j** — circuit breaker
- **Lombok**

## Структура проекта

```
api-gateway/
├── src/
│   ├── main/
│   │   ├── java/ru/itmo/gateway/
│   │   │   └── GatewayApplication.java    # Точка входа приложения
│   │   └── resources/
│   │       ├── application.yml            # Конфигурация по умолчанию (localhost)
│   │       └── application-docker.yml     # Конфигурация для Docker
├── build.gradle.kts                       # Gradle конфигурация
└── Dockerfile                             # Мульти-стейдж Docker сборка
```

## Конфигурация

| Параметр | Описание | По умолчанию |
|----------|----------|--------------|
| `server.port` | Порт сервиса | 8080 |
| `API_GATEWAY_STRIP_PREFIX` | Количество удаляемых сегментов пути | 1 |
| `CORS_ALLOWED_ORIGINS` | Разрешённые origins для CORS | * |
| `spring.data.redis.host` | Хост Redis | localhost / redis |
| `spring.data.redis.port` | Порт Redis | 6379 |

## Маршрутизация

| Path Pattern       | Target Service   | Target Port | Description |
|--------------------|------------------|-------------|-------------|
| `/api/auth/**`     | auth-service     | 8081        | Аутентификация, пользователи, 2FA |
| `/api/users/**`    | auth-service     | 8081        | Управление пользователями |
| `/api/domains/**`  | domain-service   | 8082        | Регистрация доменов, DNS записи |
| `/api/payments/**` | payment-service  | 8083        | Обработка платежей |
| `/api/orders/**`   | order-service    | 8084        | Корзина, заказы, продление |
| `/api/notifications/**` | notification-service | 8085 | Email уведомления |
| `/api/admin/**`    | admin-service    | 8086        | Административные отчёты |
| `/api/audit/**`    | audit-service    | 8087        | Логирование аудита |

## Пример запроса

**Запрос через Gateway:**
```http
GET /api/domains/my-domain.com
Host: api-gateway:8080
Authorization: Bearer jwt-token
```

**Проксируется в domain-service как:**
```http
GET /domains/my-domain.com
Host: domain-service:8082
```

## Actuator Endpoints

- `/actuator/health` — состояние сервиса
- `/actuator/info` — информация о сервисе
- `/actuator/metrics` — метрики приложения

## Диаграммы

### Sequence Diagram — Поток запроса через Gateway

```mermaid
sequenceDiagram
    participant C as Клиент (Frontend)
    participant GW as API Gateway
    participant Redis as Redis
    participant S as Сервис назначения

    C->>GW: GET /api/domains/my-domain.com
    activate GW

    GW->>GW: Найти маршрут
    GW->>GW: Убрать префикс пути

    GW->>Redis: Проверить ограничение запросов
    Redis-->>GW: OK / превышен лимит

    alt Превышен лимит
        GW-->>C: 429 Too Many Requests
    else Лимит не превышен
        GW->>S: GET /domains/my-domain.com
        activate S
        S-->>GW: 200 OK + данные
        deactivate S
        GW-->>C: 200 OK
    end

    deactivate GW
```

### BPMN Diagram — Процесс обработки запроса

```mermaid
flowchart TD
    Start([Начало]) --> Receive[Получить запрос<br/>от клиента]
    Receive --> MatchRoute[Найти маршрут<br/>по пути]

    MatchRoute --> HasRoute{Маршрут<br/>найден?}
    HasRoute -- Нет --> Return404([404 Not Found])

    HasRoute -- Да --> CheckRate[Проверить ограничение<br/>в Redis]
    CheckRate --> IsRateLimited{Лимит<br/>превышен?}

    IsRateLimited -- Да --> Return429([429 Too Many Requests])
    IsRateAllowed -- Нет --> ApplyFilters[Применить фильтры<br/>StripPrefix и др.]

    ApplyFilters --> CBCheck[Проверить Circuit Breaker]

    CBCheck --> IsCBOpen{CB<br/>открыт?}
    IsCBOpen -- Да --> Return503([503 Service Unavailable<br/>Fallback])

    IsCBOpen -- Нет --> ForwardRequest[Переслать в<br/>целевой сервис]

    ForwardRequest --> CheckResponse{Ответ<br/>получен?}

    CheckResponse -- Ошибка --> CBOnFailure[Отметить FAILURE]
    CheckResponse -- Успех --> CBOnSuccess[Отметить SUCCESS]

    CBOnFailure --> ReturnError[Ошибка проксирования]
    CBOnSuccess --> ProcessResponse[Обработать ответ]

    ProcessResponse --> Return200([200 OK<br/>ответ клиенту])

    Start -.-> Return404
    Return404 -.-> End([Конец])
    Return429 -.-> End
    Return503 -.-> End
    ReturnError -.-> End
    Return200 -.-> End

    style Start fill:#e1f5e1
    style End fill:#fce1e1
    style Return200 fill:#e1f5e1
    style Return404 fill:#ffe1e1
    style Return429 fill:#ffe1e1
    style Return503 fill:#ffe1e1
```

### BPMN Diagram — Архитектура маршрутизации

```mermaid
flowchart LR
    subgraph Frontend["Frontend :3000"]
        Client[React App]
    end

    subgraph Gateway["API Gateway :8080"]
        Router[Поиск маршрута]
        Stripper[Убрать префикс]
        RLim[Ограничение запросов]
        CB[Circuit Breaker]
    end

    subgraph Backend["Backend Services"]
        AuthS["auth-service :8081"]
        DomainS["domain-service :8082"]
        PaymentS["payment-service :8083"]
        OrderS["order-service :8084"]
        NotifS["notification-service :8085"]
        AdminS["admin-service :8086"]
        AuditS["audit-service :8087"]
    end

    subgraph Infra["Infrastructure"]
        Redis["Redis :6379"]
    end

    Client --> Router

    Router -->|/api/auth/**| Stripper
    Router -->|/api/domains/**| Stripper
    Router -->|/api/payments/**| Stripper
    Router -->|/api/orders/**| Stripper
    Router -->|/api/notifications/**| Stripper
    Router -->|/api/admin/**| Stripper
    Router -->|/api/audit/**| Stripper

    Stripper --> RLim
    RLim -.-> Redis
    Redis -.-> RLim

    RLim --> CB

    CB -->|/auth| AuthS
    CB -->|/domains| DomainS
    CB -->|/payments| PaymentS
    CB -->|/orders| OrderS
    CB -->|/notifications| NotifS
    CB -->|/admin| AdminS
    CB -->|/audit| AuditS

    CB -.-> Client

    style Gateway fill:#e3f2fd
    style Backend fill:#f3e5f5
    style Infra fill:#fff3e0
```

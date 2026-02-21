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

| Параметр                   | Описание                            | По умолчанию      |
|----------------------------|-------------------------------------|-------------------|
| `server.port`              | Порт сервиса                        | 8080              |
| `API_GATEWAY_STRIP_PREFIX` | Количество удаляемых сегментов пути | 1                 |
| `CORS_ALLOWED_ORIGINS`     | Разрешённые origins для CORS        | *                 |
| `spring.data.redis.host`   | Хост Redis                          | localhost / redis |
| `spring.data.redis.port`   | Порт Redis                          | 6379              |

## Маршрутизация

| Path Pattern            | Target Service       | Target Port | Description                       |
|-------------------------|----------------------|-------------|-----------------------------------|
| `/api/auth/**`          | auth-service         | 8081        | Аутентификация, пользователи, 2FA |
| `/api/users/**`         | auth-service         | 8081        | Управление пользователями         |
| `/api/domains/**`       | domain-service       | 8082        | Регистрация доменов, DNS записи   |
| `/api/payments/**`      | payment-service      | 8083        | Обработка платежей                |
| `/api/orders/**`        | order-service        | 8084        | Корзина, заказы, продление        |
| `/api/notifications/**` | notification-service | 8085        | Email уведомления                 |
| `/api/admin/**`         | admin-service        | 8086        | Административные отчёты           |
| `/api/audit/**`         | audit-service        | 8087        | Логирование аудита                |

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
flowchart TB
    subgraph ClientLane["         Клиент"]
        Start([Начало])
        Return404([404 Not Found])
        Return429([429 Too Many Requests])
        Return503([503 Service Unavailable<br/>Fallback])
        Return200([200 OK<br/>ответ клиенту])
        End([Конец])
    end

    subgraph GatewayLane["         API Gateway"]
        Receive[Получить запрос<br/>от клиента]
        MatchRoute[Найти маршрут<br/>по пути]
        HasRoute{Маршрут<br/>найден?}
        CheckRate[Проверить ограничение<br/>в Redis]
        IsRateLimited{Лимит<br/>превышен?}
        ApplyFilters[Применить фильтры<br/>StripPrefix и др.]
        CBCheck[Проверить Circuit Breaker]
        IsCBOpen{CB<br/>открыт?}
        ForwardRequest[Переслать в<br/>целевой сервис]
        CheckResponse{Ответ<br/>получен?}
        CBOnFailure[Отметить FAILURE]
        CBOnSuccess[Отметить SUCCESS]
        ProcessResponse[Обработать ответ]
    end

    subgraph RedisLane["         Redis"]
        RateCheck[Проверить<br/>ограничение запросов]
    end

    subgraph ServiceLane["         Сервис назначения"]
        TargetService[Обработать<br/>запрос]
    end

    Start --> Receive
    Receive --> MatchRoute

    MatchRoute --> HasRoute
    HasRoute -- Нет --> Return404

    HasRoute -- Да --> CheckRate
    CheckRate --> RateCheck
    RateCheck -.-> CheckRate

    CheckRate --> IsRateLimited

    IsRateLimited -- Да --> Return429
    IsRateLimited -- Нет --> ApplyFilters

    ApplyFilters --> CBCheck

    CBCheck --> IsCBOpen
    IsCBOpen -- Да --> Return503

    IsCBOpen -- Нет --> ForwardRequest

    ForwardRequest --> TargetService
    TargetService --> CheckResponse

    CheckResponse -- Ошибка --> CBOnFailure
    CheckResponse -- Успех --> CBOnSuccess

    CBOnFailure --> ReturnError[Ошибка проксирования]
    CBOnSuccess --> ProcessResponse

    ProcessResponse --> Return200

    Start -.-> Return404
    Return404 -.-> End
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
    subgraph FrontendLane["         Frontend :3000"]
        Client[React App]
    end

    subgraph GatewayLane["         API Gateway"]
        Router[Поиск маршрута]
        Stripper[Убрать префикс]
        RLim[Ограничение запросов]
        CB[Circuit Breaker]
    end

    subgraph BackendLane["         Backend Services"]
        AuthS["auth-service"]
        DomainS["domain-service"]
        PaymentS["payment-service"]
        OrderS["order-service"]
        NotifS["notification-service"]
        AdminS["admin-service"]
        AuditS["audit-service"]
    end

    subgraph InfraLane["         Infrastructure"]
        Redis["Redis"]
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

    style GatewayLane fill:#e3f2fd
    style BackendLane fill:#f3e5f5
    style InfraLane fill:#fff3e0
```

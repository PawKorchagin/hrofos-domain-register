# admin-service

Сервис для формирования административных отчётов в микросервисной системе регистрации доменов.

## Описание

admin-service — это Spring Boot микроервис, который агрегирует данные из других сервисов (auth-service, domain-service) для генерации административных отчётов в формате Markdown. Сервис не имеет собственной базы данных и работает как агрегатор статистики.

## Основные функции

- Генерация отчётов с общей статистикой системы
- Получение количества зарегистрированных пользователей из auth-service
- Получение статистики доменов из domain-service
- Логирование действий в audit-service
- Доступ только для пользователей с ролью ADMIN

## Технологический стек

- **Java 17**
- **Spring Boot 3.2.0**
- **OpenAPI Generator** — генерация API интерфейсов из YAML спецификации
- **Spring Security** — защита JWT-аутентификацией
- **Lombok** — сокращение бойлерплейта

## Структура проекта

```
admin-service/
├── config/                    # Конфигурация бинов Spring
│   ├── AuditConfig.java       # Конфигурация AuditClient
│   ├── JwtConfig.java         # Конфигурация JWT
│   ├── OpenApiConfig.java     # Настройка Swagger/OpenAPI
│   ├── RestTemplateConfig.java # Настройка HTTP клиента
│   └── SecurityConfig.java    # Spring Security + JWT фильтр
├── controller/                # REST контроллеры
│   ├── HealthApiController.java    # Проверка здоровья
│   └── ReportApiController.java    # Скачивание отчёта
├── security/                  # Слой безопасности
│   └── JwtAuthenticationFilter.java
├── service/                   # Бизнес-логика
│   ├── ReportService.java
│   └── impl/ReportServiceImpl.java
├── client/                    # Клиенты для других сервисов
│   ├── AuthClient.java           # Клиент auth-service
│   └── DomainStatsClient.java    # Клиент domain-service
└── exception/                 # Обработка ошибок
    └── GlobalExceptionHandler.java
```

## Конфигурация

| Параметр | Описание | По умолчанию |
|----------|----------|--------------|
| `server.port` | Порт сервиса | 8086 |
| `JWT_SECRET` | Ключ для подписи JWT токенов | - |
| `AUTH_SERVICE_URL` | Базовый URL auth-service | http://localhost:8081 |
| `DOMAIN_SERVICE_URL` | Базовый URL domain-service | http://localhost:8082 |
| `AUDIT_SERVICE_URL` | Базовый URL audit-service | http://localhost:8087 |

## API Endpoints

| Метод | Эндпоинт | Описание                     | Требуется роль |
|-------|----------|------------------------------|----------------|
| GET | `/admin/health` | Проверка здоровья сервиса    | Нет |
| GET | `/admin/report` | Скачивание отчёта (Markdown) | ADMIN |

### Получение отчёта

**Запрос:**
```http
GET /admin/report
Authorization: Bearer <jwt-token>
```

**Ответ:**
```http
Content-Type: text/markdown
Content-Disposition: attachment; filename${DB_USER:***REMOVED***}"report.md"

# Отчёт администратора

**Дата формирования:** 15.02.2026 12:30:45

---

## Статистика

| Показатель | Значение |
|---|---|
| Количество зарегистрированных пользователей | 150 |
| Количество активных пользователей | 85 |
| Количество зарегистрированных доменов | 320 |

---

*Отчёт сформирован автоматически сервисом admin-service.*
```

## Диаграммы

### Sequence Diagram — Формирование отчёта

```mermaid
sequenceDiagram
    participant A as Администратор
    participant GW as API Gateway
    participant Admin as admin-service
    participant Security as JwtAuthenticationFilter
    participant Auth as auth-service
    participant Domain as domain-service
    participant Audit as audit-service

    A->>GW: GET /api/admin/report<br/>Authorization: Bearer JWT
    GW->>Admin: GET /admin/report

    Admin->>Security: Проверить JWT
    Security->>Security: Проверить подпись и роль ADMIN
    Security-->>Admin: Токен валидный, userId/role

    Admin->>Auth: Получить кол-во пользователей<br/>Bearer JWT
    Auth-->>Admin: 150 пользователей

    Admin->>Domain: Получить статистику доменов<br/>Bearer JWT
    Domain-->>Admin: {активных: 85,<br/>доменов: 320}

    Admin->>Admin: Сформировать отчёт в Markdown

    Admin->>Audit: Лог: "Отчёт сформирован"

    Admin-->>GW: 200 OK<br/>Content-Type: text/markdown
    GW-->>A: Файл отчёта
```

### BPMN Diagram — Процесс генерации отчёта

```mermaid
flowchart TD
    Start([Начало]) --> CheckAuth[Проверить JWT]

    CheckAuth --> HasToken{Есть токен?}
    HasToken -- Нет --> Return401([401 Unauthorized])
    HasToken -- Да --> Validate[Проверить подпись JWT]

    Validate --> IsValid{Валидный?}
    IsValid -- Нет --> Return401([401 Unauthorized])
    IsValid -- Да --> CheckRole{Роль: ADMIN?}

    CheckRole -- Нет --> Return403([403 Forbidden])
    CheckRole -- Да --> GetUsers[Запрос в auth-service<br/>/auth/stats/users-count]

    GetUsers --> CheckUsers{Ответ успешен?}
    CheckUsers -- Нет --> ReturnError([500 Internal Server Error])
    CheckUsers -- Да --> GetDomains[Запрос в domain-service<br/>/domains/stats]

    GetDomains --> CheckStats{Ответ успешен?}
    CheckStats -- Нет --> ReturnError([500 Internal Server Error])
    CheckStats -- Да --> Generate[Сформировать отчёт в Markdown]

    Generate --> Log[Записать в аудит]

    Log --> Return200([200 OK<br/>report.md])

    Start -.-> Return401
    Return401 -.-> End([Конец])
    Return403 -.-> End
    ReturnError -.-> End
    Return200 -.-> End

    style Start fill:#e1f5e1
    style End fill:#fce1e1
    style Return200 fill:#e1f5e1
    style Return401 fill:#ffe1e1
    style Return403 fill:#ffe1e1
    style ReturnError fill:#ffe1e1
```

### BPMN Diagram — Поток запроса через API Gateway

```mermaid
flowchart LR
    subgraph Frontend["Frontend"]
        Client[React App]
    end

    subgraph Gateway["API Gateway :8080"]
        Router[Маршрутизация<br/>StripPrefix 1]
    end

    subgraph Admin["admin-service :8086"]
        Filter[JwtAuthenticationFilter]
        Controller[ReportApiController]
        Service[ReportServiceImpl]
        AuthC[AuthClient]
        DomainC[DomainStatsClient]
    end

    subgraph Services["Внешние сервисы"]
        AuthS["auth-service :8081"]
        DomainS["domain-service :8082"]
        AuditS["audit-service :8087"]
    end

    Client -->|GET /api/admin/report| Router
    Router -->|GET /admin/report| Filter
    Filter --> Controller
    Controller --> Service
    Service --> AuthC
    AuthC --> AuthS
    Service --> DomainC
    DomainC --> DomainS
    Service -->|"AuditClient"| AuditS
    Service -.-> Controller
    Controller -.-> Router
    Router -.-> Client

    style Gateway fill:#e3f2fd
    style Admin fill:#f3e5f5
    style Services fill:#fff3e0
```

## Зависимости между сервисами

```mermaid
graph LR
    Admin[admin-service]
    Auth[auth-service]
    Domain[domain-service]
    Audit[audit-service]

    Admin -->|HTTP| Auth
    Admin -->|HTTP| Domain
    Admin -->|HTTP| Audit

    style Admin fill:#9c27b0,color:#fff
    style Auth fill:#2196f3,color:#fff
    style Domain fill:#4caf50,color:#fff
    style Audit fill:#ff9800,color:#fff
```

## Логирование

При формировании отчёта сервис отправляет событие аудита в audit-service:
```
"Admin report generated"
```

## Security

- JWT токены проверяются через `JwtUtil` из модуля `common`
- Эндпоинт `/admin/report` доступен только пользователям с ролью `ADMIN`
- Эндпоинт `/admin/health` публичный токен аутентификации

## Мониторинг

Актuator эндпоинты:
- `/actuator/health` — состояние сервиса
- `/actuator/info` — информация о сервисе
- `/actuator/metrics` — метрики приложения

## Swagger UI

Документация API доступна по адресу: `http://localhost:8086/swagger-ui.html`

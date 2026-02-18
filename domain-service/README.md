# domain-service

Сервис для управления доменами и DNS записями в микросервисной системе регистрации доменов.

## Описание

domain-service — это Spring Boot микросервис, который управляет регистрацией доменов (L2 и L3 уровня), DNS записями, синхронизацией с внешним DNS сервером и системой бронирования доменов при оплате.

## Основные функции

- Управление L2 доменами (зонами) и их DNS записями
- Управление L3 доменами пользователей
- Синхронизация DNS записей с внешним DNS сервером (exdns)
- Бронирование доменов на время оплаты
- Продление доменов
- Отправка уведомлений о регистрации и продлении доменов
- Логирование действий в audit-service
- RBAC авторизация (ADMIN/USER роли)

## Технологический стек

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA** — работа с базой данных PostgreSQL
- **Liquibase** — управление миграциями БД
- **Spring Security** — защита JWT-аутентификацией
- **OpenAPI Generator** — генерация API интерфейсов из YAML спецификации
- **Lombok** — сокращение бойлерплейта
- **HikariCP** — пул соединений с БД

## Структура проекта

```
domain-service/
├── config/                    # Конфигурация бинов Spring
│   ├── AuditConfig.java       # Конфигурация AuditClient
│   ├── ExdnsClientConfig.java # Конфигурация ExdnsClient
│   ├── JwtConfig.java         # Конфигурация JWT
│   ├── OpenApiConfig.java     # Настройка Swagger/OpenAPI
│   └── SecurityConfig.java    # Spring Security + JWT фильтр
├── controller/                # REST контроллеры
│   ├── L2DomainApiController.java    # Управление L2 доменами
│   ├── L3DomainApiController.java    # Управление L3 доменами
│   ├── DnsRecordApiController.java   # Управление DNS записями
│   ├── UserDomainApiController.java  # Управление доменами пользователей
│   ├── DomainReservationController.java # Бронирование доменов
│   ├── StatsApiController.java       # Статистика
│   └── ExpiringDomainsController.java # Домены, истекающие скоро
├── service/                   # Бизнес-логика
│   ├── L2DomainService.java   # Интерфейс сервиса L2 доменов
│   ├── L2DomainServiceImpl.java
│   ├── L3DomainService.java   # Интерфейс сервиса L3 доменов
│   ├── DnsRecordService.java  # Интерфейс сервиса DNS записей
│   ├── DnsRecordServiceImpl.java
│   ├── UserDomainService.java # Интерфейс сервиса доменов пользователей
│   ├── UserDomainServiceImpl.java
│   ├── DomainStatsService.java
│   ├── DomainReservationService.java # Бронирование доменов
│   └── DomainReservationServiceImpl.java
├── repository/                # Spring Data JPA репозитории
│   ├── DomainRepository.java
│   ├── DnsRecordRepository.java
│   ├── BadWordRepository.java
│   └── DomainReservationRepository.java
├── entity/                    # JPA сущности
│   ├── Domain.java            # Домен (L2/L3)
│   ├── DnsRecord.java         # DNS запись
│   ├── BadWord.java           # Запрещённые слова
│   └── DomainReservation.java # Бронирование
├── client/                    # Клиенты для внешних сервисов
│   ├── ExdnsClient.java       # Внешний DNS сервер
│   └── NotificationClient.java # Сервис уведомлений
├── security/                  # Слой безопасности
│   └── JwtAuthenticationFilter.java
└── util/
    └── SecurityUtil.java      # Утилиты для работы с SecurityContext
```

## Конфигурация

| Параметр | Описание | По умолчанию |
|----------|----------|--------------|
| `server.port` | Порт сервиса | 8082 |
| `JWT_SECRET` | Ключ для подписи JWT токенов | - |
| `EXDNS_API_TOKEN` | Токен для внешнего DNS API | changeme |
| `EXDNS_BASE_URL` | Базовый URL внешнего DNS | http://localhost:8000 |
| `NOTIFICATION_SERVICE_URL` | Базовый URL notification-service | http://localhost:8085 |
| `AUDIT_SERVICE_URL` | Базовый URL audit-service | http://localhost:8087 |
| `DOMAIN_RESERVATION_TTL_MINUTES` | Время жизни бронирования | 15 |

## API Endpoints

### L2 Домены

| Метод | Эндпоинт | Описание | Требуется роль |
|-------|----------|----------|----------------|
| GET | `/domains/l2Domains` | Получить все L2 домены | Нет |
| POST | `/domains/l2Domains` | Создать L2 домен (зону) | ADMIN |
| DELETE | `/domains/l2Domains/{l2Domain}` | Удалить L2 домен | ADMIN |
| POST | `/domains/l2Domains/{l2Domain}` | Создать DNS запись для L2 | ADMIN |
| GET | `/domains/l2Domains/{l2Domain}` | Получить DNS записи L2 домена | ADMIN |

### L3 Домены

| Метод | Эндпоинт | Описание | Требуется роль |
|-------|----------|----------|----------------|
| POST | `/domains/l3Domains/{l3Domain}` | Создать L3 домен с DNS записью | Authenticated |
| GET | `/domains/l3Domains/{l3Domain}/dnsRecords` | Получить DNS записи L3 домена | Authenticated (только свои) |
| POST | `/domains/l3Domains/{l3Domain}/ns` | Создать NS запись для L3 | Authenticated |
| GET | `/domains/l3Domains/{name}/free` | Получить свободные L3 домены | Нет |

### Домены пользователя

| Метод | Эндпоинт | Описание | Требуется роль |
|-------|----------|----------|----------------|
| GET | `/domains/userDomains` | Получить L3 домены пользователя | Authenticated |
| POST | `/domains/userDomains` | Создать L3 домены для пользователя | Authenticated |
| POST | `/domains/userDomains/renew` | Продлить L3 домены | Authenticated |
| GET | `/domains/userDomains/detailed` | Детальная информация о доменах | Authenticated |
| DELETE | `/domains/userDomains/expired` | Удалить истёкшие домены | ADMIN |
| GET | `/domains/userDomains/expiring?days${DB_USER:***REMOVED***}N` | Домены, истекающие через N дней | ADMIN |

### DNS Записи

| Метод | Эндпоинт | Описание | Требуется роль |
|-------|----------|----------|----------------|
| GET | `/domains/dnsRecords/{id}` | Получить DNS запись по ID | ADMIN |
| PUT | `/domains/dnsRecords/{id}` | Обновить DNS запись | Authenticated (только свои) |
| DELETE | `/domains/dnsRecords/{id}` | Удалить DNS запись | Authenticated (только свои) |

### Бронирование доменов

| Метод | Эндпоинт | Описание | Требуется роль |
|-------|----------|----------|----------------|
| POST | `/domains/reservations` | Забронировать домены на время оплаты | Authenticated |
| POST | `/domains/reservations/{paymentId}/confirm` | Подтвердить бронь (после оплаты) | Authenticated |
| DELETE | `/domains/reservations/{paymentId}` | Отменить бронь | Authenticated |
| POST | `/domains/reservations/cleanup` | Очистить истёкшие брони | Authenticated |

### Статистика

| Метод | Эндпоинт | Описание | Требуется роль |
|-------|----------|----------|----------------|
| GET | `/domains/stats` | Получить статистику доменов | ADMIN |

## Диаграммы

### Sequence Diagram — Регистрация L3 домена пользователем

```mermaid
sequenceDiagram
    participant U as Пользователь
    participant GW as API Gateway
    participant Domain as domain-service
    participant Security as JwtAuthenticationFilter
    participant DB as PostgreSQL
    participant Dns as Внешний DNS
    participant Audit as audit-service
    participant Notif as notification-service

    U->>GW: POST /api/domains/userDomains<br/>Authorization: Bearer JWT
    GW->>Domain: POST /domains/userDomains

    Domain->>Security: Проверить JWT
    Security->>Security: Извлечь userId
    Security-->>Domain: Токен валидный, userId

    Domain->>DB: Найти/создать L3 домены
    DB-->>Domain: Список доменов

    loop Для каждого домена
        Domain->>DB: Обновить даты
    end

    Domain->>Audit: Логировать: домены созданы
    Domain->>Notif: Отправить: домены активированы
    Notif-->>Domain: 200 OK

    Domain-->>GW: 200 OK<br/>domains
    GW-->>U: 200 OK
```

### Sequence Diagram — Управление DNS записями

```mermaid
sequenceDiagram
    participant U as Пользователь
    participant GW as API Gateway
    participant Domain as domain-service
    participant Security as JwtAuthenticationFilter
    participant DB as PostgreSQL
    participant Dns as Внешний DNS

    U->>GW: POST /api/domains/l3Domains/{l3Domain}
    GW->>Domain: POST /domains/l3Domains/{l3Domain}

    Domain->>Security: Проверить JWT и userId
    Security-->>Domain: userId

    Domain->>DB: Найти L2 домен
    DB-->>Domain: L2Domain

    Domain->>DB: Создать/найти L3 домен
    DB-->>Domain: L3Domain

    Domain->>DB: Создать DNS запись
    DB-->>Domain: DnsRecord

    Domain->>Dns: Получить зону
    Dns-->>Domain: Текущая версия

    Domain->>Domain: Собрать все записи

    Domain->>Dns: Обновить зону
    Dns-->>Domain: 200 OK

    Domain->>DB: Обновить версию
    DB-->>Domain: OK

    Domain-->>GW: 200 OK
    GW-->>U: 200 OK
```

### Sequence Diagram — Бронирование и оплата домена

```mermaid
sequenceDiagram
    participant U as Пользователь
    participant Payment as payment-service
    participant Domain as domain-service
    participant DB as PostgreSQL
    participant Notif as notification-service
    participant Audit as audit-service

    Note over U,Payment: Начало оплаты
    U->>Payment: Запрос на оплату доменов
    Payment->>Domain: POST /domains/reservations<br/>{paymentId, userId, domains}

    Domain->>DB: Проверить наличие доменов
    DB-->>Domain: OK

    Domain->>DB: Проверить существующие брони
    DB-->>Domain: Нет конфликтов

    Domain->>DB: Создать бронь с TTL
    DB-->>Domain: Бронь создана

    Domain-->>Payment: Домены забронированы

    Note over Payment: Обработка платежа...

    alt Успешная оплата
        Payment->>Domain: Подтвердить бронь
        Domain->>DB: Удалить бронь
        Domain->>DB: Создать домены

        Domain->>Notif: Отправить: домены активированы
        Domain->>Audit: Логировать: домены созданы

        Domain-->>Payment: OK
        Payment-->>U: Оплата успешна

    else Ошибка оплаты
        Payment->>Domain: Отменить бронь
        Domain->>DB: Удалить бронь
        Domain-->>Payment: Бронь снята
    end
```

### Sequence Diagram — Продление домена

```mermaid
sequenceDiagram
    participant U as Пользователь
    participant GW as API Gateway
    participant Domain as domain-service
    participant Security as JwtAuthenticationFilter
    participant DB as PostgreSQL
    participant Notif as notification-service
    participant Audit as audit-service

    U->>GW: POST /api/domains/userDomains/renew
    GW->>Domain: POST /domains/userDomains/renew

    Domain->>Security: Проверить JWT и userId
    Security-->>Domain: userId

    Domain->>DB: Найти домены пользователя
    DB-->>Domain: Список доменов

    loop Для каждого домена
        Domain->>DB: Проверить владельца
        Domain->>Domain: Рассчитать дату окончания
        Domain->>DB: Обновить дату
    end

    Domain->>Audit: Логировать: домены продлены
    Domain->>Notif: Отправить: домены продлены

    Domain-->>GW: 200 OK
    GW-->>U: Домены продлены
```

### Sequence Diagram — Безопасность и авторизация

```mermaid
sequenceDiagram
    participant C as Клиент
    participant Filter as JwtAuthenticationFilter
    participant SecurityConfig as SecurityConfig
    participant Service as Сервис
    participant Audit as audit-service

    C->>Filter: HTTP запрос с Authorization: Bearer JWT

    Filter->>Filter: Извлечь токен

    alt Токен отсутствует
        Filter-->>C: 401 Unauthorized
    else Токен присутствует
        Filter->>Filter: Проверить токен
        alt Токен невалидный
            Filter-->>C: 401 Unauthorized
        else Токен валидный
            Filter->>Filter: Извлечь userId, isAdmin
            Filter->>Filter: Создать аутентификацию

            Filter->>SecurityConfig: Проверить права
            SecurityConfig->>SecurityConfig: Сравнить путь и метод

            alt Публичный эндпоинт
                SecurityConfig-->>Service: Доступ разрешён
            else Требуется аутентификация
                SecurityConfig-->>Service: userId в контексте
            else Требуется роль ADMIN
                alt Пользователь ADMIN
                    SecurityConfig-->>Service: Доступ разрешён
                else Пользователь не ADMIN
                    SecurityConfig-->>C: 403 Forbidden
                end
            end

            Service->>Audit: Логировать: операция
            Service-->>Filter: Результат
            Filter-->>C: 200 OK
        end
    end
```

### BPMN Diagram — Общий поток регистрации домена

```mermaid
flowchart TD
    Start([Начало]) --> Auth[Проверить JWT токен]

    Auth --> IsValid{Токен<br/>валидный?}
    IsValid -- Нет --> Return401([401 Unauthorized])
    IsValid -- Да --> ExtractUser[Извлечь userId и роли]

    ExtractUser --> ParseDomain[Распарсить L3 домен<br/>выделить L2]
    ParseDomain --> FindL2[Найти L2 домен в БД]

    FindL2 --> L2Exists{L2<br/>существует?}
    L2Exists -- Нет --> Return404([404 Not Found<br/>L2 не найден])
    L2Exists -- Да --> CheckDuplicate{Домен уже<br/>занят?}

    CheckDuplicate -- Да --> Return409([409 Conflict<br/>Домен занят])
    CheckDuplicate -- Нет --> CheckBad{Содержит<br/>запрещённое слово?}

    CheckBad -- Да --> Return400([400 Bad Request<br/>Запрещённое слово])
    CheckBad -- Нет --> CreateL3[Создать L3 домен в БД<br/>userId, activatedAt, finishedAt]

    CreateL3 --> SetPeriod[Установить период<br/>MONTH или YEAR]
    SetPeriod --> SaveDomain[Сохранить в БД]

    SaveDomain --> Audit[Лог: домены созданы]
    Audit --> Notif[Уведомление: домены активированы]

    Notif --> Return200([200 OK<br/>Домен зарегистрирован])

    Start -.-> Return401
    Return401 -.-> End([Конец])
    Return404 -.-> End
    Return409 -.-> End
    Return400 -.-> End
    Return200 -.-> End

    style Start fill:#e1f5e1
    style End fill:#fce1e1
    style Return200 fill:#e1f5e1
    style Return401 fill:#ffe1e1
    style Return404 fill:#ffe1e1
    style Return409 fill:#ffe1e1
    style Return400 fill:#ffe1e1
```

### BPMN Diagram — Процесс синхронизации DNS с внешним сервером

```mermaid
flowchart TD
    Start([Запрос DNS операции]) --> ExtractL2[Получить L2 домен<br/>из L3 или напрямую]

    ExtractL2 --> GetZone[Получить текущую зону]
    GetZone --> ZoneExists{Зона существует<br/>в DNS?}

    ZoneExists -- Нет --> CreateZone[Создать новую зону]
    ZoneExists -- Да --> GetAllRecords[Получить все записи<br/>из БД]

    CreateZone --> GetAllRecords

    GetAllRecords --> BuildZone[Собрать zoneBody<br/>увеличить версию]

    BuildZone --> ReplaceZone[Обновить зону]
    ReplaceZone --> SyncSuccess{Синхронизация<br/>успешна?}

    SyncSuccess -- Нет --> Return502([502 Bad Gateway<br/>Ошибка DNS])
    SyncSuccess -- Да --> UpdateVersion[Обновить версию в БД]

    UpdateVersion --> Return200([200 OK<br/>DNS синхронизирован])

    Start -.-> Return502
    Return502 -.-> End([Конец])
    Return200 -.-> End

    style Start fill:#e1f5e1
    style End fill:#fce1e1
    style Return200 fill:#e1f5e1
    style Return502 fill:#ffe1e1
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

    subgraph Domain["domain-service :8082"]
        Filter[JwtAuthenticationFilter]
        Controllers[Controllers]
        Services[Services]
        Repositories[Repositories]
    end

    subgraph External["Внешние сервисы"]
        DB[(PostgreSQL)]
        Exdns[exdns :8000]
        Notif[notification-service :8085]
        Audit[audit-service :8087]
    end

    Client -->|Request: /api/domains/*| Router
    Router -->|Forwarded: /domains/*| Filter
    Filter --> Controllers
    Controllers --> Services
    Services --> Repositories
    Repositories <--> DB

    Services -->|ExdnsClient| Exdns
    Services -->|NotificationClient| Notif
    Services -->|AuditClient| Audit

    Services -.-> Controllers
    Controllers -.-> Router
    Router -.-> Client

    style Gateway fill:#e3f2fd
    style Domain fill:#4caf50,color:#fff
    style DB fill:#ff9800,color:#fff
    style External fill:#fff3e0
```

### BPMN Diagram — Бронирование и оплата домена

```mermaid
flowchart TD
    Start([Начало оплаты]) --> ReserveRequest[POST /domains/reservations<br/>paymentId, userId, domains]

    ReserveRequest --> CheckL2[Проверить существование L2]
    CheckL2 --> L2Valid{Все L2<br/>существуют?}
    L2Valid -- Нет --> Return404([404 Not Found<br/>L2 не найден])

    L2Valid -- Да --> CheckOwned{Домены уже<br/>заняты?}
    CheckOwned -- Да --> Return409([409 Conflict<br/>Домен занят])

    CheckOwned -- Нет --> CheckReservation{Есть активные<br/>брони?}
    CheckReservation -- Да --> Return409([409 Conflict<br/>Домен забронирован])

    CheckReservation -- Нет --> CreateReservation[Создать бронь<br/>expiresAt ${DB_USER:***REMOVED***} now + TTL]

    CreateReservation --> Return202([202 Accepted<br/>Домены забронированы])

    Return202 --> Payment[Обработка платежа]

    Payment --> PaymentResult{Результат<br/>оплаты}

    PaymentResult -- Успех --> Confirm[Подтвердить бронь]
    Confirm --> DeleteReservation[Удалить бронь]
    DeleteReservation --> CreateDomains[Создать домены в БД]
    CreateDomains --> AuditCreate[Лог: домены созданы]
    AuditCreate --> NotifCreate[Уведомление отправлено]
    NotifCreate --> PaymentSuccess([Успех<br/>домены созданы])

    PaymentResult -- Отмена --> Cancel[Отменить бронь]
    Cancel --> DeleteReservation2[Удалить бронь]
    DeleteReservation2 --> PaymentFailed([Отмена<br/>бронь снята])

    Start -.-> Return404
    Return404 -.-> End([Конец])
    Return409 -.-> End
    PaymentSuccess -.-> End
    PaymentFailed -.-> End

    style Start fill:#e1f5e1
    style End fill:#fce1e1
    style PaymentSuccess fill:#e1f5e1
    style PaymentFailed fill:#ffe1e1
    style Return404 fill:#ffe1e1
    style Return409 fill:#ffe1e1
    style Return202 fill:#fff9e6
```

## Зависимости между сервисами

```mermaid
graph LR
    Domain[domain-service]
    Exdns[exdns<br/>:8000]
    Notif[notification-service<br/>:8085]
    Audit[audit-service<br/>:8087]
    DB[(PostgreSQL<br/>:5432)]

    Domain -->|HTTP| Exdns
    Domain -->|HTTP| Notif
    Domain -->|HTTP| Audit
    Domain -->|JPA| DB

    style Domain fill:#4caf50,color:#fff
    style DB fill:#ff9800,color:#fff
    style Notif fill:#2196f3,color:#fff
    style Audit fill:#f44336,color:#fff
```

## Жизненный цикл домена

```mermaid
stateDiagram-v2
    [*] --> Свободен: Домен доступен
    Свободен --> Забронирован: Создана запись в domain_reservation
    Забронирован --> Забронирован: Продлён TTL (опционально)
    Забронирован --> Свободен: Отмена оплаты / Истечение TTL
    Забронирован --> Активен: Подтверждение оплаты

    Активен --> Активен: Продление (userDomains/renew)
    Активен --> Истёк: finishedAt < now

    Свободен --> Удалён: Удаление L2 домена (ADMIN)
    Активен --> Удалён: Удаление L2/L3 домена

    Истёк --> [*]: Автоматическое удаление<br/>или очищение администратором
    Удалён --> [*]

    note right of Забронирован
        TTL: 15 минут (по умолчанию)
        Защита от гонок при оплате
    end note

    note right of Активен
        userId закреплён
        activatedAt установлено
        finishedAt ${DB_USER:***REMOVED***} activatedAt + период
    end note
```

## Логирование

### Аудит события
Сервис отправляет события аудита в audit-service:
- `"Domains created"` — при создании доменов пользователем
- `"Domains renewed"` — при продлении доменов
- `"Expired domains deleted: N"` — при удалении истёкших доменов

### Уведомления
Сервис отправляет уведомления в notification-service:
- `DOMAIN_ACTIVATED` — при регистрации домена
- `DOMAIN_RENEWED` — при продлении домена

## Security

- JWT токены проверяются через `JwtUtil` из модуля `common`
- Тип токена: `access`
- userId извлекается из токена и сохраняется как principal в Spring Security Context
- Роли:
  - `ROLE_USER` — все аутентифицированные пользователи
  - `ROLE_ADMIN` — администраторы
- Проверка владельца домена на уровне сервисов:
  - Пользователи могут менять только свои домены и DNS записи
  - Администраторы могут менять любые домены

## Мониторинг

Actuator эндпоинты:
- `/actuator/health` — состояние сервиса
- `/actuator/info` — информация о сервисе
- `/actuator/metrics` — метрики приложения

## Swagger UI

Документация API доступна по адресу: `http://localhost:8082/swagger-ui.html`

## Типы DNS записей

Поддерживаемые типы DNS записей:
- `A` — IPv4 адрес
- `AAAA` — IPv6 адрес
- `NS` — Name Server
- `MX` — Mail Exchange
- `TXT` — Текстовая запись
- `CNAME` — Каноническое имя
- `SOA` — Start of Authority

## Периоды регистрации доменов

- `MONTH` — 1 месяц
- `YEAR` — 1 год

При продлении:
- Если домен ещё активен: `finishedAt ${DB_USER:***REMOVED***} finishedAt + period`
- Если домен истёк: `finishedAt ${DB_USER:***REMOVED***} now + period`

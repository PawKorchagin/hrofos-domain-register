# Hrofos domain register

Микросервисная система регистрации доменов и управления DNS.

## Описание

Hrofos domain register — это распределённая система из микросервисов для регистрации доменов, управления DNS записями, обработки платежей и отправки уведомлений. Система построена на архитектуре Spring Boot с использованием PostgreSQL баз данных, React фронтенда и собственного Elixir DNS сервера.

## Основные функции

- Регистрация и аутентификация пользователей с JWT токенами
- Двухфакторная аутентификация (TOTP, Google Authenticator)
- Управление L2 и L3 доменами
- Управление DNS записями (A, AAAA, NS, MX, TXT, CNAME, SOA)
- Корзина доменов и оформление заказов
- Интеграция с платёжной системой YooKassa
- Отправка email-уведомлений
- Административные отчёты
- Аудит всех действий пользователей
- Планировщик задач для истекающих доменов

## Технологический стек

### Backend (Java)
- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security** — JWT аутентификация
- **Spring Cloud Gateway** — API Gateway
- **Spring Data JPA** — работа с данными
- **PostgreSQL** — базы данных для каждого сервиса
- **Liquibase** — миграции баз данных
- **JJWT 0.12.3** — создание и валидация JWT
- **TOTP 1.7.1** — двухфакторная аутентификация
- **OpenAPI Generator** — генерация API из YAML
- **SpringDoc OpenAPI** — Swagger UI

### Frontend
- **React 19**
- **TypeScript**
- **Rsbuild** (Vite альтернатива)
- **Chakra UI v3** + **Emotion**
- **MobX** — управление состоянием
- **react-router** — навигация
- **Orval** — генерация API клиентов из OpenAPI

### DNS Server
- **Elixir 1.18**
- **Bandit** — HTTP сервер
- **libcluster** — кластеризация узлов


### Сервисы

| Сервис               | Порт | Описание                          |
|----------------------|------|-----------------------------------|
| frontend             | 3000 | React клиентское приложение       |
| api-gateway          | 8080 | Единая точка входа, маршрутизация |
| auth-service         | 8081 | Аутентификация, пользователи, 2FA |
| domain-service       | 8082 | Управление доменами и DNS         |
| payment-service      | 8083 | Обработка платежей (YooKassa)     |
| order-service        | 8084 | Корзина и оформление заказов      |
| notification-service | 8085 | Отправка email уведомлений        |
| admin-service        | 8086 | Административные отчёты           |
| audit-service        | 8087 | Аудит действий пользователей      |
| scheduler-service    | 8088 | Планировщик задач                 |
| exdns (HTTP)         | 8000 | HTTP API DNS сервера              |
| exdns (DNS)          | 5353 | UDP/TCP DNS сервер                |

## Диаграммы

### Sequence Diagram — Регистрация пользователя и верификация email

```mermaid
sequenceDiagram
    participant U as Пользователь
    participant GW as API Gateway
    participant Auth as auth-service
    participant DB as auth_db
    participant Notif as notification-service
    participant Audit as audit-service

    U->>GW: POST /api/auth/register<br/>{email, password}
    GW->>Auth: POST /auth/register

    Auth->>DB: Найти пользователя по email
    DB-->>Auth: email свободен

    Auth->>Auth: Хешировать пароль<br/>Создать токен верификации

    Auth->>DB: Сохранить пользователя
    DB-->>Auth: Пользователь создан

    Auth->>Audit: Логирование: "Пользователь зарегистрирован"
    Auth->>Notif: Отправить уведомление<br/>EMAIL_VERIFICATION

    Notif->>Notif: Получить email пользователя
    Notif->>Notif: Сгенерировать HTML письмо
    Notif->>U: Email с ссылкой верификации

    Auth-->>GW: 201 Created
    GW-->>U: 201 Created

    Note over U: Пользователь переходит по ссылке

    U->>GW: GET /api/auth/verify-email?token${DB_USER:***REMOVED***}xxx
    GW->>Auth: GET /auth/verify-email

    Auth->>DB: Отметить email как подтверждённый
    Auth->>Audit: Логирование: "Email подтверждён"

    Auth-->>GW: 200 OK
    GW-->>U: 200 OK
```

### Sequence Diagram — Вход в систему с 2FA

```mermaid
sequenceDiagram
    participant U as Пользователь
    participant GW as API Gateway
    participant Auth as auth-service
    participant DB as auth_db
    participant TOTP as TOTP-верификатор
    participant Audit as audit-service

    U->>GW: POST /api/auth/login<br/>{email, password, totpCode?}
    GW->>Auth: POST /auth/login

    Auth->>DB: Найти пользователя по email
    DB-->>Auth: Пользователь

    Auth->>Auth: Проверить пароль

    alt 2FA включена
        Auth->>DB: Получить секрет 2FA
        Auth->>TOTP: Проверить код

        alt Код неверный
            Auth-->>GW: 400 Неверный TOTP код
            GW-->>U: 400 Bad Request
        end
    end

    Auth->>DB: Удалить старые refresh-токены
    Auth->>DB: Сохранить новый refresh-токен

    Auth->>Auth: Создать access-токен (15 мин)<br/>Создать refresh-токен (30 дней)

    Auth->>Audit: Логирование: "Пользователь вошёл"

    Auth-->>GW: 200 OK<br/>{accessToken, refreshToken, userId, email}
    GW-->>U: Токены получены
```

### Sequence Diagram — Оформление заказа на домены

```mermaid
sequenceDiagram
    participant U as Пользователь
    participant GW as API Gateway
    participant Order as order-service
    participant Domain as domain-service
    participant Payment as payment-service
    participant DB as order_db
    participant Notif as notification-service
    participant Audit as audit-service

    Note over U: Этап 1: Оформление корзины

    loop Добавление доменов
        U->>GW: POST /api/orders/cart/{domain}
        GW->>Order: POST /orders/cart/{domain}
        Order->>DB: Добавить домен в корзину
    end

    U->>GW: POST /api/orders/cart/checkout<br/>{period: MONTH/YEAR}
    GW->>Order: POST /orders/cart/checkout

    Order->>DB: Получить корзину пользователя
    Order->>Order: Рассчитать стоимость<br/>(кол-во * цена * множитель)

    Order->>Payment: Создать платёж<br/>{domains, amount, currency}
    Payment->>Domain: Забронировать домены<br/>на время оплаты
    Domain->>Domain: Создать запись бронирования<br/>с TTL

    Payment-->>Order: 200 OK<br/>{paymentId, paymentUrl}

    Order->>DB: Очистить корзину
    Order->>Audit: Логирование: "Платёж инициирован"

    Order-->>GW: 200 OK<br/>{paymentId, paymentUrl}
    GW-->>U: Платёжная ссылка

    Note over U: Пользователь оплачивает

    Payment->>Payment: Обработка через YooKassa

    alt Успешная оплата
        Payment->>Domain: Подтвердить бронь
        Domain->>Domain: Создать домены в БД
        Domain->>Domain: Синхронизировать DNS
        Domain->>Notif: Отправить: домены активированы
        Domain->>Audit: Логировать: домены созданы

        Payment->>Notif: Отправить: платёж одобрен
        Payment->>Order: Успех
    else Оплата не удалась
        Payment->>Domain: Отменить бронь
        Domain->>Domain: Удалить запись бронирования
    end
```

### Sequence Diagram — Управление DNS записями

```mermaid
sequenceDiagram
    participant U as Пользователь
    participant GW as API Gateway
    participant Domain as domain-service
    participant DB as domain_db
    participant Dns as DNS-сервер
    participant Query as DNS запрос
    participant Audit as audit-service

    U->>GW: POST /api/domains/l3Domains/sub.example.com<br/>{type: A, value: 1.2.3.4}
    GW->>Domain: POST /domains/l3Domains/sub.example.com

    Domain->>Domain: Проверить JWT и userId
    Domain->>DB: Найти L2 домен (example.com)
    Domain->>DB: Создать L3 домен (sub.example.com)
    Domain->>DB: Создать DNS запись

    Domain->>Dns: Получить текущую зону<br/>Bearer token
    Dns-->>Domain: Текущая версия

    Domain->>Domain: Собрать все записи
    Domain->>Dns: Обновить зону<br/>{version, records}
    Dns-->>Domain: 200 OK

    Domain->>DB: Обновить версию зоны

    Domain->>Audit: Логировать: запись создана

    Domain-->>GW: 200 OK
    GW-->>U: DNS запись создана

    Note over Query: DNS запрос к серверу
    Query->>Dns: A sub.example.com
    Dns-->>Query: 1.2.3.4
```

### Sequence Diagram — Продление домена

```mermaid
sequenceDiagram
    participant U as Пользователь
    participant GW as API Gateway
    participant Order as order-service
    participant Domain as domain-service
    participant Payment as payment-service
    participant Notif as notification-service
    participant Audit as audit-service

    U->>GW: POST /api/orders/domains/renew<br/>{domains, period}
    GW->>Order: POST /orders/domains/renew

    Order->>Domain: Продлить домены<br/>{domains, period}

    Domain->>Domain: Для каждого домена:<br/>Проверить владельца<br/>Рассчитать дату окончания

    Domain->>Domain: Обновить finishedAt в БД

    Domain->>Audit: Логировать: домены продлены
    Domain->>Notif: Отправить уведомление о продлении

    Domain-->>Order: Список продлённых доменов
    Order-->>GW: 200 OK
    GW-->>U: Домены продлены

    Note over Domain: Если домены платные:<br/>создаётся платёж через PaymentService
```

### Sequence Diagram — Генерация административного отчёта

```mermaid
sequenceDiagram
    participant Admin as Администратор
    participant GW as API Gateway
    participant Svc as admin-service
    participant Auth as auth-service
    participant Domain as domain-service
    participant Audit as audit-service

    Admin->>GW: GET /api/admin/report<br/>Authorization: Bearer JWT
    GW->>Svc: GET /admin/report

    Svc->>Svc: Проверить роль ADMIN

    Svc->>Auth: Получить кол-во пользователей
    Auth-->>Svc: 150 пользователей

    Svc->>Domain: Получить статистику доменов
    Domain-->>Svc: {активных: 85,<br/>доменов: 320}

    Svc->>Svc: Сформировать отчёт в Markdown

    Svc->>Audit: Логировать: отчёт сформирован

    Svc-->>GW: 200 OK
    GW-->>Admin: Файл отчёта
```

### Sequence Diagram — Планировщик истекающих доменов

```mermaid
sequenceDiagram
    participant Sch as scheduler-service
    participant Domain as domain-service
    participant Notif as notification-service
    participant Audit as audit-service

    loop Ежедневно в 00:00
        Sch->>Sch: Запуск по расписанию

        Sch->>Domain: Получить домены, истекающие через 7 дней
        Domain-->>Sch: Список доменов

        loop Для каждого домена
            Sch->>Notif: Отправить уведомление о сроке
            Notif->>Notif: Отправить email владельцу
        end

        Sch->>Domain: Удалить истёкшие домены
        Domain->>Domain: Удалить из БД
        Domain->>Domain: Синхронизировать DNS

        Sch->>Audit: Логировать: очистка выполнена
    end
```

### BPMN Diagram — Полный флоу регистрации домена

```mermaid
flowchart TB
    subgraph UserLane["         Пользователь"]
        Start([Начало])
        WaitVerify[Ожидание клика в email]
        AddToCart[Добавить домены в корзину]
        Checkout[Оформить заказ<br/>Выбор периода]
        Success([Домен активирован])
        Fail([Домен не зарегистрирован])
        End([Конец])
    end

    subgraph AuthLane["         auth-service"]
        Register[Зарегистрироваться]
        RegSuccess[Пользователь создан]
        VerifyEmail[Подтвердить email]
        EmailVerified{Email<br/>подтверждён?}
        Login[Войти в систему]
        GetToken[Получить JWT токены]
    end

    subgraph OrderLane["         order-service"]
        Calculate[Рассчитать стоимость]
    end

    subgraph PaymentLane["         payment-service"]
        CreatePayment[Создать платёж]
        Reserve[Забронировать домены]
        UserPay{Оплата<br/>успешна?}
    end

    subgraph DomainLane["         domain-service"]
        Confirm[Подтвердить бронь]
        CreateDomains[Создать домены в БД]
        SyncDNS[Синхронизировать DNS]
        CancelReserve[Отменить бронь]
    end

    subgraph NotifLane["         notification-service"]
        Notify[Отправить уведомления]
    end

    subgraph AuditLane["         audit-service"]
        AuditLog[Записать в аудит]
    end

    Start --> RegOrLogin{Зарегистрирован?}

    RegOrLogin -- Нет --> Register
    Register --> RegSuccess
    RegSuccess --> VerifyEmail
    VerifyEmail --> EmailVerified

    EmailVerified -- Нет --> WaitVerify
    WaitVerify --> EmailVerified

    EmailVerified -- Да --> Login
    RegOrLogin -- Да --> Login

    Login --> GetToken
    GetToken --> AddToCart

    AddToCart --> Checkout
    Checkout --> Calculate
    Calculate --> CreatePayment
    CreatePayment --> Reserve

    Reserve --> UserPay

    UserPay -- Да --> Confirm
    Confirm --> CreateDomains
    CreateDomains --> SyncDNS

    SyncDNS --> Notify
    Notify --> AuditLog

    AuditLog --> Success

    UserPay -- Нет --> CancelReserve
    CancelReserve --> Fail

    Start -.-> Fail
    Fail -.-> End
    Success -.-> End

    style Start fill:#e1f5e1
    style End fill:#fce1e1
    style Success fill:#a5d6a7
    style Fail fill:#ef9a9a
```

### BPMN Diagram — Поток запроса через API Gateway

```mermaid
flowchart LR
    subgraph FrontendLane["         Frontend"]
        Client[React/браузер]
    end

    subgraph GatewayLane["         API Gateway"]
        Router[Маршрутизатор]
        Cors[CORS фильтр]
        RLim[Ограничение запросов]
    end

    subgraph ServicesLane["         Микросервисы"]
        Auth[auth-service<br/>Аутентификация]
        Domain[domain-service<br/>Домены и DNS]
        Payment[payment-service<br/>Платежи]
        Order[order-service<br/>Корзина и заказы]
        Notif[notification-service<br/>Email уведомления]
        Admin[admin-service<br/>Отчёты]
        Audit[audit-service<br/>Аудит логи]
        Scheduler[scheduler-service<br/>Задачи]
    end

    subgraph ExternalLane["         Внешние сервисы"]
        Exdns[exdns<br/>DNS сервер]
        SMTP[SMTP<br/>Yandex Postbox]
        YooKassa[YooKassa API]
    end

    subgraph DBLane["         Базы данных PostgreSQL"]
        AuthDB[(auth_db)]
        DomainDB[(domain_db)]
        PaymentDB[(payment_db)]
        OrderDB[(order_db)]
        NotifDB[(notification_db)]
        AuditDB[(audit_db)]
    end

    Client -->|HTTP запросы| Router
    Router --> Cors
    Cors --> RLim

    RLim -->|/api/auth/**| Auth
    RLim -->|/api/domains/**| Domain
    RLim -->|/api/payments/**| Payment
    RLim -->|/api/orders/**| Order
    RLim -->|/api/notifications/**| Notif
    RLim -->|/api/admin/**| Admin
    RLim -->|/api/audit/**| Audit
    RLim -->|/api/scheduler/**| Scheduler

    Auth <--> AuthDB
    Auth -->|JWT| Audit

    Domain <--> DomainDB
    Domain -->|HTTP| Exdns
    Domain -->|HTTP| Notif
    Domain -->|HTTP| Audit

    Payment <--> PaymentDB
    Payment -->|HTTP| YooKassa
    Payment -->|HTTP| Domain

    Order <--> OrderDB
    Order -->|HTTP| Payment
    Order -->|HTTP| Domain
    Order -->|HTTP| Audit

    Notif <--> NotifDB
    Notif -->|SMTP| SMTP

    Admin -->|HTTP| Auth
    Admin -->|HTTP| Domain
    Admin -->|HTTP| Audit

    Scheduler -->|HTTP| Domain
    Scheduler -->|HTTP| Auth
    Scheduler -->|HTTP| Notif

    Audit <--> AuditDB

    style GatewayLane fill:#e3f2fd
    style FrontendLane fill:#f5f5f5
    style ExternalLane fill:#fff3e0
    style DBLane fill:#e8f5e9
```

### BPMN Diagram — Аутентификация и авторизация

```mermaid
flowchart TB
    subgraph ClientLane["         Клиент"]
        Start([Запрос к API])
        Return401([401 Unauthorized])
        Return403([403 Forbidden])
        Return200([200 OK])
        End([Конец])
    end

    subgraph SecurityLane["         Безопасность"]
        HasToken{Передан<br/>Authorization<br/>Bearer?}
        JwtFilter[JWT фильтр]
        Validate[Проверить токен]
        IsValid{Токен<br/>валидный?}
        ExtractClaims[Извлечь userId,<br/>email, isAdmin]
        CreateAuth[Создать аутентификацию<br/>ROLE_USER + ROLE_ADMIN?]
        SecurityCtx[Установить в контекст]
        IsPublic{Публичный<br/>эндпоинт?}
        IsAuthenticated{Требуется<br/>аутентификация?}
        HasAdmin{Требуется<br/>роль ADMIN?}
        CheckAdmin{Пользователь<br/>ADMIN?}
        PassFilter[Разрешить доступ]
    end

    subgraph ServiceLane["         Сервис"]
        Service[Вызвать сервис]
        GetUserId[Получить userId]
        BusinessLogic[Выполнить бизнес-логику]
    end

    subgraph AuditLane["         audit-service"]
        AuditLog[Записать в аудит]
    end

    Start --> HasToken

    HasToken -- Нет --> Return401
    HasToken -- Да --> JwtFilter

    JwtFilter --> Validate
    Validate --> IsValid
    IsValid -- Нет --> Return401
    IsValid -- Да --> ExtractClaims

    ExtractClaims --> CreateAuth
    CreateAuth --> SecurityCtx

    SecurityCtx --> IsPublic

    IsPublic -- Да --> PassFilter
    PassFilter --> Service

    IsPublic -- Нет --> IsAuthenticated

    IsAuthenticated -- Нет --> Service
    IsAuthenticated -- Да --> HasAdmin

    HasAdmin -- Нет --> Service
    HasAdmin -- Да --> CheckAdmin

    CheckAdmin -- Нет --> Return403
    CheckAdmin -- Да --> Service

    Service --> GetUserId
    GetUserId --> BusinessLogic
    BusinessLogic --> AuditLog

    AuditLog --> Return200

    Start -.-> Return401
    Return401 -.-> End
    Return403 -.-> End
    Return200 -.-> End

    style Start fill:#e1f5e1
    style End fill:#fce1e1
    style Return200 fill:#a5d6a7
    style Return401 fill:#ef9a9a
    style Return403 fill:#ffcc80
```

### BPMN Diagram — Жизненный цикл домена

```mermaid
flowchart TB
    subgraph OrderLane["         order-service"]
        InCart[В корзине]
        Checkout[Оформление заказа]
    end

    subgraph DomainLane["         domain-service"]
        Start([Свободен])
        Cancel[Отмена брони]
        Active([Активен])
        Reserved[Забронирован<br/>TTL: 15 мин]
        Renewed[Продлён<br/>finishedAt + period]
        Expired[Истёк<br/>finishedAt < now]
    end

    subgraph PaymentLane["         payment-service"]
        Payment{Оплата<br/>успешна?}
    end

    subgraph NotifLane["         notification-service"]
        Reminder[Напоминание]
    end

    subgraph SchedulerLane["         scheduler-service"]
        Cleanup[Удалить истёкшие]
    end

    subgraph UserLane["         Пользователь"]
    Renew{Пользователь<br/>продлевает?}
    RenewCheck{Продлён до<br/>истечения?}
        Delete{Админ<br/>удаляет?}
        Deleted[Удалён]
        End([Конец])
    end

    Start --> InCart
    InCart --> Checkout
    Checkout --> Reserved

    Reserved --> Payment

    Payment -- Да --> Active
    Payment -- Нет --> Cancel
    Cancel --> Start

    Active --> Renew

    Renew -- Да --> Renewed
    Renewed --> Active

    Active --> Expiring{Истекает<br/>через 7 дней?}

    Expiring -- Да --> Reminder
    Reminder --> RenewCheck

    RenewCheck -- Да --> Active
    RenewCheck -- Нет --> Expired

    Expired --> Cleanup
    Cleanup --> Start

    Active --> Delete
    Delete -- Да --> Deleted
    Deleted --> End

    Start -.-> End

    style Start fill:#a5d6a7
    style Active fill:#81c784
    style Reserved fill:#fff59d
    style Expired fill:#ef9a9a
    style Deleted fill:#b0bec5
    style End fill:#fce1e1
```

## JWT Токены

### Типы токенов

| Тип           | Срок действия | Хранение                     | Назначение                     |
|---------------|---------------|------------------------------|--------------------------------|
| Access Token  | 15 минут      | Клиент (localStorage/cookie) | Доступ к защищённым эндпоинтам |
| Refresh Token | 30 дней       | База данных (refresh_token)  | Получение нового access token  |

### Payload Access Token

```json
{
  "sub": "550e8400-e29b-41d4-a716-446655440000",
  "email": "user@example.com",
  "isAdmin": false,
  "type": "access",
  "iat": 1234567890,
  "exp": 1234568790
}
```

## Базы данных

Каждый сервис имеет собственную PostgreSQL базу данных:

| Сервис               | База данных     |
|----------------------|-----------------|
| auth-service         | auth_db         |
| domain-service       | domain_db       |
| payment-service      | payment_db      |
| order-service        | order_db        |
| notification-service | notification_db |
| audit-service        | audit_db        |

Миграции управляются через Liquibase и применяются автоматически при запуске.

## Аудит

Сервисы отправляют события аудита в `audit-service` через `AuditClient` (fail-silent):
- `"User registered: {email}"` — при регистрации
- `"User logged in"` — при входе
- `"Email verified"` — при верификации email
- `"Domains created"` — при создании доменов
- `"Payment initiated: {paymentId}"` — при инициации платежа
- `"Admin report generated"` — при генерации отчёта

## Типы уведомлений

| Тип                  | Описание                      |
|----------------------|-------------------------------|
| ORDER_CREATED        | Создание заказа               |
| PAYMENT_APPROVED     | Успешная оплата               |
| DOMAIN_ACTIVATED     | Активация домена              |
| DOMAIN_EXPIRING_SOON | Истечение срока (напоминание) |
| DOMAIN_EXPIRED       | Истечение срока               |
| DOMAIN_RENEWED       | Продление домена              |
| EMAIL_VERIFICATION   | Верификация email             |

## Мониторинг

Все сервисы предоставляют Actuator эндпоинты:
- `/actuator/health` — состояние сервиса
- `/actuator/info` — информация о сервисе
- `/actuator/metrics` — метрики приложения

## Swagger UI

Документация API каждого сервиса доступна:
- auth-service: `http://localhost:8081/swagger-ui.html`
- domain-service: `http://localhost:8082/swagger-ui.html`
- и т.д.

## Логика доступа

### Роли пользователей

- **ROLE_USER** — все аутентифицированные пользователи
- **ROLE_ADMIN** — администраторы (доступ к административным эндпоинтам)

### Правила доступа

- Публичные эндпоинты (без токена): регистрация, login, verify email, Swagger UI
- Аутентифицированные: защищённые эндпоинты для авторизованных пользователей
- ADMIN: статистика, отчёты, управление L2 доменами

### Владение ресурсами

- Пользователи могут управлять только своими доменами и DNS записями
- Администраторы могут управлять любыми ресурсами
- Проверка владельца выполняется на уровне сервисов

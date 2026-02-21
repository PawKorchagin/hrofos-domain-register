# order-service

Сервис для управления корзиной и оформления заказов в микросервисной системе регистрации доменов.

## Описание

order-service — это Spring Boot микросервис, который управляет корзиной доменов пользователя, рассчитывает стоимости и инициирует оплаты через интеграцию с payment-service. Сервис хранит состояние корзины в PostgreSQL базе данных и взаимодействует с другими сервисами через REST API.

## Основные функции

- Добавление L3-доменов в корзину пользователя
- Просмотр текущей корзины с расчётом стоимостей (месячная и годовая)
- Оформление заказа с выбором периода (MONTH/YEAR)
- Инициирование платежа через payment-service
- Продление сроков действия доменов
- Логирование действий в audit-service
- Защита всех операций JWT-аутентификацией

## Технологический стек

- **Java 17**
- **Spring Boot 3.2.0**
- **PostgreSQL** — хранение данных корзины
- **Liquibase** — миграции базы данных
- **OpenAPI Generator** — генерация API интерфейсов из YAML спецификации
- **Spring Security** — защита JWT-аутентификацией
- **HikariCP** — пул соединений к базе данных
- **Lombok** — сокращение бойлерплейта

## Структура проекта

```
order-service/
├── config/                    # Конфигурация бинов Spring
│   ├── AuditConfig.java       # Конфигурация AuditClient
│   ├── DomainClientConfig.java # Настройка DomainClient
│   ├── JwtConfig.java         # Конфигурация JWT
│   ├── OpenApiConfig.java     # Настройка Swagger/OpenAPI
│   ├── PaymentClientConfig.java # Настройка PaymentClient
│   └── SecurityConfig.java    # Spring Security + JWT фильтр
├── controller/                # REST контроллеры
│   ├── CartApiController.java    # Операции с корзиной
│   └── DomainApiController.java  # Операции с доменами (продление)
├── service/                   # Бизнес-логика
│   ├── CartService.java
│   └── impl/CartServiceImpl.java
├── client/                    # Клиенты для других сервисов
│   ├── DomainClient.java         # Клиент domain-service
│   ├── DomainClientException.java
│   ├── DomainClientProperties.java
│   ├── PaymentClient.java        # Клиент payment-service
│   ├── PaymentClientException.java
│   ├── PaymentClientProperties.java
│   ├── PaymentCreateRequest.java
│   └── PaymentCreateResponse.java
├── entity/                    # Сущности JPA
│   ├── Cart.java               # Сущность корзины
│   └── CartId.java             # Составной первичный ключ
├── repository/                # Репозитории JPA
│   └── CartRepository.java
├── security/                  # Слой безопасности
│   └── JwtAuthenticationFilter.java
└── resources/
    ├── db/changelog/           # Liquibase миграции
    ├── static/
    │   └── openapi.yaml        # Спецификация OpenAPI
    └── application.yml         # Конфигурация приложения
```

## API Endpoints

| Метод | Эндпоинт                  | Описание                               | Требуется роль |
|-------|---------------------------|----------------------------------------|----------------|
| GET   | `/orders/cart/me`         | Получить корзину текущего пользователя | Аутентификация |
| POST  | `/orders/cart/{l3Domain}` | Добавить домен в корзину               | Аутентификация |
| POST  | `/orders/cart/checkout`   | Оформить заказ (создать платёж)        | Аутентификация |
| POST  | `/orders/domains/renew`   | Продлить домены                        | Аутентификация |

### Получение корзины

**Запрос:**
```http
GET /orders/cart/me
Authorization: Bearer <jwt-token>
```

**Ответ:**
```json
{
  "totalMonthlyPrice": 600,
  "totalYearlyPrice": 5040,
  "l3Domains": ["iva.example.com", "ivan.example.com", "test.example.com"]
}
```

### Добавление домена в корзину

**Запрос:**
```http
POST /orders/cart/iva.example.com
Authorization: Bearer <jwt-token>
```

**Ответ:** `201 Created`

### Оформление заказа

**Запрос:**
```http
POST /orders/cart/checkout
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "period": "MONTH"
}
```

**Ответ:**
```json
{
  "paymentId": "550e8400-e29b-41d4-a716-446655440000",
  "paymentUrl": "https://payment.example.com/pay/550e8400-e29b-41d4-a716-446655440000",
  "amount": 60000,
  "currency": "RUB",
  "status": "PENDING"
}
```

### Продление доменов

**Запрос:**
```http
POST /orders/domains/renew
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "l3Domains": ["iva.example.com"],
  "period": "YEAR"
}
```

**Ответ:** `["iva.example.com"]`

## Диаграммы

### Sequence Diagram — Оформление заказа

```mermaid
sequenceDiagram
    participant U as Пользователь
    participant GW as API Gateway
    participant Order as order-service
    participant Security as JwtAuthenticationFilter
    participant DB as PostgreSQL
    participant Payment as payment-service
    participant Audit as audit-service

    U->>GW: POST /api/orders/cart/checkout<br/>Authorization: Bearer JWT
    GW->>Order: POST /orders/cart/checkout

    Order->>Security: Проверить JWT
    Security->>Security: Проверить подпись
    Security-->>Order: userId

    Order->>DB: Получить корзину
    DB-->>Order: Список доменов в корзине

    alt Корзина пуста
        Order-->>GW: 400 Bad Request
        GW-->>U: 400 Bad Request
    end

    Order->>Order: Рассчитать стоимость:<br/>кол-во * цена * множитель

    Order->>Payment: Создать платёж<br/>Bearer JWT
    Note over Order,Payment: l3Domains, period, amount (копейках), currency
    Payment-->>Order: {paymentId, paymentUrl, amount, currency, status}

    Payment-->>U: Платёжная ссылка

    Order->>DB: Очистить корзину
    DB-->>Order: Корзина очищена

    Order->>Audit: Лог: Платёж инициирован

    Order-->>GW: 200 OK<br/>{paymentId, paymentUrl, amount, currency, status}
    GW-->>U: 200 OK<br/>PaymentLinkResponse
```

### Sequence Diagram — Добавление домена в корзину

```mermaid
sequenceDiagram
    participant U as Пользователь
    participant GW as API Gateway
    participant Order as order-service
    participant Security as JwtAuthenticationFilter
    participant DB as PostgreSQL

    U->>GW: POST /api/orders/cart/{l3Domain}<br/>Authorization: Bearer JWT
    GW->>Order: POST /orders/cart/{l3Domain}

    Order->>Security: Проверить JWT
    Security->>Security: Проверить подпись
    Security-->>Order: userId

    Order->>Order: Валидировать l3Domain

    Order->>DB: Проверить: домен в корзине?

    alt Домен уже в корзине
        Order-->>GW: 201 Created (без изменений)
    else Домен не в корзине
        Order->>DB: Добавить домен
        DB-->>Order: Домен добавлен
        Order-->>GW: 201 Created
    end

    GW-->>U: 201 Created
```

### Sequence Diagram — Продление доменов

```mermaid
sequenceDiagram
    participant U as Пользователь
    participant GW as API Gateway
    participant Order as order-service
    participant Security as JwtAuthenticationFilter
    participant Domain as domain-service
    participant Audit as audit-service

    U->>GW: POST /api/orders/domains/renew<br/>Authorization: Bearer JWT
    GW->>Order: POST /orders/domains/renew

    Order->>Security: Проверить JWT
    Security->>Security: Проверить подпись
    Security-->>Order: userId

    Order->>Domain: Продлить домены<br/>Bearer JWT
    Note over Order,Domain: l3Domains, period
    Domain-->>Order: Продлённые домены

    Order->>Audit: Лог: Запрос продления

    Order-->>GW: 200 OK<br/>Продлённые домены
    GW-->>U: 200 OK<br/>["iva.example.com"]
```

### BPMN Diagram — Процесс оформления заказа

```mermaid
flowchart TB
    subgraph UserLane["         Пользователь"]
        Start([Начало])
        Return401([401 Unauthorized])
        Return400([400 Bad Request<br/>Корзина пуста])
        Return500([500 Internal Server Error])
        Return200([200 OK<br/>PaymentLinkResponse])
        End([Конец])
    end

    subgraph OrderLane["         order-service"]
        CheckAuth[Проверить JWT]
        Validate[Проверить подпись JWT]
        GetUserId[Извлечь userId из токена]
        FetchCart[Получить корзину из БД]
        CartEmpty{Корзина<br/>пуста?}
        CalcPrice[Рассчитать стоимость:<br/>кол-во * цена * множитель]
        ClearCart[Очистить корзину в БД]
        LogAudit[Записать в аудит]
    end

    subgraph PaymentLane["         payment-service"]
        CreatePayment[Создать платёж<br/>через payment-service]
        PaymentSuccess{Платёж<br/>создан?}
    end

    Start --> CheckAuth

    CheckAuth --> HasToken{Есть токен?}
    HasToken -- Нет --> Return401
    HasToken -- Да --> Validate

    Validate --> IsValid{Валидный?}
    IsValid -- Нет --> Return401
    IsValid -- Да --> GetUserId

    GetUserId --> FetchCart

    FetchCart --> CartEmpty
    CartEmpty -- Да --> Return400
    CartEmpty -- Нет --> CalcPrice

    CalcPrice --> CreatePayment

    CreatePayment --> PaymentSuccess
    PaymentSuccess -- Нет --> Return500
    PaymentSuccess -- Да --> ClearCart

    ClearCart --> LogAudit

    LogAudit --> Return200

    Start -.-> Return401
    Return401 -.-> End
    Return400 -.-> End
    Return500 -.-> End
    Return200 -.-> End

    style Start fill:#e1f5e1
    style End fill:#fce1e1
    style Return200 fill:#e1f5e1
    style Return401 fill:#ffe1e1
    style Return400 fill:#fff4e1
    style Return500 fill:#ffcccc
```

### BPMN Diagram — Добавление домена в корзину

```mermaid
flowchart TB
    subgraph UserLane["         Пользователь"]
        Start([Начало])
        Return401([401 Unauthorized])
        Return400([400 Bad Request])
        Return201([201 Created])
        Return201_2([201 Created])
        End([Конец])
    end

    subgraph OrderLane["         order-service"]
        CheckAuth[Проверить JWT]
        Validate[Проверить подпись JWT]
        GetUserId[Извлечь userId]
        ValidateDomain[Проверить l3Domain:<br/>не пустой]
        DomainValid{Валидный<br/>домен?}
        CheckExists[Проверить: домен<br/>в корзине?]
        AlreadyExists{Уже<br/>в корзине?}
        AddToCart[Добавить домен в БД]
    end

    Start --> CheckAuth

    CheckAuth --> HasToken{Есть токен?}
    HasToken -- Нет --> Return401
    HasToken -- Да --> Validate

    Validate --> IsValid{Валидный?}
    IsValid -- Нет --> Return401
    IsValid -- Да --> GetUserId

    GetUserId --> ValidateDomain

    ValidateDomain --> DomainValid
    DomainValid -- Нет --> Return400
    DomainValid -- Да --> CheckExists

    CheckExists --> AlreadyExists
    AlreadyExists -- Да --> Return201
    AlreadyExists -- Нет --> AddToCart

    AddToCart --> Return201_2

    Start -.-> Return401
    Return401 -.-> End
    Return400 -.-> End
    Return201 -.-> End
    Return201_2 -.-> End

    style Start fill:#e1f5e1
    style End fill:#fce1e1
    style Return201 fill:#e1f5e1
    style Return401 fill:#ffe1e1
    style Return400 fill:#fff4e1
```

### BPMN Diagram — Поток запроса через API Gateway

```mermaid
flowchart LR
    subgraph FrontendLane["         Frontend"]
        Client[React App]
    end

    subgraph GatewayLane["         API Gateway"]
        Router[Маршрутизация<br/>StripPrefix 1]
    end

    subgraph OrderLane["         order-service"]
        Filter[JwtAuthenticationFilter]
        CartCtrl[CartApiController]
        DomainCtrl[DomainApiController]
        Service[CartServiceImpl]
        Repo[CartRepository]
        DomC[DomainClient]
        PayC[PaymentClient]
    end

    subgraph DBLane["         PostgreSQL"]
        Cart[Таблица cart]
    end

    subgraph ServicesLane["         Внешние сервисы"]
        PayS["payment-service"]
        AuditS["audit-service"]
        DomainS["domain-service"]
    end

    Client -->|POST /api/orders/cart/checkout| Router
    Router -->|POST /orders/cart/checkout| Filter
    Filter --> CartCtrl
    CartCtrl --> Service
    Service --> Repo
    Repo --> Cart
    Service --> PayC
    PayC --> PayS
    Service -->|"AuditClient"| AuditS

    Client -.->|POST /api/orders/domains/renew| Router
    Router -.->|POST /orders/domains/renew| Filter
    Filter -.-> DomainCtrl
    DomainCtrl -.-> DomC
    DomC -.-> DomainS

    style GatewayLane fill:#e3f2fd
    style OrderLane fill:#4caf50
    style DBLane fill:#607d8b
    style ServicesLane fill:#ff9800
```

### BPMN Diagram — Расчёт стоимости заказа

```mermaid
flowchart TB
    subgraph OrderLane["         order-service"]
        Start([Получены данные корзины])
        CountDomain[Подсчитать домены]
        monthlyPrice[Читать конфигурацию:<br/>DOMAIN_MONTHLY_PRICE]
        yearlyDiscount[Читать конфигурацию:<br/>DOMAIN_YEARLY_DISCOUNT]
        GetPeriod[Получить период из запроса]
        CreateRequest[Создать запрос на платёж:<br/>l3Domains, period, amount, RUB]
    end

    subgraph CalcLane["         Расчёт"]
        CheckPeriod{Период?}
        CalcMonthly[amount  count * monthlyPrice * 100]
        CalcYearlyStep1[yearlyFull  count * monthlyPrice * 12]
        CalcYearly[amount  yearlyFull * yearlyDiscount * 100]
    end

    subgraph PaymentLane["         payment-service"]
        PaymentClient[Вызвать paymentClient.createPayment]
    end

    subgraph UserLane["         Пользователь"]
        End1([Отправить запрос в payment-service])
    end

    Start --> CountDomain
    CountDomain --> monthlyPrice
    monthlyPrice --> yearlyDiscount
    yearlyDiscount --> GetPeriod

    GetPeriod --> CheckPeriod

    CheckPeriod -- MONTH --> CalcMonthly
    CheckPeriod -- YEAR --> CalcYearlyStep1
    CalcYearlyStep1 --> CalcYearly

    CalcMonthly --> CreateRequest
    CalcYearly --> CreateRequest

    CreateRequest --> PaymentClient
    PaymentClient --> End1


    style Start fill:#e1f5e1
    style End1 fill:#e1f5e1
    style CalcMonthly fill:#fff3e0
    style CalcYearly fill:#fff3e0
    style CreateRequest fill:#e8f5e9
```

## Зависимости между сервисами

```mermaid
graph LR
    Order[order-service]
    Payment[payment-service]
    Domain[domain-service]
    Audit[audit-service]
    DB[(PostgreSQL)]

    Order -->|HTTP| Payment
    Order -->|HTTP| Domain
    Order -->|HTTP| Audit
    Order -->|JPA / Liquibase| DB

    style Order fill:#4caf50,color:#fff
    style Domain fill:#2196f3,color:#fff
    style Payment fill:#ff9800,color:#fff
    style Audit fill:#9c27b0,color:#fff
    style DB fill:#607d8b,color:#fff
```

## Объёмная модель данных

```mermaid
erDiagram
    CART {
        UUID user_id PK
        string l3_domain PK
    }
```

**Таблица `cart`:**
- `user_id` — UUID пользователя (часть составного первичного ключа)
- `l3_domain` — имя L3-домена (часть составного первичного ключа, VARCHAR(255))

## Логирование

При выполнении операций сервис отправляет события аудита в audit-service:
- `"Payment initiated: {paymentId} (period${DB_USER:***REMOVED***}{period})"` — при инициации платежа
- `"Domain renewal requested: {count} domains (period${DB_USER:***REMOVED***}{period})"` — при запросе продления доменов

## Расчёт стоимости

### Параметры
- **DOMAIN_MONTHLY_PRICE** — базовая цена домена за месяц (по умолчанию: 200 руб.)
- **DOMAIN_YEARLY_DISCOUNT** — коэффициент скидки за год (по умолчанию: 0.7)

### Формулы
- **Месячная цена (несколько доменов):** `count * DOMAIN_MONTHLY_PRICE`
- **Годовая цена (один домен на год):** `(DOMAIN_MONTHLY_PRICE * 12) * DOMAIN_YEARLY_DISCOUNT`
- **Годовая цена (несколько доменов на год):** `(count * DOMAIN_MONTHLY_PRICE * 12) * DOMAIN_YEARLY_DISCOUNT`

### Примеры
- 1 домен на месяц: 200 руб.
- 3 домена на месяц: 600 руб.
- 1 домен на год: 200 * 12 * 0.7 ${DB_USER:***REMOVED***} 1680 руб.
- 3 домена на год: 600 * 12 * 0.7 ${DB_USER:***REMOVED***} 5040 руб.

## Security

- JWT токены проверяются через `JwtAuthenticationFilter`
- userId извлекается из токена и используется как principal
- Все эндпоинты требуют аутентификации (кроме `/actuator/**`, `/v3/api-docs/**`, `/swagger-ui/**`)
- JWT токен передаётся при интеграции с payment-service и domain-service

## Мониторинг

Actuator эндпоинты:
- `/actuator/health` — состояние сервиса
- `/actuator/info` — информация о сервисе
- `/actuator/metrics` — метрики приложения

## Swagger UI

Документация API доступна по адресу: `http://localhost:8084/swagger-ui.html`

## Миграции базы данных

Liquibase changelog находится в `src/main/resources/db/changelog/`:
- `001-initial.sql` — создание таблицы `cart`

Миграции применяются автоматически при запуске приложения.

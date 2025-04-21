# 🅿️ Система бронирования парковочных мест

<div align="center">

[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)](https://spring.io/)
[![JavaFX](https://img.shields.io/badge/JavaFX-4287f5?style=for-the-badge&logo=java&logoColor=white)](https://openjfx.io/)
[![MySQL](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)](https://maven.apache.org/)
[![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=white)](https://jwt.io/)

<img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" title="Java" width="40" height="40"/>&nbsp;
<img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" title="Spring" width="40" height="40"/>&nbsp;
<img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/mysql/mysql-original.svg" title="MySQL" width="40" height="40"/>&nbsp;
<img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/css3/css3-original.svg" title="CSS3" width="40" height="40"/>&nbsp;
<img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/git/git-original.svg" title="Git" width="40" height="40"/>

📚 Курсовая работа по дисциплине «Современные технологии программирования»  
‍🎓 Выполнил студент 3-его курса группы ПИ22-1 Косарев Григорий

</div>

---

<div align="center">

### 🚀 Быстрый старт

[![Клонировать](https://img.shields.io/badge/Клонировать-181717?style=for-the-badge&logo=github&logoColor=white)](#-установка)
[![Документация](https://img.shields.io/badge/Документация-147EFB?style=for-the-badge&logo=gitbook&logoColor=white)](#-о-проекте)
[![Запуск](https://img.shields.io/badge/Запуск-00ED64?style=for-the-badge&logo=readme&logoColor=white)](#-установка)

</div>

## 📝 Содержание

<div align="center">

[![О проекте](https://img.shields.io/badge/О%20проекте-FF4785?style=for-the-badge&logo=about.me&logoColor=white)](#-о-проекте)
[![Архитектура](https://img.shields.io/badge/Архитектура-1BA0D7?style=for-the-badge&logo=azure-artifacts&logoColor=white)](#-архитектура)
[![Технологии](https://img.shields.io/badge/Технологии-00C7B7?style=for-the-badge&logo=stack-overflow&logoColor=white)](#-технологический-стек)
[![Установка](https://img.shields.io/badge/Установка-4EAA25?style=for-the-badge&logo=gnubash&logoColor=white)](#-установка)

</div>

---

## 🎯 О проекте

<div align="center">

### 🌟 Современное решение для управления парковкой

</div>

Информационная система для бронирования парковочных мест в торговых центрах, реализованная как современное клиент-серверное приложение с удобным графическим интерфейсом.

### ✨ Основные возможности

<div align="center">

| 🔐 **Безопасность** | 🚗 **Управление парковкой** | 💼 **Администрирование** |
|:-------------------:|:---------------------------:|:-----------------------:|
| Регистрация и авторизация | Просмотр парковочных мест | Панель управления |
| JWT-аутентификация | Бронирование в реальном времени | Мониторинг транзакций |
| Разграничение прав | Управление бронированиями | Управление пользователями |

</div>

---

## 🏗 Архитектура

<div align="center">

### 🎨 Современная многоуровневая архитектура

</div>

### 📊 Общая структура
```mermaid
graph TD
    A[Клиент-серверное приложение] --> B[Frontend - JavaFX Client]
    A --> C[Backend - Spring Boot Server]
    
    B --> D[Пользовательский интерфейс]
    B --> E[Клиентская логика]
    B --> F[Сервисы взаимодействия с сервером]
    
    C --> G[REST API]
    C --> H[Бизнес-логика]
    C --> I[База данных]
    
    D --> J[Основной интерфейс]
    D --> K[Админ-панель]
    
    G --> L[Контроллеры]
    H --> M[Сервисы]
    I --> N[Репозитории]
```

### 🖥 Frontend (course_client)
```mermaid
graph TD
    A[Frontend - JavaFX] --> B[View - FXML]
    A --> C[Controller]
    A --> D[Model - DTO]
    
    B --> E[MainView.fxml]
    B --> F[AdminPanel.fxml]
    
    C --> G[MainController]
    C --> H[AdminController]
    
    D --> I[UserDTO]
    D --> J[BookingDTO]
    D --> K[TransactionDTO]
    D --> L[ParkingSpotDTO]
    
    M[ServerService] --> G
    M --> H
```

### ⚙️ Backend (course_server)
```mermaid
graph TD
    A[Backend - Spring Boot] --> B[Controllers]
    A --> C[Services]
    A --> D[Repositories]
    A --> E[Entities]
    
    B --> F[UserController]
    B --> G[BookingController]
    B --> H[TransactionController]
    B --> I[ParkingSpotController]
    
    E --> J[User]
    E --> K[Booking]
    E --> L[Transaction]
    E --> M[ParkingSpot]
    
    D --> N[JPA Repositories]
```

### 🔄 Процессы системы

```mermaid
sequenceDiagram
    participant User
    participant Frontend
    participant Backend
    participant Database
    
    User->>Frontend: Выбор парковки
    Frontend->>Backend: GET /parking-spots
    Backend->>Database: Запрос свободных мест
    Database-->>Backend: Список мест
    Backend-->>Frontend: JSON ответ
    Frontend->>User: Отображение мест
    
    User->>Frontend: Бронирование места
    Frontend->>Backend: POST /bookings
    Backend->>Database: Создание брони
    Database-->>Backend: Подтверждение
    Backend-->>Frontend: Статус бронирования
    Frontend->>User: Результат операции
```

```mermaid
sequenceDiagram
    participant User
    participant Frontend
    participant Backend
    participant Database
    
    User->>Frontend: Ввод логина/пароля
    Frontend->>Backend: POST /auth/login
    Backend->>Database: Проверка пользователя
    Database-->>Backend: Данные пользователя
    Backend->>Backend: Генерация JWT
    Backend-->>Frontend: JWT токен
    Frontend->>Frontend: Сохранение токена
    Frontend->>User: Доступ к системе
```

---

## 🛠 Технологический стек

<div align="center">

### 💻 Frontend (course_client)

| Технология | Версия | Описание | Иконка |
|------------|---------|----------|---------|
| Java | 23 | Основной язык программирования | <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" width="20"/> |
| JavaFX | 17.0.6 | Создание графического интерфейса | <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" width="20"/> |
| FXML | - | Декларативное описание UI | 📄 |
| CSS | - | Стилизация интерфейса | <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/css3/css3-original.svg" width="20"/> |
| Maven | - | Система сборки проекта | <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/maven/maven-original.svg" width="20"/> |
| Jackson | 2.18.2 | Работа с JSON | 🔄 |
| JUnit | 5.10.2 | Модульное тестирование | ✅ |

### ⚙️ Backend (course_server)

| Технология | Версия | Описание | Иконка |
|------------|---------|----------|---------|
| Java | 23 | Основной язык программирования | <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" width="20"/> |
| Spring Boot | 3.3.5 | Основной фреймворк | <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" width="20"/> |
| Spring Security | - | Безопасность и аутентификация | 🔒 |
| Spring Data JPA | - | Работа с базой данных | 💾 |
| MySQL | Latest | СУБД | <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/mysql/mysql-original.svg" width="20"/> |
| Maven | - | Система сборки | <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/maven/maven-original.svg" width="20"/> |
| Lombok | - | Уменьшение шаблонного кода | 🔧 |
| JWT | 0.11.5 | Аутентификация и авторизация | 🎫 |

</div>

---

## 📦 Установка

<div align="center">

### ⚡ Быстрый старт

[![JDK](https://img.shields.io/badge/JDK%2023-007396?style=for-the-badge&logo=java&logoColor=white)](https://www.oracle.com/java/technologies/downloads/)
[![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)](https://maven.apache.org/)
[![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white)](https://git-scm.com/)

</div>

### 🚀 Шаги установки


<summary>1️⃣ Клонирование репозитория</summary>

```bash
git clone <repository-url>
cd course_work-main
```



<summary>2️⃣ Настройка базы данных</summary>

```properties
# course_server/src/main/resources/application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/course_db
spring.datasource.username=root
spring.datasource.password=root
```



<summary>3️⃣ Запуск сервера</summary>

```bash
cd course_server
mvn spring-boot:run
```



<summary>4️⃣ Запуск клиента</summary>

```bash
cd course_client
mvn javafx:run
```


---

## 👤 Автор

<div align="center">

**Косарев Григорий**

- 👨‍🎓 Группа: ПИ22-1
- 📚 Курс: 3
- 🏛 Университет: Финансовый университет при Правительстве РФ

</div>

---
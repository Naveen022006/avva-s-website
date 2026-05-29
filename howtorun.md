# 🍛 How to Run — Avva's Home Foods

This guide covers two ways to run the project:

- **Option A — Docker** *(Recommended, easiest)*
- **Option B — Manual** *(Run MongoDB + Spring Boot separately)*

---

## 📋 Prerequisites

### Common (Both Options)
| Tool | Version | Download |
|------|---------|----------|
| Git | Any | https://git-scm.com |

### Option A — Docker
| Tool | Version | Download |
|------|---------|----------|
| Docker Desktop | Latest | https://www.docker.com/products/docker-desktop |

### Option B — Manual
| Tool | Version | Download |
|------|---------|----------|
| Java JDK | 17+ | https://adoptium.net |
| MongoDB | 6+ | https://www.mongodb.com/try/download/community |

> Maven is **not required** — the project includes `mvnw.cmd` (Windows) and `mvnw` (Linux).

---

## 🔑 Environment Setup (Required for Both Options)

The app needs a Google OAuth Client ID to enable Google Sign-In.

### Step 1 — Create the `.env` file

Navigate to the `backend/` folder and create a `.env` file:

**Windows (Command Prompt):**
```cmd
cd backend
copy NUL .env
```

**Linux / macOS:**
```bash
cd backend
touch .env
```

### Step 2 — Add credentials to `backend/.env`

Open the file in any text editor and paste the following:

```env
GOOGLE_CLIENT_ID=your_google_client_id_here
GOOGLE_CLIENT_SECRET=your_google_client_secret_here
MONGODB_URI=mongodb://localhost:27017/avvahomefoods
```

> ℹ️ To get a Google Client ID, visit [Google Cloud Console](https://console.cloud.google.com/), create a project, and enable the **Google Identity** API.

---

## 🐳 Option A — Run with Docker (Recommended)

This starts the **Spring Boot app + MongoDB** together automatically.

### Windows

```cmd
REM Step 1: Clone the repository (skip if already done)
git clone https://github.com/Naveen022006/avva-s-website.git
cd avva-s-website

REM Step 2: Set your Google Client ID (replace with your actual ID)
set GOOGLE_CLIENT_ID=your_google_client_id_here

REM Step 3: Build and start all services
docker-compose up --build
```

### Linux / macOS

```bash
# Step 1: Clone the repository (skip if already done)
git clone https://github.com/Naveen022006/avva-s-website.git
cd avva-s-website

# Step 2: Set your Google Client ID (replace with your actual ID)
export GOOGLE_CLIENT_ID=your_google_client_id_here

# Step 3: Build and start all services
docker-compose up --build
```

### ✅ Access the App

Once you see `Started AvvaHomeFoodsApplication`, open your browser:

```
http://localhost:8080
```

### 🛑 Stop Docker

```bash
docker-compose down
```

To also delete the database volume:
```bash
docker-compose down -v
```

---

## 🔧 Option B — Run Manually (Without Docker)

### Step 1 — Start MongoDB

**Windows:**
1. Install MongoDB from https://www.mongodb.com/try/download/community
2. Open **Services** (`Win + R` → `services.msc`) and start **MongoDB Server**
   — OR run manually:
```cmd
"C:\Program Files\MongoDB\Server\6.0\bin\mongod.exe" --dbpath C:\data\db
```

**Linux:**
```bash
sudo systemctl start mongod
# Verify it's running:
sudo systemctl status mongod
```

---

### Step 2 — Set Environment Variables

**Windows (Command Prompt — must stay open):**
```cmd
set GOOGLE_CLIENT_ID=your_google_client_id_here
```

**Linux / macOS:**
```bash
export GOOGLE_CLIENT_ID=your_google_client_id_here
```

---

### Step 3 — Build & Run the Backend

Open a terminal in the **project root** (`food/` folder).

**Windows:**
```cmd
cd backend
mvnw.cmd clean package -DskipTests
mvnw.cmd spring-boot:run
```

**Linux / macOS:**
```bash
cd backend
chmod +x mvnw
./mvnw clean package -DskipTests
./mvnw spring-boot:run
```

> First run will download Maven dependencies (~2–5 minutes depending on internet speed).

---

### Step 4 — Access the App

Once you see:
```
Started AvvaHomeFoodsApplication in X seconds
```

Open your browser:
```
http://localhost:8080
```

---

## 🔐 Default Login Credentials

The app **auto-creates** an admin account on first startup:

| Role  | Username | Password |
|-------|----------|----------|
| Admin | `admin1` | `admin123` |

> ⚠️ Change this password after first login in production.

---

## 📦 What Happens on First Run

On startup, the app automatically:
1. **Seeds 12 products** (masala powders) into MongoDB
2. **Creates the admin user** (`admin1` / `admin123`)

No manual database setup is required.

---

## 🌐 Available Pages

| URL | Page |
|-----|------|
| `http://localhost:8080/` | Home |
| `http://localhost:8080/products.html` | Products |
| `http://localhost:8080/login.html` | Login |
| `http://localhost:8080/signup.html` | Sign Up |
| `http://localhost:8080/admin.html` | Admin Dashboard |
| `http://localhost:8080/my-orders.html` | My Orders |

---

## 🔌 API Endpoints (Quick Reference)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register user |
| POST | `/api/auth/login` | Login |
| POST | `/api/auth/google-login` | Google OAuth login |
| GET | `/api/products` | List all products |
| GET | `/api/categories` | List categories |
| POST | `/api/orders` | Place an order |

---

## 🛠️ Troubleshooting

### ❌ Port 8080 already in use

**Windows:**
```cmd
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

**Linux:**
```bash
lsof -i :8080
kill -9 <PID>
```

---

### ❌ MongoDB connection refused

Make sure MongoDB is running:

**Windows:** Check `services.msc` → MongoDB Server → Start

**Linux:**
```bash
sudo systemctl start mongod
```

---

### ❌ `GOOGLE_CLIENT_ID` error on startup

Ensure the environment variable is set **before** running the app. The app will fail to start without it.

---

### ❌ Docker build fails

Make sure Docker Desktop is running, then:
```bash
docker-compose down -v
docker-compose up --build
```

---

## 📁 Project Structure (Quick Reference)

```
food/
├── backend/          → Spring Boot API (Java 17, Maven)
├── frontend/         → HTML/CSS/JS pages (served by Spring Boot)
├── button/           → Standalone UI component
├── Dockerfile        → Multi-stage Docker build
├── docker-compose.yml→ App + MongoDB services
└── howtorun.md       → This file
```

# Avva's Home Foods - Setup & Deployment Guide

## 🌐 Cloud Deployment (Render) - RECOMMENDED

**For production deployment, use Render Cloud Platform:**

→ **[Complete Render Deployment Guide](./RENDER_DEPLOYMENT.md)**

Quick steps:
1. Push code to GitHub
2. Create account at [render.com](https://render.com)
3. Set environment variables:
   - `SPRING_DATA_MONGODB_URI` (MongoDB Atlas URI)
   - `GOOGLE_CLIENT_ID` (Google OAuth)
4. Deploy from GitHub - Render auto-detects `render.yaml`

Deploy Status: ✅ **Ready for Production**

---

## 🔐 Configuration & Security

The project requires environment variables for secure features like Google OAuth. 

### 1. Set up the `.env` file
1. Navigate to the `backend/` directory.
2. Create/Edit a file named `.env` and add:
   ```env
   GOOGLE_CLIENT_ID=your-google-client-id
   GOOGLE_CLIENT_SECRET=your-google-client-secret
   MONGODB_URI=mongodb://localhost:27017/avva_home_foods
   ```
> [!IMPORTANT]
> The `.env` file contains secrets and is ignored by Git. Never share this file.

---

## 🚀 Quick Start (Windows VS Code)

### 1. Run Backend server (Terminal 1)
Open a new terminal in VS Code and run:
```powershell
cd backend
$env:GOOGLE_CLIENT_ID="your-id"; .\mvnw.cmd spring-boot:run
```
*Note: If using Command Prompt, use `set GOOGLE_CLIENT_ID=your-id` before running.*

### 2. Run Frontend website (Terminal 2)
```powershell
cd frontend
python -m http.server 8000
```
Then visit: `http://localhost:8000`

---

## 🐧 Linux Server Deployment

### 1. Prerequisites
- **JDK 17+**: `sudo apt install openjdk-17-jdk`
- **MongoDB**: Ensure it's running on port 27017.
- **Python 3**: For serving the frontend.

### 2. Run Backend in Background
```bash
cd backend
chmod +x mvnw
export GOOGLE_CLIENT_ID="your-id"
nohup ./mvnw spring-boot:run > backend.log 2>&1 &
```

### 3. Run Frontend in Background
```bash
cd frontend
nohup python3 -m http.server 8000 > frontend.log 2>&1 &
```

---

## Prerequisites
- Java JDK 17+
- MongoDB (Running on port 27017)
- VS Code Extensions: "Extension Pack for Java" and "Live Server"

## Project Structure
*   `backend/`: Spring Boot Application (API running on port 8080)
*   `frontend/`: Static Web Files (HTML/CSS/JS)

## Troubleshooting
*   **MongoDB Connection Error**: Ensure your local MongoDB service is running.
*   **Port 8080 in use**: If the backend fails to start, check if another application is using port 8080.
*   **Missing ID**: If Google Sign-in doesn't load, verify your `GOOGLE_CLIENT_ID` is set in the environment or `.env` file.

---

## 🐳 Docker Deployment

You can run the entire application (Backend + Frontend + Database) using Docker.

### 1. Prerequisites
- Git installed.
- Docker and Docker Compose installed.

### 2. Clone the Repository
Open a terminal and run:
```bash
git clone https://github.com/Naveen022006/avva-s-website.git
cd avva-s-website
```

### 3. Configuration
Create a `.env` file in the root directory (same level as `docker-compose.yml`) with your Google Client ID:
```env
GOOGLE_CLIENT_ID=your_google_client_id_here
```

### 4. Build and Run
Open a terminal in the project root and run:
```bash
docker-compose up --build
```

- The application will be available at: `http://localhost:8080`
- The backend API and frontend are both served on this port.
- MongoDB will be running in a separate container on port `27017`.

### 5. Stop the Application
Press `Ctrl+C` to stop the logs, then run:
```bash
docker-compose down
```
For a complete cleanup (removing volumes):
```bash
docker-compose down -v
```

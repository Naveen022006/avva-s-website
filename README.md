# Avva's Home Foods - Windows Setup Guide

## 🚀 Quick Start (Windows VS Code)

### 1. Run Backend server (Terminal 1)
Open a new terminal (`Ctrl + ~`), navigate to `backend`, and run:
```powershell
cd backend
.\mvnw.cmd spring-boot:run
```
*Wait until you see "Started AvvaHomeFoodsApplication" in the logs.*

### 2. Run Frontend website (Terminal 2 or Live Server)
**Option A (Recommended - Live Server Extension):**
1. Right-click `frontend/index.html` in the file explorer.
2. Select **"Open with Live Server"**.

**Option B (Simple Open):**
Open a new terminal and run:
```powershell
start frontend/index.html
```

---

## Prerequisites
- Java JDK 17+
- MongoDB (Running on port 27017)
- VS Code Extensions: "Extension Pack for Java" and "Live Server"

## Project Structure
*   `backend/`: Spring Boot Application (API running on port 8080)
*   `frontend/`: Static Web Files (HTML/CSS/JS)

## Running the Backend (Spring Boot)

1.  Open the `backend` folder in VS Code or a terminal.
2.  Navigate to the `backend` directory:
    ```bash
    cd backend
    ```
3.  Run the application using the Maven wrapper:
    *   **Windows (Command Prompt / PowerShell)**:
        ```powershell
        .\mvnw.cmd spring-boot:run
        ```
    *   **Mac / Linux**:
        ```bash
        ./mvnw spring-boot:run
        ```
4.  Wait for the application to start. You should see logs indicating Tomact started on port `8080`.
    *   API Base URL: `http://localhost:8080`

**Note:** If you need to change the database configuration, edit `backend/src/main/resources/application.properties`.

## Running the Frontend

1.  Open the `frontend` folder in VS Code.
2.  Locate the main entry file, typically **`index.html`** or **`login.html`**.
3.  Right-click on the file and select **"Open with Live Server"**.
4.  This will open the website in your default browser (usually at `http://127.0.0.1:5500/frontend/index.html`).

## Alternative: Running Frontend from Terminal

If you prefer using the terminal to open the website:

*   **Windows**:
    ```powershell
    start frontend/index.html
    ```
*   **Mac**:
    ```bash
    open frontend/index.html
    ```
*   **Linux**:
    ```bash
    xdg-open frontend/index.html
    ```

## Important URLs

*   **Home Page**: `index.html`
*   **User Login**: `login.html` (or `user_login.html`)
*   **Admin Dashboard**: `admin.html`

## Troubleshooting

*   **MongoDB Connection Error**: Ensure your local MongoDB service is running.
*   **Port 8080 in use**: If the backend fails to start, check if another application is using port 8080. You can change the port in `application.properties` (e.g., `server.port=8081`).
*   **CORS Issues**: If the frontend cannot communicate with the backend, ensure `@CrossOrigin` is enabled on your backend controllers.

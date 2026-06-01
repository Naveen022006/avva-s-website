# Avva's Home Foods

> A full-stack e-commerce web application for ordering traditional homemade masalas and pickles. Built with a Spring Boot REST API, MongoDB Atlas, Cloudinary image CDN, and a plain HTML/CSS/JS frontend — deployed on Render.

---

## ✨ Features

### Storefront (Customer Side)
- **Home page** — Hero banner, featured products, category showcase with animated card entrances
- **Products page** — Full catalogue with category filter tabs (animated transitions), sort by price/rating, skeleton loader while fetching
- **Product detail page** — Image gallery, weight/price variant chips, ingredient accordion, customer reviews with star ratings, related products
- **Cart & Checkout** — Persistent cart (localStorage), dynamic delivery charge calculation (free delivery above a configurable threshold), COD / online payment selection
- **Order tracking** — *My Orders* page with full order history, subtotal + delivery charge breakdown per order
- **User auth** — Sign up / Login with email + password (stored in MongoDB with BCrypt hashing); Google Sign-In support

### Admin Dashboard (`/frontend/admin.html`)
- **Stats panel** — Live cards showing total revenue, total orders, pending orders count (highlighted in amber), and out-of-stock product count
- **Manage Products** — Full CRUD with Cloudinary image upload, weight/price variant pricing, quick in-stock toggle button on every row (no modal needed)
- **Monitor Orders** — Orders table with customer name, phone, items, total, and per-row status dropdown; inline delivery charge editor; view order details modal; delete order
- **Order search & filter** — Real-time search by customer name, phone, or Order ID; status dropdown filter; live result count
- **Auto-refresh** — Orders table refreshes every 60 seconds when the Monitor Orders tab is active; a pulsing red badge on the tab shows the count of PENDING orders
- **Manage Categories** — Add / delete product categories; dropdowns on the product form update automatically
- **Store Settings** — Configure global delivery charge and free-delivery threshold (persisted to MongoDB)

---

## 🏗️ Tech Stack

| Layer | Technology |
|---|---|
| Backend | Spring Boot 3.2.2, Java 17 |
| Database | MongoDB Atlas (cloud) |
| Image CDN | Cloudinary |
| Auth | Custom token filter (`SimpleAuthFilter`) + BCrypt |
| Frontend | Plain HTML5, CSS3, Vanilla JavaScript |
| Deployment | Render (Docker) |
| CI/CD | GitHub → Render auto-deploy on push |

---

## 📁 Project Structure

```
avva-s-website/
├── backend/                        # Spring Boot API
│   ├── src/main/java/com/avvahomefoods/
│   │   ├── controller/             # REST endpoints
│   │   │   ├── AuthController.java
│   │   │   ├── ProductController.java
│   │   │   ├── OrderController.java
│   │   │   ├── CategoryController.java
│   │   │   └── ReviewController.java
│   │   ├── model/                  # MongoDB documents
│   │   │   ├── Product.java
│   │   │   ├── Order.java
│   │   │   ├── Category.java
│   │   │   ├── Review.java
│   │   │   └── User.java
│   │   ├── repository/             # Spring Data MongoDB repos
│   │   └── config/
│   │       ├── SecurityConfig.java # CORS + route protection
│   │       ├── SimpleAuthFilter.java # Token-based auth
│   │       ├── WebConfig.java
│   │       ├── AdminSeeder.java    # Seeds default admin on startup
│   │       └── DataSeeder.java
│   └── src/main/resources/
│       └── application.properties
├── frontend/                       # Static HTML/CSS/JS
│   ├── index.html                  # Home page
│   ├── products.html               # Product catalogue
│   ├── product-details.html        # Single product view
│   ├── order.html                  # Checkout
│   ├── my-orders.html              # Order history
│   ├── admin.html                  # Admin dashboard
│   ├── login.html / signup.html    # Auth pages
│   ├── css/style.css
│   └── js/
│       ├── app.js                  # Core frontend logic
│       └── product-details.js
├── Dockerfile
├── docker-compose.yml
└── render.yaml                     # Render deployment config
```

---

## 🔑 API Reference

### Authentication
| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/api/auth/signup` | Public | Register a new user |
| POST | `/api/auth/login` | Public | Login, returns token |

### Products
| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/products` | Public | Get all products |
| GET | `/api/products/{id}` | Public | Get product by ID |
| GET | `/api/products/category/{cat}` | Public | Filter by category |
| GET | `/api/products/search?name=X` | Public | Search by name |
| POST | `/api/products` | Admin | Create product |
| PUT | `/api/products/{id}` | Admin | Update product |
| DELETE | `/api/products/{id}` | Admin | Delete product |
| POST | `/api/products/upload` | Admin | Upload image to Cloudinary |

### Orders
| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/api/orders` | Public | Place a new order |
| GET | `/api/orders` | Admin | Get all orders |
| GET | `/api/orders/{id}` | Admin | Get order by ID |
| GET | `/api/orders/my-orders?email=X` | Public | Get orders by email |
| PUT | `/api/orders/{id}/status?status=X` | Admin | Update order status |
| PUT | `/api/orders/{id}/delivery-charge?charge=X` | Admin | Edit delivery charge |
| DELETE | `/api/orders/{id}` | Admin | Delete order |

### Categories & Reviews
| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/categories` | Public | Get all categories |
| POST | `/api/categories` | Admin | Create category |
| DELETE | `/api/categories/{id}` | Admin | Delete category |
| GET | `/api/reviews/product/{id}` | Public | Get reviews for product |
| POST | `/api/reviews` | Public | Submit a review |

### Settings
| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/settings` | Public | Get delivery settings |
| PUT | `/api/settings` | Admin | Update delivery settings |

---

## 🔐 Auth Tokens

Authentication uses a simple token model — no JWT, no OAuth session required for the API.

| Token | Role | Used for |
|---|---|---|
| `Bearer admin-dummy-token-avva` | `ROLE_ADMIN` | All admin operations |
| `Bearer user-dummy-token-avva` | `ROLE_USER` | User-scoped operations |

Tokens are stored in `localStorage` after login and injected as `Authorization` headers on API calls.

> **Note:** Google Sign-In is supported on the frontend; a `GOOGLE_CLIENT_ID` env var is required to enable it.

---

## 🚀 Local Development

### Prerequisites
- Java JDK 17+
- Maven (or use the included `mvnw.cmd` wrapper)
- A MongoDB Atlas URI (or local MongoDB on port 27017)

### 1. Backend

```powershell
cd backend

# Windows PowerShell
$env:MONGODB_URI="mongodb://localhost:27017/avvahomefoods"
$env:CLOUDINARY_CLOUD_NAME="your-cloud-name"
$env:CLOUDINARY_API_KEY="your-api-key"
$env:CLOUDINARY_API_SECRET="your-api-secret"
.\mvnw.cmd spring-boot:run
```

API will be available at `http://localhost:8080`.

### 2. Frontend

```powershell
cd frontend
python -m http.server 8000
```

Open `http://localhost:8000` in your browser.

> The frontend auto-detects `localhost` and points `API_BASE` to `http://localhost:8080/api`.

---

## ☁️ Production Deployment (Render)

The project ships with a `render.yaml` that auto-configures a Render Web Service.

**Steps:**
1. Push the repo to GitHub.
2. Create a free account at [render.com](https://render.com).
3. Click **New → Web Service → Connect GitHub repo**.
4. Render reads `render.yaml` automatically. Set these environment variables in the Render dashboard:

| Variable | Description |
|---|---|
| `MONGODB_URI` | MongoDB Atlas connection string |
| `CLOUDINARY_CLOUD_NAME` | Cloudinary cloud name |
| `CLOUDINARY_API_KEY` | Cloudinary API key |
| `CLOUDINARY_API_SECRET` | Cloudinary API secret |
| `GOOGLE_CLIENT_ID` | Google OAuth Client ID (for Sign-In button) |

5. Click **Deploy**. Render builds the Docker image and serves the app on the assigned URL.

Health check endpoint: `/actuator/health`

---

## 🐳 Docker (Self-Hosted)

```bash
# Clone
git clone https://github.com/Naveen022006/avva-s-website.git
cd avva-s-website

# Build and run (set env vars in docker-compose.yml or a .env file)
docker-compose up --build
```

App will be available at `http://localhost:8080`.

To stop:
```bash
docker-compose down
```

---

## 🐧 Linux / VPS Deployment

```bash
cd backend
chmod +x mvnw
export MONGODB_URI="your-atlas-uri"
export CLOUDINARY_CLOUD_NAME="..."
nohup ./mvnw spring-boot:run > backend.log 2>&1 &

# Serve frontend statically
cd ../frontend
nohup python3 -m http.server 8000 > frontend.log 2>&1 &
```

---

## 🛠️ Troubleshooting

| Problem | Fix |
|---|---|
| Monitor Orders / Manage Categories tabs don't open | Clear browser cache — ensure no duplicate `let` declarations between `app.js` and inline scripts |
| Orders table shows empty / 403 | Confirm you're logged in as admin (`admin-dummy-token-avva` stored in localStorage) |
| Image upload fails | Set `CLOUDINARY_CLOUD_NAME`, `CLOUDINARY_API_KEY`, `CLOUDINARY_API_SECRET` in Render env vars |
| Backend fails to start on Render | Check `MONGODB_URI` is set; Render must bind to `PORT` env var (already configured) |
| Google Sign-In button missing | Set `GOOGLE_CLIENT_ID` env var and add the Render URL to Google OAuth authorized origins |
| Port 8080 in use locally | Kill the process or change `server.port` in `application.properties` |

---

## 📦 Default Admin Account

A default admin is seeded on first startup via `AdminSeeder.java`.

| Field | Value |
|---|---|
| Email | `admin@avva.com` |
| Password | Set via `ADMIN_PASSWORD` env var (defaults to a hardcoded value in `AdminSeeder.java`) |
| Token | `admin-dummy-token-avva` |

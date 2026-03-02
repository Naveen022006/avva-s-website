# ✅ Render Deployment Checklist

Use this checklist to ensure your application is ready for deployment to Render.

## Pre-Deployment

### Code Preparation
- [ ] All code changes committed to `main` branch
- [ ] `.env` file NOT committed (verify in .gitignore)
- [ ] `.env.example` reflects all required variables
- [ ] No hardcoded secrets in source code
- [ ] Sensitive files in .gitignore (.env, .DS_Store, etc.)

### Configuration Files
- [ ] `render.yaml` exists in project root
- [ ] `Dockerfile` is present and optimized
- [ ] `.dockerignore` exists with appropriate exclusions
- [ ] `application.properties` uses `${VARIABLE}` for sensitive data
- [ ] `pom.xml` has Spring Boot Actuator dependency

### Frontend Configuration
- [ ] `app.js` API_BASE uses environment-aware logic (`window.location.origin`)
- [ ] No hardcoded `localhost:8080` URLs remain
- [ ] CORS settings updated in SecurityConfig for Render domains
- [ ] All image paths are relative (no absolute paths)

### Security
- [ ] Admin password changed from default `admin123`
- [ ] Google OAuth credentials obtained from Google Cloud Console
- [ ] MongoDB Atlas cluster created and configured
- [ ] GOOGLE_CLIENT_ID is valid and authorized URIs include Render domain

## Render Setup

### Account & Repository
- [ ] GitHub account created and repo pushed
- [ ] Render account created at https://render.com
- [ ] GitHub repository connected to Render

### Environment Variables Configured
In Render Dashboard → Environment:
- [ ] `SPRING_DATA_MONGODB_URI` - MongoDB Atlas connection string
  - Format: `mongodb+srv://user:pass@cluster.xxxxx.mongodb.net/avvahomefoods?retryWrites=true&w=majority`
  - IP whitelist set to `0.0.0.0/0` in MongoDB Atlas (or specific Render IP range)
- [ ] `GOOGLE_CLIENT_ID` - Your Google OAuth client ID

### Deployment Configuration
- [ ] Web Service name: `avva-home-foods`
- [ ] Environment: `Docker`
- [ ] Region: `Oregon` (or closest to you)
- [ ] Plan: `Free` (or `Standard` for production)
- [ ] Auto-deploy from GitHub: **Enabled**

## Post-Deployment Tests

### Accessibility
- [ ] Visit `https://your-app.onrender.com` - homepage loads
- [ ] Page styling looks correct (dark theme visible)
- [ ] No console errors in browser DevTools

### API Endpoints
- [ ] `https://your-app.onrender.com/api/products` returns product list
- [ ] `https://your-app.onrender.com/api/categories` returns categories
- [ ] `https://your-app.onrender.com/health` shows UP status

### Authentication
- [ ] Admin login works: `admin` / `admin123` (or new password)
- [ ] User signup/login functional
- [ ] Google OAuth button appears and works
- [ ] JWT token returned on successful login

### Functionality
- [ ] Products display with prices and images
- [ ] Categories filter products correctly
- [ ] Add to cart works (stores in localStorage)
- [ ] Checkout form validation works
- [ ] Order submission successful

### Database
- [ ] Check MongoDB Atlas dashboard - collections have data
- [ ] User account created on signup
- [ ] Order created when placed
- [ ] Review submission creates document

### Logs & Monitoring
- [ ] Render Logs tab shows app is running (no critical errors)
- [ ] MongoDB connections successful (no connection timeout errors)
- [ ] Google OAuth token verification working
- [ ] No permission denied errors in logs

## Troubleshooting Checklist

- [ ] App won't build?
  - Check `Dockerfile` syntax
  - Ensure `pom.xml` is valid XML
  - Check Maven dependencies available

- [ ] App crashes after deploy?
  - Check Render Logs for error messages
  - Verify MongoDB URI is correct
  - Check GOOGLE_CLIENT_ID is set

- [ ] API calls return 404?
  - Verify app.js API_BASE is correct (should auto-detect)
  - Check app is serving at `/` not `/api/`

- [ ] Database connection fails?
  - Verify `SPRING_DATA_MONGODB_URI` in environment
  - Add Render's IP to MongoDB Atlas whitelist
  - Test connection string locally first

- [ ] CORS errors in console?
  - Verify render domain in SecurityConfig
  - Check app is running (not spun down)
  - Clear browser cache

## Continuous Deployment

After this checklist:
- [ ] GitHub actions/workflows set up (optional)
- [ ] Backup strategy for database defined
- [ ] Monitoring/alerting configured (optional)
- [ ] Custom domain mapped (optional)
- [ ] SSL certificate configured (Render does automatically)

## Documentation Updates

- [ ] README.md updated with Render deployment link
- [ ] RENDER_DEPLOYMENT.md is comprehensive and clear
- [ ] .env.example has all required variables documented
- [ ] Team has access to Render credentials (if team project)

---

✅ **Once all items are checked, your app is production-ready!**

Questions? Check [RENDER_DEPLOYMENT.md](./RENDER_DEPLOYMENT.md) for detailed instructions.

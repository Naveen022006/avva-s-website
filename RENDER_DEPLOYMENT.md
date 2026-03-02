# 🚀 Render Deployment Guide - Avva's Home Foods

## Prerequisites
- GitHub account with the repository pushed
- Render account (https://render.com)
- MongoDB Atlas account (for cloud database) OR use Render's PostgreSQL

---

## Step 1: Set Up MongoDB Atlas (Cloud Database)

1. Go to [MongoDB Atlas](https://www.mongodb.com/cloud/atlas)
2. Create a free cluster
3. Create a database user and get the connection string:
   - Format: `mongodb+srv://username:password@cluster0.xxxxx.mongodb.net/avva_home_foods?retryWrites=true&w=majority`
4. Copy this URI - you'll need it for Render env vars

---

## Step 2: Get Google OAuth Credentials

1. Go to [Google Cloud Console](https://console.cloud.google.com)
2. Create a new project
3. Enable Google+ API
4. Create OAuth 2.0 credentials (Web application)
5. Add authorized redirect URIs:
   - `https://your-app-name.onrender.com`
   - `https://your-app-name.onrender.com/callback`
6. Copy your `GOOGLE_CLIENT_ID`

---

## Step 3: Push Code to GitHub

Ensure all files are committed and pushed:
```bash
git add .
git commit -m "Prepare for Render deployment"
git push origin main
```

---

## Step 4: Deploy on Render

### Option A: Using render.yaml (Auto-deploy)

1. Go to https://render.com/dashboard
2. Click **"New +"** → **"Web Service"**
3. Connect your GitHub repository
4. Render will auto-detect `render.yaml` configuration
5. Set environment variables:
   - `SPRING_DATA_MONGODB_URI`: Your MongoDB Atlas connection string
   - `GOOGLE_CLIENT_ID`: Your Google OAuth client ID

### Option B: Manual Configuration (if render.yaml doesn't auto-deploy)

1. **Create Web Service**
   - Name: `avva-home-foods` (or your desired name)
   - Environment: `Docker`
   - Branch: `main`
   - Dockerfile Path: `./Dockerfile`

2. **Set Environment Variables** in Render Dashboard:
   ```
   SPRING_DATA_MONGODB_URI = mongodb+srv://username:password@cluster0.xxxxx.mongodb.net/avva_home_foods?retryWrites=true&w=majority
   GOOGLE_CLIENT_ID = your-google-client-id-here
   ```

3. **Instance Details**
   - Plan: **Free** or **Standard** (free tier has limitations)
   - Region: **Oregon** (recommended)
   - Max Memory: 512 MB (free tier)

4. Click **Create Web Service** and wait for build to complete

---

## Step 5: Test Your Deployment

Once deployed (URL looks like: `https://avva-home-foods.onrender.com`):

1. **Test Frontend:**
   - Visit: `https://avva-home-foods.onrender.com`
   - Should see the homepage

2. **Test API:**
   - Visit: `https://avva-home-foods.onrender.com/api/products`
   - Should see JSON product list

3. **Test Admin Login:**
   - Username: `admin`
   - Password: `admin123`

4. **Troubleshoot Issues:**
   - Check Render Logs: Dashboard → Your App → Logs tab
   - Common issues:
     - MongoDB connection error: Verify MONGODB_URI env var and IP whitelist in MongoDB Atlas
     - Google OAuth mismatch: Ensure GOOGLE_CLIENT_ID matches and authorized URIs are set
     - Port issues: Dockerfile and Render should both use port 8080

---

## Step 6: Update Frontend Domain (Optional)

If you need to hardcode the Render domain in frontend:

**File**: `frontend/js/app.js`

The app automatically detects localhost vs production:
- **Dev**: Uses `http://localhost:8080/api`
- **Prod**: Uses `https://your-app-name.onrender.com/api`

No manual changes needed! 🎉

---

## Step 7: Set Up Custom Domain (Optional)

1. Go to Render Dashboard → Your App → Settings
2. Add Custom Domain: `www.avvahomefoods.com` (if you own the domain)
3. Update DNS records as per Render's instructions

---

## Production Checklist

- [ ] MongoDB Atlas cluster created and whitelisted Render IP
- [ ] Google OAuth credentials generated and URIs updated
- [ ] Environment variables set in Render dashboard
- [ ] Dockerfile builds successfully (`docker build .`)
- [ ] render.yaml is in root directory
- [ ] All secrets removed from codebase
- [ ] Admin password changed from `admin123` to something secure
- [ ] CORS origins updated to include your Render domain
- [ ] API_BASE in app.js uses environment-aware logic

---

## Updating After Deployment

### To Deploy Changes:
1. Make code changes locally
2. Commit and push to GitHub:
   ```bash
   git add .
   git commit -m "Your message"
   git push origin main
   ```
3. Render auto-deploys (watch logs to verify)

### To Change Environment Variables:
1. Render Dashboard → Your App → Environment
2. Update the variable
3. Render automatically redeploys

---

## Important Notes

⚠️ **Free Tier Limitations:**
- Apps spin down after 15 minutes of inactivity
- First request after spin-down takes 30 seconds
- 512 MB memory limit
- No persistent file storage

💡 **Recommendations:**
- Use **Paid Plan** ($7/month) for production
- Store uploaded images in MongoDB (Base64) or AWS S3
- Use MongoDB Atlas free tier (suitable for small apps)
- Enable auto-deploy from GitHub for CI/CD

---

## Environment Variables Reference

| Variable | Example | Required |
|----------|---------|----------|
| `SPRING_DATA_MONGODB_URI` | `mongodb+srv://...` | ✅ Yes |
| `GOOGLE_CLIENT_ID` | `123456.apps.googleusercontent.com` | ✅ Yes |
| `JAVA_OPTS` | (optional for tuning) | ❌ No |

---

## Troubleshooting

### Issue: "MongoDB connection refused"
**Solution:** 
- Verify connection string in MongoDB Atlas
- Add Render IP to MongoDB Atlas IP whitelist (or use 0.0.0.0/0 for all)

### Issue: "Google OAuth validation failed"
**Solution:**
- Verify GOOGLE_CLIENT_ID matches Google Cloud Console
- Add authorized redirect URIs: `https://your-app-name.onrender.com`

### Issue: "Image upload returns 500 error"
**Solution:**
- Images are saved to temp directory on free tier
- Use MongoDB binary storage or AWS S3 for production

### Issue: "Frontend can't reach API"
**Solution:**
- Check app.js API_BASE logic (should auto-detect)
- Verify CORS settings in SecurityConfig
- Check browser console for exact error

---

## Support

For more info:
- [Render Docs](https://render.com/docs)
- [Spring Boot on Render](https://render.com/docs/deploy-spring-boot)
- [MongoDB Atlas Docs](https://docs.atlas.mongodb.com/)

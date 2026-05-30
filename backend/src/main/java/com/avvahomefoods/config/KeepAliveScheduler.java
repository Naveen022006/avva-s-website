package com.avvahomefoods.config;

import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Pings the app's own health endpoint every 14 minutes so Render free-tier
 * never reaches the 15-minute inactivity threshold and shuts the service down.
 * Uses RENDER_EXTERNAL_URL (auto-injected by Render) as the base URL.
 */
@Component
public class KeepAliveScheduler {

    private static final Logger log = LoggerFactory.getLogger(KeepAliveScheduler.class);

    @Value("${RENDER_EXTERNAL_URL:}")
    private String renderExternalUrl;

    @Value("${server.port:8080}")
    private String serverPort;

    // Every 14 minutes (840_000 ms)
    @Scheduled(fixedDelay = 840_000, initialDelay = 60_000)
    public void keepAlive() {
        String base = (renderExternalUrl != null && !renderExternalUrl.isBlank())
                ? renderExternalUrl
                : "http://localhost:" + serverPort;

        String pingUrl = base + "/actuator/health";

        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(pingUrl).openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10_000);
            conn.setReadTimeout(10_000);
            int status = conn.getResponseCode();
            conn.disconnect();
            log.info("[KeepAlive] Pinged {} — HTTP {}", pingUrl, status);
        } catch (Exception e) {
            log.warn("[KeepAlive] Ping failed: {}", e.getMessage());
        }
    }
}

package com.pccth_km_bot.backend.Service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class LineWebhookService {

    private static final String LINE_SET_WEBHOOK_URL = "https://api.line.me/v2/bot/channel/webhook/endpoint";

    private final RestTemplate restTemplate;

    public LineWebhookService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String setLineWebhookUrl(String channelAccessToken, String webhookUrl) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(channelAccessToken);

            Map<String, String> body = Map.of("endpoint", webhookUrl);
            HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    LINE_SET_WEBHOOK_URL, HttpMethod.PUT, request, String.class);

            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to set LINE webhook URL: " + e.getMessage(), e);
        }
    }
}

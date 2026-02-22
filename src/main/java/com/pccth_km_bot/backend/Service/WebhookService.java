package com.pccth_km_bot.backend.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WebhookService {

    private final RestTemplate restTemplate;

    @Value("${n8n.webhook.url}")
    private String n8nWebhookUrl;

    public WebhookService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String forwardToN8n(String linePayload) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request = new HttpEntity<>(linePayload, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(n8nWebhookUrl, request, String.class);

            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to forward to n8n at " + n8nWebhookUrl + ": " + e.getMessage(), e);
        }
    }
}

package com.pccth_km_bot.backend.Service;

import org.springframework.beans.factory.annotation.Value;
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

    @Value("${line.set.webhook}")
    private String lineSetWebhook;

    @Value("${line.test.webhook}")
    private String lineTestWebhook;

    private final RestTemplate restTemplate;

    public LineWebhookService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String setAndVerifyWebhook(String channelAccessToken, String webhookUrl) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(channelAccessToken);

            Map<String, String> body = Map.of("endpoint", webhookUrl);
            HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

            // Set webhook
            restTemplate.exchange(
                    lineSetWebhook,
                    HttpMethod.PUT,
                    request,
                    String.class
            );

            // Verify กับ LINE
            HttpEntity<Void> testRequest = new HttpEntity<>(headers);

            restTemplate.exchange(
                    lineTestWebhook,
                    HttpMethod.POST,
                    testRequest,
                    String.class
            );

            // ยิง test เข้า endpoint ของเราเอง
            ResponseEntity<String> n8nCheck = restTemplate.postForEntity(
                    webhookUrl,
                    "{\"healthCheck\":\"n8n-test\"}",
                    String.class
            );

            if (n8nCheck.getBody() == null ||
                    !n8nCheck.getBody().contains("Workflow was started")) {

                throw new RuntimeException(
                        "Webhook endpoint is reachable but NOT connected to n8n"
                );
            }

            return "Webhook set, verified, and connected to n8n successfully";

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to set/verify webhook: " + e.getMessage(),
                    e
            );
        }
    }
}

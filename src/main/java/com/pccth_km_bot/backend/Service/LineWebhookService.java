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
    private static final String LINE_TEST_WEBHOOK_URL = "https://api.line.me/v2/bot/channel/webhook/test";
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
                    LINE_SET_WEBHOOK_URL,
                    HttpMethod.PUT,
                    request,
                    String.class
            );

            // Verify กับ LINE
            HttpEntity<Void> testRequest = new HttpEntity<>(headers);

            restTemplate.exchange(
                    LINE_TEST_WEBHOOK_URL,
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

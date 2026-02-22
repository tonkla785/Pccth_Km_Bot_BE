package com.pccth_km_bot.backend.Controller;

import com.pccth_km_bot.backend.Service.WebhookService;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("webhook-line-bot")
public class WebhookController {

    private final WebhookService webhookService;

    public WebhookController(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    @PostMapping("/callback")
    public ResponseEntity<?> lineCallback(@RequestBody String linePayload) {
        String n8nResponse = webhookService.forwardToN8n(linePayload);
        return ResponseEntity.ok(Map.of(
                "responseStatus", 200,
                "responseMessage", "Forwarded to n8n successfully",
                "data", n8nResponse));
    }
}

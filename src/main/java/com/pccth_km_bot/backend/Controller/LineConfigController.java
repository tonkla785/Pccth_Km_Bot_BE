package com.pccth_km_bot.backend.Controller;

import com.pccth_km_bot.backend.DTO.WebhookRequestDTO;
import com.pccth_km_bot.backend.Service.LineWebhookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("line-config")
public class LineConfigController {

    private final LineWebhookService lineWebhookService;

    public LineConfigController(LineWebhookService lineWebhookService) {
        this.lineWebhookService = lineWebhookService;
    }

    @PostMapping("/set-webhook")
    public ResponseEntity<?> setLineWebhook(@Valid @RequestBody WebhookRequestDTO request) {
        lineWebhookService.setLineWebhookUrl(request.getChannelAccessToken(), request.getWebhookUrl());
        return ResponseEntity.ok(Map.of(
                "responseStatus", 200,
                "responseMessage", "LINE webhook URL updated successfully",
                "webhookUrl", request.getWebhookUrl()));
    }
}

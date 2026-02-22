package com.pccth_km_bot.backend.DTO;

import jakarta.validation.constraints.NotNull;

public class WebhookRequestDTO {

    @NotNull(message = "channelAccessToken is required")
    private String channelAccessToken;

    @NotNull(message = "webhookUrl is required")
    private String webhookUrl;

    public String getChannelAccessToken() {
        return channelAccessToken;
    }

    public void setChannelAccessToken(String channelAccessToken) {
        this.channelAccessToken = channelAccessToken;
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }

    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }
}

package com.uranus.whproxy.controllers;

import com.uranus.whproxy.services.WebhookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequestMapping("/doserve/{token}")
@RestController
public class WebhookController {

    @Autowired
    private WebhookService webhookService;

    //TODO: This endpoint should be async
    @PostMapping
    public ResponseEntity<String> action(
            HttpServletRequest request,
            @PathVariable(name = "token") String token,
            @RequestParam(name = "bgurl") String bgurl)
            throws IOException {

        return webhookService.proxyRequest(request, bgurl);
    }

    public WebhookService getWebhookService() {
        return webhookService;
    }

    public void setWebhookService(WebhookService webhookService) {
        this.webhookService = webhookService;
    }
}

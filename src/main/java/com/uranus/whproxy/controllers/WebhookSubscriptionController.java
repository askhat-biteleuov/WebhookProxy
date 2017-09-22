package com.uranus.whproxy.controllers;

import com.uranus.whproxy.services.WebhookService;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequestMapping("/webhook/{token}")
@RestController
public class WebhookSubscriptionController {

    @Autowired
    private WebhookService webhookService;

    @PostMapping
    public ResponseEntity<String> add(
            HttpServletRequest request,
            @PathVariable(name = "token") String token,
            @RequestParam(name = "value") String value)
            throws IOException {

        URIBuilder uriBuilder = new URIBuilder()
                .setHost(request.getServerName())
                .setPath("/doserve/" + token)
                .addParameter("bgurl", value);

        if (request.getHeader("x-forwarded-proto") != null) {
            uriBuilder.setScheme(request.getHeader("x-forwarded-proto"));
        } else {
            uriBuilder.setScheme(request.getScheme());
        }

        return webhookService.add(token, uriBuilder.toString());
    }

    @GetMapping
    public ResponseEntity<String> get(
            @PathVariable(name = "token") String token)
            throws IOException {

        return webhookService.get(token);
    }

    @PutMapping
    public ResponseEntity<String> update(
            HttpServletRequest request,
            @PathVariable(name = "token") String token,
            @RequestParam(name = "value") String value)
            throws IOException {

        return this.add(request, token, value);
    }

    @DeleteMapping
    public ResponseEntity<String> delete(
            @PathVariable(name = "token") String token)
            throws IOException {

        return webhookService.delete(token);
    }

    public WebhookService getWebhookService() {
        return webhookService;
    }

    public void setWebhookService(WebhookService webhookService) {
        this.webhookService = webhookService;
    }
}

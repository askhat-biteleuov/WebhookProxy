package com.uranus.whproxy.services;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class WebhookService {

    public ResponseEntity<String> add(String token, String origin) throws IOException {
        return doRequest("https://api.telegram.org/bot" + token + "/setWebhook?url=" + origin);
    }

    public ResponseEntity<String> get(String token) throws IOException {
        return doRequest("https://api.telegram.org/bot" + token + "/getWebhookInfo");
    }

    public ResponseEntity<String> update(String token, String origin) throws IOException {
        return this.add(token, origin);
    }

    public ResponseEntity<String> delete(String token) throws IOException {
        return doRequest("https://api.telegram.org/bot" + token + "/deleteWebhook");
    }

    private ResponseEntity<String> doRequest(String url)
            throws IOException {

        CloseableHttpClient httpclient = HttpClients.createDefault();

        return httpclient.execute(new HttpGet(url), response -> {
            HttpEntity entity = response.getEntity();
            return new ResponseEntity<>(
                    entity != null ? EntityUtils.toString(entity) : null,
                    HttpStatus.valueOf(response.getStatusLine().getStatusCode()));
        });
    }

    public ResponseEntity<String> proxyRequest(HttpServletRequest origin, String bgurl)
            throws IOException {

        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost request = new HttpPost(bgurl);

        BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
        basicHttpEntity.setContent(origin.getInputStream());
        request.setEntity(basicHttpEntity);

        ResponseEntity<String> result = httpclient.execute(request, response -> {
            HttpEntity entity = response.getEntity();
            return new ResponseEntity<>(
                    entity != null ? EntityUtils.toString(entity) : null,
                    HttpStatus.valueOf(response.getStatusLine().getStatusCode()));
        });

        httpclient.close();

        return result;
    }
}

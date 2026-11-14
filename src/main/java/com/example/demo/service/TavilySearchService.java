
package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TavilySearchService {

    @Value("${tavily.enabled:false}")
    private boolean enabled;

    @Value("${tavily.apiKey:}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Map<String, String>> search(String query) {

        List<Map<String, String>> results = new ArrayList<>();

        if (!enabled || apiKey.isBlank()) {
            return results;
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            Map<String, Object> body = new HashMap<>();
            body.put("query", query);
            body.put("max_results", 10);

            HttpEntity<Map<String, Object>> request =
                    new HttpEntity<>(body, headers);

            Map response = restTemplate.postForObject(
                    "https://api.tavily.com/search",
                    request,
                    Map.class
            );

            if (response != null && response.get("results") instanceof List) {
                List items = (List) response.get("results");

                for (Object obj : items) {
                    if (obj instanceof Map) {
                        Map item = (Map) obj;

                        Map<String, String> result = new HashMap<>();
                        result.put("title", String.valueOf(item.get("title")));
                        result.put("url", String.valueOf(item.get("url")));
                        result.put("content", String.valueOf(item.get("content")));

                        results.add(result);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Search error: " + e.getMessage());
        }

        return results;
    }
}

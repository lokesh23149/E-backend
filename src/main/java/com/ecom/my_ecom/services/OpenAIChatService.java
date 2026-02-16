package com.ecom.my_ecom.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OpenAIChatService {

    private static final String GROQ_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

    private static final String SYSTEM_PROMPT = """
        You are a helpful shopping assistant for a gym and fitness e-commerce store.

        The store sells products in these categories:
        - Accessories
        - Clothing
        - Equipment
        - Supplements

        Help users with:
        - Product recommendations based on fitness goals
        - Workout & training related product suggestions
        - Supplement guidance (general, non-medical)
        - Order tracking
        - Shipping & delivery
        - Returns & refunds
        - General shopping questions

        Be concise, friendly, and professional.

        If users ask about supplements:
        - Provide general information only
        - Do NOT give medical advice
        - Suggest consulting a professional for health-related concerns
        """;

    @Value("${groq.api.key:}")
    private String groqKey;

    @Value("${openai.api.key:}")
    private String openaiKey;

    @Value("${openai.chat.model:gpt-4o-mini}")
    private String openaiModel;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OpenAIChatService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder
                .setConnectTimeout(java.time.Duration.ofSeconds(10))
                .setReadTimeout(java.time.Duration.ofSeconds(20))
                .build();
    }

    public String chat(String userMessage) {
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return "Please enter a message.";
        }
        
        String groq = groqKey != null ? groqKey.trim() : "";
        String openai = openaiKey != null ? openaiKey.trim() : "";
        
        // Use Groq as primary (free), fallback to OpenAI only if Groq fails
        if (!groq.isEmpty()) {
            String result = callChat(GROQ_URL, groq, "llama-3.1-8b-instant", userMessage);
            if (result != null && !result.contains("error") && !result.contains("Invalid") && !result.contains("API error")) {
                return result;
            }
            // If Groq fails and OpenAI available, try OpenAI as fallback
            if (!openai.isEmpty()) {
                String modelToUse = (openaiModel != null && !openaiModel.trim().isEmpty()) 
                    ? openaiModel.trim() 
                    : "gpt-4o-mini";
                String openaiResult = callChat(OPENAI_URL, openai, modelToUse, userMessage);
                if (openaiResult != null && !openaiResult.contains("quota") && !openaiResult.contains("billing")) {
                    return openaiResult;
                }
            }
            return result; // Return Groq error if both fail
        }
        
        // If no Groq key, try OpenAI
        if (!openai.isEmpty()) {
            String modelToUse = (openaiModel != null && !openaiModel.trim().isEmpty()) 
                ? openaiModel.trim() 
                : "gpt-4o-mini";
            return callChat(OPENAI_URL, openai, modelToUse, userMessage);
        }
        
        return "Chat not configured. Set groq.api.key (free at https://console.groq.com) in application.properties.";
    }
    
    private String callChat(String url, String apiKey, String model, String userMessage) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            Map<String, Object> body = Map.of(
                    "model", model,
                    "messages", List.of(
                            Map.of("role", "system", "content", SYSTEM_PROMPT),
                            Map.of("role", "user", "content", userMessage.trim())
                    ),
                    "max_tokens", 200,
                    "temperature", 0.5
            );

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null && !response.getBody().isBlank()) {
                Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), Map.class);
                String content = extractContent(responseMap);
                if (content != null && !content.isBlank()) {
                    return content.trim();
                }
            }
            return "Sorry, I couldn't generate a response. Please try again.";
        } catch (HttpStatusCodeException e) {
            boolean isGroq = url.contains("groq");
            String errorMsg = parseApiError(e.getResponseBodyAsString(), e.getStatusCode(), isGroq);
            return errorMsg != null ? errorMsg : "API error: " + e.getStatusCode().value();
        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg != null && (msg.contains("401") || msg.contains("Unauthorized"))) {
                boolean isGroq = url.contains("groq");
                return isGroq 
                    ? "Invalid Groq API key. Check groq.api.key in application.properties or get a free key at https://console.groq.com"
                    : "Invalid API key.";
            }
            if (msg != null && msg.contains("timed out")) {
                return "Request timed out. Please try again.";
            }
            return "Error: " + (msg != null ? msg : "Unknown error occurred.");
        }
    }

    @SuppressWarnings("unchecked")
    private String extractContent(Map<String, Object> body) {
        Object choices = body.get("choices");
        if (!(choices instanceof List) || ((List<?>) choices).isEmpty()) {
            return null;
        }
        Object firstChoice = ((List<?>) choices).get(0);
        if (!(firstChoice instanceof Map)) {
            return null;
        }
        Object message = ((Map<?, ?>) firstChoice).get("message");
        if (!(message instanceof Map)) {
            return null;
        }
        Object content = ((Map<?, ?>) message).get("content");
        if (content == null) {
            return null;
        }
        if (content instanceof String) {
            return (String) content;
        }
        if (content instanceof List) {
            List<?> parts = (List<?>) content;
            StringBuilder sb = new StringBuilder();
            for (Object part : parts) {
                if (part instanceof Map) {
                    Object text = ((Map<?, ?>) part).get("text");
                    if (text != null) sb.append(text.toString());
                }
            }
            return sb.length() > 0 ? sb.toString() : null;
        }
        return content.toString();
    }

    @SuppressWarnings("unchecked")
    private String parseApiError(String responseBody, org.springframework.http.HttpStatusCode status, boolean isGroq) {
        int code = status.value();
        if (responseBody == null || responseBody.isBlank()) {
            if (code == 401) {
                return isGroq 
                    ? "Invalid Groq API key. Check groq.api.key in application.properties or get a free key at https://console.groq.com"
                    : "Invalid API key. Check your API key in application.properties.";
            }
            if (code == 429) {
                return isGroq
                    ? "Groq rate limit reached. Please try again in a moment."
                    : "Rate limit or quota exceeded. Check billing at https://platform.openai.com/account/billing";
            }
            return null;
        }
        try {
            Map<String, Object> json = objectMapper.readValue(responseBody, Map.class);
            Object err = json.get("error");
            if (err instanceof Map) {
                Object message = ((Map<String, Object>) err).get("message");
                if (message != null) {
                    String msg = message.toString();
                    if (!isGroq && (msg.toLowerCase().contains("quota") || msg.toLowerCase().contains("billing"))) {
                        return msg + " Add payment at https://platform.openai.com/account/billing";
                    }
                    return msg;
                }
            }
        } catch (Exception ignored) { }
        if (code == 401) {
            return isGroq 
                ? "Invalid Groq API key. Check groq.api.key in application.properties or get a free key at https://console.groq.com"
                : "Invalid API key. Check your API key in application.properties.";
        }
        if (code == 429) {
            return isGroq
                ? "Groq rate limit reached. Please try again in a moment."
                : "Rate limit or quota exceeded. Check billing at https://platform.openai.com/account/billing";
        }
        return null;
    }
}

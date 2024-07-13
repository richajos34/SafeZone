package com.berkeley.irms.warnme.services;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OpenAIService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final OkHttpClient client = new OkHttpClient();

    /**
     * Extracts the location from a natural language query using the OpenAI API.
     *
     * @param naturalLanguageQuery the natural language query to extract the location from
     * @return the extracted location, or an empty string if the extraction fails
     */
    public String extractLocation(String naturalLanguageQuery) {
        JSONObject json = new JSONObject();
        json.put("model", "gpt-3.5-turbo");
        json.put("prompt", "Extract the location from this query: \"" + naturalLanguageQuery + "\". The location should be a proper noun and can include buildings, landmarks, or place names. Return only the location name.");
        System.out.println("query: " + naturalLanguageQuery); // correct query
        json.put("max_tokens", 100);

        RequestBody body = RequestBody.create(
               json.toString(),
                MediaType.parse("application/json; charset=utf-8")
        );
        System.out.println("Request JSON: " + json.toString(2)); // Print the request JSON
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/completions")
                .header("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();

        System.out.println("Request URL: " + request.url());
        System.out.println("Request Headers: " + request.headers());
        System.out.println("Request Body: " + body);

        //OpenAI service failing to get coordinates
        try (Response response = client.newCall(request).execute()) {
            System.out.println("Response Code: " + response.code()); // Print the response code
            System.out.println("Response Message: " + response.message()); // Print the response message
            if (response.isSuccessful()) {
                JSONObject responseBody = new JSONObject(response.body().string());
                return responseBody.getJSONArray("choices").getJSONObject(0).getString("text").trim();
            } else {
                System.err.println("Failed to extract location: " + response.message());
                String responseBodyString = response.body().string();
                System.err.println("Response body: " + responseBodyString); // Print the response body for debugging
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}

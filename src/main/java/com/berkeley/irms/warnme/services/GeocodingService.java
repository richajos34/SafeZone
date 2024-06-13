package com.berkeley.irms.warnme.services;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.jetbrains.annotations.TestOnly;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GeocodingService {

    @Value("${google.api.key}")
    private String apiKey;

    private final OkHttpClient client = new OkHttpClient();

    public double[] getCoordinates(String location) {
        apiKey ="AIzaSyCedXCGNwepBSi8wHKg1aZuQPZHq_UAkYE";
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + location + "&key=" + apiKey;

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                JSONObject responseBody = new JSONObject(response.body().string());
                JSONObject locationJson = responseBody
                        .getJSONArray("results")
                        .getJSONObject(0)
                        .getJSONObject("geometry")
                        .getJSONObject("location");
                double lat = locationJson.getDouble("lat");
                double lng = locationJson.getDouble("lng");
                return new double[]{lat, lng};
            } else {
                System.err.println("Failed to get coordinates: " + response.message());
                return new double[]{};
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new double[]{};
        }
    }

    public static void main(String[] args) {
        GeocodingService geocodingService = new GeocodingService();
        double[] coords = geocodingService.getCoordinates("Wheeler Hall, Berkeley, CA");
        System.out.println(coords[0]+" "+coords[1]);

    }
}

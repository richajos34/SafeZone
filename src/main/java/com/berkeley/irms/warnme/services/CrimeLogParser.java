package com.berkeley.irms.warnme.services;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.berkeley.irms.warnme.WarnmeApplication;
import com.berkeley.irms.warnme.models.Incident;
import com.berkeley.irms.warnme.models.Location;

public class CrimeLogParser {

    private final IncidentService incidentService;

    @Autowired
    public CrimeLogParser(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    public JSONArray parseCrimeLogToJson(String ocrText) {
        JSONArray jsonArray = new JSONArray();
        String[] entries = ocrText.split("\n\n");

        for (String entry : entries) {
            String[] lines = entry.trim().split("\n");
            JSONObject jsonObject = new JSONObject();

            for (String line : lines) {
                String[] parts = line.split(":");
                if (parts.length > 1) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    jsonObject.put(key, value);
                }
            }

            if (jsonObject.length() > 0) {
                jsonArray.put(jsonObject);
            }
        }

        return jsonArray;
    }

    public List<Incident> parseJsonToIncidents(JSONArray jsonArray) {
        List<Incident> incidents = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            String caseNumber = jsonObject.optString("case_number", "Unknown Case Number");
            String description = jsonObject.optString("crimes", "No description provided");
            String reported = jsonObject.optString("reported_date", "Unknown Date");
            String locationDetails = jsonObject.optString("location", "Unknown Location");
            String disposition = jsonObject.optString("disposition", "Unknown Disposition");

            Location location = new Location((float) 23.8, (float) 34.5);
                                                                          

            Incident incident = new Incident(
                    "Case #" + caseNumber,
                    description,
                    location,
                    reported,
                    disposition.contains("Closed") ? "Closed" : "Active",
                    "Crime"
            );

            incidents.add(incident);
        }

        return incidents;
    }

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(WarnmeApplication.class, args);

        // Get the bean from the Spring context
        CrimeLogParser parser = context.getBean(CrimeLogParser.class);
        String ocrText = "Case #24-01609\nCrime(s): 23152(A): VC; Dui Alcohol; Misdemeanor\nReported: 06/02/2024 02:57\nOccurred Range: 06/02/2024 02:57 - 6/2/24 3:17\nLocation(s): Shattuck Av & Channing Wy\nDisposition: Closed: 6/2/2024\n\nCase #24-01610\nINFO: Information Only Report\nReported: 06/02/2024 07:04\nOccurred Range: 06/02/2024 07:04 - 6/2/24 8:08\nLocation(s): Jackson St & Monroe St\nDisposition: Inactive: 6/2/2024";

        JSONArray result = parser.parseCrimeLogToJson(ocrText);
        List<Incident> incidents = parser.parseJsonToIncidents(result);

        List<Incident> savedIncidents = parser.incidentService.saveAllIncidents(incidents);
        savedIncidents.forEach(incident -> System.out.println("Saved Incident: " + incident.getId()));

    }
}

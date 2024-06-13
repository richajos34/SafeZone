package com.berkeley.irms.warnme.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.berkeley.irms.warnme.models.Incident;
import com.berkeley.irms.warnme.services.GeocodingService;
import com.berkeley.irms.warnme.services.IncidentService;
import com.berkeley.irms.warnme.services.OpenAIService;

import java.util.List;
//import java.util.Optional;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    private final IncidentService incidentService;
    private final OpenAIService openAIService;
    private final GeocodingService geocodingService;


    @Autowired
    public IncidentController(IncidentService incidentService, OpenAIService openAIService, GeocodingService geocodingService) {
        this.incidentService = incidentService;
        this.openAIService = openAIService;
        this.geocodingService = geocodingService;
    }

    @GetMapping
    public List<Incident> getAllIncidents() {
        return incidentService.getAllIncidents();
    }

    @GetMapping("/search")
    public List<Incident> searchIncidents(@RequestParam String query) {
        //"Function: get incidents near a location in a search
        // make component for AI search
        String location = openAIService.extractLocation(query);
        double[] coords = geocodingService.getCoordinates(location);
        if (coords.length == 2) {
            return incidentService.findIncidentsNear(coords[0], coords[1], 0.2);
        } else {
            return List.of();
        }
    }


    @GetMapping("/{id}")
    public Incident getIncidentById(@PathVariable String id) {
        return incidentService.getIncidentById(id)
                .orElseThrow(() -> new RuntimeException("Incident not found with id: " + id));
    }

     @GetMapping("/")
    public Incident getIncidentByTitle(@RequestParam String title) {
        return incidentService.getIncidentByTitle(title)
                .orElseThrow(() -> new RuntimeException("Incident not found with id: " + title));
    }

    @PostMapping
    public Incident createIncident(@RequestBody Incident incident) {
        return incidentService.createIncident(incident);
    }

    @DeleteMapping("/{id}")
    public void deleteIncident(@PathVariable String id) {
        incidentService.deleteIncident(id);
    }


}

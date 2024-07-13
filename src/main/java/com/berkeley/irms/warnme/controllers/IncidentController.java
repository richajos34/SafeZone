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
    /**
     * Constructs an IncidentController instance with the provided IncidentService, OpenAIService, and GeocodingService.
     *
     * @param incidentService The IncidentService implementation to use for incident-related operations.
     * @param openAIService The OpenAIService implementation to use for AI-related functionality.
     * @param geocodingService The GeocodingService implementation to use for geocoding-related operations.
     */
    public IncidentController(IncidentService incidentService, OpenAIService openAIService, GeocodingService geocodingService) {
        this.incidentService = incidentService;
        this.openAIService = openAIService;
        this.geocodingService = geocodingService;
    }

    /**
     * Retrieves a list of all incidents.
     *
     * @return A list of all incidents.
     */
    @GetMapping
    public List<Incident> getAllIncidents() {
        return incidentService.getAllIncidents();
    }

    /**
     * Searches for incidents near a given location based on the provided query.
     *
     * @param query The search query containing a location.
     * @return A list of incidents near the extracted location, or an empty list if the location could not be determined.
     */
    @GetMapping("/search")
    public List<Incident> searchIncidents(@RequestParam String query) {
        String location = openAIService.extractLocation(query);
        double[] coords = geocodingService.getCoordinates(location);
        if (coords.length == 2) {
            return incidentService.findIncidentsNear(coords[0], coords[1], 0.2);
        } else {
            return List.of();
        }
    }


    /**
     * Retrieves an incident by its unique identifier.
     *
     * @param id The unique identifier of the incident to retrieve.
     * @return The incident with the specified ID, or throws a RuntimeException if the incident is not found.
     */
    @GetMapping("/{id}")
    public Incident getIncidentById(@PathVariable String id) {
        return incidentService.getIncidentById(id)
                .orElseThrow(() -> new RuntimeException("Incident not found with id: " + id));
    }

     /**
      * Retrieves an incident by its title.
      *
      * @param title The title of the incident to retrieve.
      * @return The incident with the specified title, or throws a RuntimeException if the incident is not found.
      */
     @GetMapping("/")
    public Incident getIncidentByTitle(@RequestParam String title) {
        return incidentService.getIncidentByTitle(title)
                .orElseThrow(() -> new RuntimeException("Incident not found with id: " + title));
    }

    /**
     * Creates a new incident.
     *
     * @param incident The incident to create.
     * @return The created incident.
     */
    @PostMapping
    public Incident createIncident(@RequestBody Incident incident) {
        return incidentService.createIncident(incident);
    }

    /**
     * Deletes an incident by its unique identifier.
     *
     * @param id The unique identifier of the incident to delete.
     */
    @DeleteMapping("/{id}")
    public void deleteIncident(@PathVariable String id) {
        incidentService.deleteIncident(id);
    }


}

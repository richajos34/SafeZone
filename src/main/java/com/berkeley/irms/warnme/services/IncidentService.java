package com.berkeley.irms.warnme.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.berkeley.irms.warnme.cache.InMemCache;
import com.berkeley.irms.warnme.models.Incident;
import com.berkeley.irms.warnme.repositories.IncidentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IncidentService {

    private static final String incidentsCacheKey = "incidentsCacheKey";
    private final IncidentRepository incidentRepository;
    //private final SmsService smsService;

    private final InMemCache<List<Incident>> cache = new InMemCache<>();;

    @Autowired
    public IncidentService(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
        //this.smsService = smsService;
    }

    /**
     * Retrieves all incidents from the incident repository, caching the results in memory for faster subsequent retrieval.
     *
     * @return a list of all incidents, retrieved from the cache if available, or from the repository if not.
     */
    public List<Incident> getAllIncidents() {
        List<Incident> incidents = null;
        incidents = cache.get(incidentsCacheKey);
        if (incidents != null) {
            return incidents;
        }

        incidents = incidentRepository.findAll();
        if (incidents != null && incidents.size() > 0) {
            cache.put(incidentsCacheKey, incidents);
        }
        return incidents;
    }

    /**
     * Finds all incidents within the specified radius of the given latitude and longitude.
     *
     * @param lat The latitude of the center point.
     * @param lon The longitude of the center point.
     * @param radiusInMiles The radius in miles to search for incidents.
     * @return A list of incidents that are within the specified radius.
     */
    public List<Incident> findIncidentsNear(double lat, double lon, double radiusInMiles) {
        List<Incident> allIncidents = incidentRepository.findAll();
        List<Incident> nearbyIncidents = new ArrayList<>();

        for (Incident incident : allIncidents) {
            double incidentLat = incident.getLocation().getX();
            double incidentLon = incident.getLocation().getY();

            if (distance(lat, lon, incidentLat, incidentLon) <= radiusInMiles) {
                nearbyIncidents.add(incident);
            }
        }

        return nearbyIncidents;
    }
    
    /**
     * Calculates the distance between two geographic coordinates in miles.
     *
     * @param lat1 The latitude of the first coordinate.
     * @param lon1 The longitude of the first coordinate.
     * @param lat2 The latitude of the second coordinate.
     * @param lon2 The longitude of the second coordinate.
     * @return The distance between the two coordinates in miles.
     */
    
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 3959; //earth radius
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // miles
    }

    public Optional<Incident> getIncidentById(String id) {
        return incidentRepository.findById(id);
    }

    public Optional<Incident> getIncidentByTitle(String title) {
        return incidentRepository.findFirstByTitleContainingIgnoreCase(title);
    }

    /**
     * Creates a new incident and saves it to the repository.
     *
     * @param incident The incident to be created.
     * @return The saved incident.
     */
    public Incident createIncident(Incident incident) {
        Incident savedIncident = incidentRepository.save(incident);
        cache.clear();
        return savedIncident;
    }

    public List<Incident> saveAllIncidents(List<Incident> incidents) {
        return incidentRepository.saveAll(incidents);  // Save a list of incidents
    }

    public void deleteIncident(String id) {
        cache.clear();
        incidentRepository.deleteById(id);
    }

}

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
    // Haversine formula
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

    public Incident createIncident(Incident incident) {
        Incident savedIncident = incidentRepository.save(incident);
        //String message = "New Incident Reported: " + incident.getTitle() + " at " + incident.getLocation().toString();
        //smsService.sendSms("+19096829050", message);
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

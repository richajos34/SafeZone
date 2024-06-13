package com.berkeley.irms.warnme.repositories;

import com.berkeley.irms.warnme.models.Incident;
import com.berkeley.irms.warnme.models.Location;

import org.springframework.data.geo.Distance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncidentRepository extends MongoRepository<Incident, String> {

    List<Incident> findByStatus(String status);

     Optional<Incident> findFirstByTitleContainingIgnoreCase(String keyword);
     
    @Query("{'location': {$near: {$geometry: {type: 'Point', coordinates: [?1, ?0]}, $maxDistance: ?2}}}")
    List<Incident> findByLocationNear(double lat, double lon, double maxDistance);
}
package com.berkeley.irms.warnme.repositories;

import com.berkeley.irms.warnme.models.City;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CityRepository extends MongoRepository<City, String> {

     Optional<City> findFirstByNameContainingIgnoreCase(String keyword);
}
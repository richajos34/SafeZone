package com.berkeley.irms.warnme.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.berkeley.irms.warnme.models.City;
import com.berkeley.irms.warnme.repositories.CityRepository;

import java.util.List;
import java.util.Optional;

@Service
/**
 * Provides services for managing cities, including retrieving a list of all cities, getting a city by ID or name, creating a new city, and deleting a city.
 */
public class CityService {

    private final CityRepository cityRepository;

    @Autowired
    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    /**
     * Retrieves a list of all cities.
     * @return a list of all cities
     */
    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    /**
     * Retrieves a city by its unique identifier.
     * @param id the unique identifier of the city to retrieve
     * @return an {@link Optional} containing the city if found, or an empty {@link Optional} if not found
     */
    public Optional<City> getCityById(String id) {
        return cityRepository.findById(id);
    }

     /**
      * Retrieves a city by its name.
      * @param name the name of the city to retrieve
      * @return an {@link Optional} containing the city if found, or an empty {@link Optional} if not found
      */
     public Optional<City> getCityByName(String name) {
        return cityRepository.findFirstByNameContainingIgnoreCase(name);
    }

    /**
     * Creates a new city and saves it to the database.
     * @param city the city object to be created
     * @return the created city object
     */
    public City createCity(City city) {
        return cityRepository.save(city);
    }

    /**
     * Deletes a city from the database by its unique identifier.
     * @param id the unique identifier of the city to delete
     */
    public void deleteCity(String id) {
        cityRepository.deleteById(id);
    }

}

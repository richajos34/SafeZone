package com.berkeley.irms.warnme.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.berkeley.irms.warnme.models.City;
import com.berkeley.irms.warnme.services.CityService;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
public class CityController {

    private final CityService CityService;

    @Autowired
    public CityController(CityService CityService) {
        this.CityService = CityService;
    }

    /**
     * Retrieves a list of all cities.
     *
     * @return a list of all cities
     */
    @GetMapping
    public List<City> getAllCities() {
        return CityService.getAllCities();
    }

    /**
     * Retrieves a city by its unique identifier.
     *
     * @param id the unique identifier of the city to retrieve
     * @return the city with the specified identifier, or throw a RuntimeException if the city is not found
     */
    @GetMapping("/{id}")
    public City getCityById(@PathVariable String id) {
        return CityService.getCityById(id)
                .orElseThrow(() -> new RuntimeException("City not found with id: " + id));
    }

     /**
      * Retrieves a city by its name.
      *
      * @param name the name of the city to retrieve
      * @return the city with the specified name, or throw a RuntimeException if the city is not found
      */
     @GetMapping("/")
    public City getCityByTitle(@RequestParam String name) {
        return CityService.getCityByName(name)
                .orElseThrow(() -> new RuntimeException("City not found with id: " + name));
    }

    /**
     * Creates a new city.
     *
     * @param city the city to create
     * @return the created city
     */
    @PostMapping
    public City createCity(@RequestBody City City) {
        return CityService.createCity(City);
    }

    /**
     * Deletes a city by its unique identifier.
     *
     * @param id the unique identifier of the city to delete
     */
    @DeleteMapping("/{id}")
    public void deleteCity(@PathVariable String id) {
        CityService.deleteCity(id);
    }
}

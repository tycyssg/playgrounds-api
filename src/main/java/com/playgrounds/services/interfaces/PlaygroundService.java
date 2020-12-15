package com.playgrounds.services.interfaces;


import com.playgrounds.exceptions.models.CountiesNotFoundException;
import com.playgrounds.exceptions.models.CountyNotFoundException;
import com.playgrounds.exceptions.models.ParkNotFoundException;
import com.playgrounds.exceptions.models.ParksDataNotFoundException;
import com.playgrounds.models.County;
import com.playgrounds.models.Park;

import java.util.List;

public interface PlaygroundService {

    public List<County> getCountiesWithParks() throws CountiesNotFoundException;

    public County getSpecificCounty(String countyName) throws CountyNotFoundException;

    public List<Park> getParksOfSpecificCounty(Long countyId) throws ParksDataNotFoundException;

    public Park getParkById(Long parkId) throws ParkNotFoundException;

    public Park getParkByName(String parkName) throws ParkNotFoundException;

    public void deletePark(Long parkId) throws ParkNotFoundException;

    public void deleteCounty(Long countyId) throws CountyNotFoundException;

    public void updateCountyName(Long countyId, String countyName) throws CountyNotFoundException;
}

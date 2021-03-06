package com.playgrounds.services;

import com.playgrounds.dto.ParkDto;
import com.playgrounds.exceptions.models.CountiesNotFoundException;
import com.playgrounds.exceptions.models.CountyNotFoundException;
import com.playgrounds.exceptions.models.ParkNotFoundException;
import com.playgrounds.exceptions.models.ParksDataNotFoundException;
import com.playgrounds.models.County;
import com.playgrounds.models.Park;
import com.playgrounds.models.ParkFacility;
import com.playgrounds.models.ParkOpeningHours;
import com.playgrounds.repository.CountyRepository;
import com.playgrounds.repository.ParkFacilityRepository;
import com.playgrounds.repository.ParkOpeningHoursRepository;
import com.playgrounds.repository.ParkRepository;
import com.playgrounds.services.interfaces.PlaygroundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.playgrounds.constants.ErrorConstants.*;


@Service
public class PlaygroundServiceImplementation implements PlaygroundService {

    private final CountyRepository countyRepository;
    private final ParkRepository parkRepository;
    private final ParkFacilityRepository parkFacilityRepository;
    private final ParkOpeningHoursRepository parkOpeningHoursRepository;

    @Autowired
    public PlaygroundServiceImplementation(CountyRepository countyRepository, ParkRepository parkRepository, ParkFacilityRepository parkFacilityRepository, ParkOpeningHoursRepository parkOpeningHoursRepository) {
        this.countyRepository = countyRepository;
        this.parkRepository = parkRepository;
        this.parkFacilityRepository = parkFacilityRepository;
        this.parkOpeningHoursRepository = parkOpeningHoursRepository;
    }

    @Override
    public List<County> getCountiesWithParks() throws CountiesNotFoundException {
        List<County> dbData = countyRepository.findAll();

        if (dbData.isEmpty()) throw new CountiesNotFoundException();

        return dbData;
    }

    @Override
    public County getSpecificCounty(String countyName) throws CountyNotFoundException {
        if (!countyRepository.existsByCounty(countyName)) throw new CountyNotFoundException();

        return countyRepository.findByCounty(countyName);
    }

    @Override
    public List<Park> getParksOfSpecificCounty(Long countyId) throws ParksDataNotFoundException {
        List<Park> dbData = parkRepository.findAllByCountyId(countyId);

        if (dbData.isEmpty()) throw new ParksDataNotFoundException();

        return dbData;
    }

    @Override
    public Park getParkById(Long parkId) throws ParkNotFoundException {
        if (!parkRepository.existsById(parkId)) throw new ParkNotFoundException(PARK_NOT_FOUND_ID);

        return parkRepository.findById(parkId).orElse(null);
    }

    @Override
    public Park getParkByName(String parkName) throws ParkNotFoundException {
        if (!parkRepository.existsByName(parkName)) throw new ParkNotFoundException(PARK_NOT_FOUND_NAME);

        return parkRepository.findByName(parkName);
    }

    @Override
    public void deletePark(Long parkId) throws ParkNotFoundException {
        if (!parkRepository.existsById(parkId)) throw new ParkNotFoundException(PARK_NOT_FOUND_ID);

        parkRepository.deleteById(parkId);
    }

    @Override
    public void deleteCounty(Long countyId) throws CountyNotFoundException {
        if (!countyRepository.existsById(countyId)) throw new CountyNotFoundException(COUNTY_DATA_NOT_FOUND_ID);

        countyRepository.deleteById(countyId);
    }

    @Override
    public void updateCountyName(Long countyId, String countyName) throws CountyNotFoundException {
        if (!countyRepository.existsById(countyId)) throw new CountyNotFoundException(COUNTY_DATA_NOT_FOUND_ID);

        countyRepository.updateCountyName(countyId, countyName);
    }

    @Override
    public Park addPark(Long countyId, Park park) throws CountyNotFoundException {
        if (!countyRepository.existsById(countyId)) throw new CountyNotFoundException(COUNTY_DATA_NOT_FOUND_ID);

        park.setCountyId(countyId);
        return parkRepository.save(park);
    }

    @Override
    public County addCounty(County county) {
        return countyRepository.save(county);
    }

    @Override
    public ParkFacility addParkFacility(Long parkId, ParkFacility parkFacility) throws ParkNotFoundException {
        if (!parkRepository.existsById(parkId)) throw new ParkNotFoundException(PARK_NOT_FOUND_ID);

        parkFacility.setParkId(parkId);
        return parkFacilityRepository.save(parkFacility);
    }

    @Override
    public void updatePark(Long parkId, ParkDto parkDto) throws ParkNotFoundException {
        if (!parkRepository.existsById(parkId)) throw new ParkNotFoundException(PARK_NOT_FOUND_ID);

        parkRepository.updatePark(parkDto.getName(), parkDto.getDescription(), parkDto.getPhone(), parkDto.getEmail(), parkDto.getWebsite(), parkDto.getAccessibility(), parkDto.getDirections(), parkDto.getSurfaceType(), parkId);
    }

    @Override
    public void deleteFacility(Long facilityId) throws ParkNotFoundException {
        if (!parkFacilityRepository.existsById(facilityId)) throw new ParkNotFoundException(PARK_NOT_FOUND_ID);

        parkFacilityRepository.deleteById(facilityId);
    }

    @Override
    public void updateParkOpeningHours(Long parkOpeningHoursId, ParkOpeningHours parkOpeningHours) throws ParkNotFoundException {
        if (!parkOpeningHoursRepository.existsById(parkOpeningHoursId))
            throw new ParkNotFoundException(PARK_NOT_FOUND_ID);

        parkOpeningHoursRepository.save(parkOpeningHours);
    }

}

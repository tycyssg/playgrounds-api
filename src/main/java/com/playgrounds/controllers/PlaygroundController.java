package com.playgrounds.controllers;

import com.playgrounds.dto.ParkDto;
import com.playgrounds.exceptions.models.CountiesNotFoundException;
import com.playgrounds.exceptions.models.CountyNotFoundException;
import com.playgrounds.exceptions.models.ParkNotFoundException;
import com.playgrounds.exceptions.models.ParksDataNotFoundException;
import com.playgrounds.models.County;
import com.playgrounds.models.Park;
import com.playgrounds.models.ParkFacility;
import com.playgrounds.models.ParkOpeningHours;
import com.playgrounds.services.interfaces.PlaygroundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.zip.DataFormatException;

import static com.playgrounds.constants.ErrorConstants.INVALID_DATA_FORMAT;
import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping("/api")
public class PlaygroundController {

    private static final String PARK_DELETED_SUCCESSFULLY = "Park deleted successfully";
    private static final String COUNTY_DELETED_SUCCESSFULLY = "County deleted successfully";
    private static final String FACILITY_DELETED_SUCCESSFULLY = "Facility deleted successfully";
    private static final String COUNTY_UPDATE_SUCCESSFULLY = "County updated successfully";
    private static final String PARK_UPDATE_SUCCESSFULLY = "Park updated successfully";
    private final PlaygroundService playgroundService;

    @Autowired
    public PlaygroundController(PlaygroundService playgroundService) {
        this.playgroundService = playgroundService;
    }


    @GetMapping("/listCounties")
    public ResponseEntity<List<County>> getAllCounties() throws CountiesNotFoundException {
        return new ResponseEntity<>(playgroundService.getCountiesWithParks(), OK);
    }

    @GetMapping("/getCounty")
    public ResponseEntity<County> getCounty(@RequestParam("countyName") String countyName) throws CountyNotFoundException {
        return new ResponseEntity<>(playgroundService.getSpecificCounty(countyName), OK);
    }

    @GetMapping("/getParksByCountyId/{countyId}")
    public ResponseEntity<List<Park>> getParksOfSpecificCounty(@PathVariable("countyId") Long countyId) throws ParksDataNotFoundException {
        return new ResponseEntity<>(playgroundService.getParksOfSpecificCounty(countyId), OK);
    }

    @GetMapping("/getParkById/{parkId}")
    public ResponseEntity<Park> getParkById(@PathVariable("parkId") Long parkId) throws ParkNotFoundException {
        return new ResponseEntity<>(playgroundService.getParkById(parkId), OK);
    }

    @GetMapping("/getParkByName")
    public ResponseEntity<Park> getParkByName(@RequestParam("parkName") String parkName) throws ParkNotFoundException {
        return new ResponseEntity<>(playgroundService.getParkByName(parkName), OK);
    }


    @PostMapping("/addCounty")
    @PreAuthorize("hasAnyAuthority('user:create')")
    public ResponseEntity<County> addCounty(@Valid @RequestBody County county, BindingResult bindingResult) throws DataFormatException {
        if (bindingResult.hasErrors()) throw new DataFormatException(INVALID_DATA_FORMAT);

        return new ResponseEntity<>(playgroundService.addCounty(county), OK);
    }

    @PostMapping("/addPark/{countyId}")
    @PreAuthorize("hasAnyAuthority('user:create')")
    public ResponseEntity<Park> addPark(@PathVariable("countyId") Long countyId, @RequestBody Park park) throws CountyNotFoundException {
        return new ResponseEntity<>(playgroundService.addPark(countyId, park), OK);
    }

    @PostMapping("/addParkFacility/{parkId}")
    @PreAuthorize("hasAnyAuthority('user:create')")
    public ResponseEntity<ParkFacility> addParkFacility(@PathVariable("parkId") Long countyId, @RequestBody ParkFacility parkFacility) throws ParkNotFoundException {
        return new ResponseEntity<>(playgroundService.addParkFacility(countyId, parkFacility), OK);
    }

    @PutMapping("/updateCountyName/{countyId}")
    @PreAuthorize("hasAnyAuthority('user:update')")
    public ResponseEntity<String> getParkByName(@PathVariable("countyId") Long countyId, @RequestParam String countyName) throws CountyNotFoundException {
        playgroundService.updateCountyName(countyId, countyName);
        return new ResponseEntity<>(COUNTY_UPDATE_SUCCESSFULLY, OK);
    }

    @PutMapping("/updatePark/{parkId}")
    @PreAuthorize("hasAnyAuthority('user:update')")
    public ResponseEntity<String> updatePark(@PathVariable("parkId") Long parkId, @RequestBody ParkDto parkDto) throws ParkNotFoundException {
        playgroundService.updatePark(parkId, parkDto);
        return new ResponseEntity<>(PARK_UPDATE_SUCCESSFULLY, OK);
    }

    @PutMapping("/updateParkOpeningHours/{parkOpeningHoursId}")
    @PreAuthorize("hasAnyAuthority('user:update')")
    public ResponseEntity<String> updateParkOpeningHours(@PathVariable("parkOpeningHoursId") Long parkOpeningHoursId, @RequestBody ParkOpeningHours parkOpeningHours) throws ParkNotFoundException {
        playgroundService.updateParkOpeningHours(parkOpeningHoursId, parkOpeningHours);
        return new ResponseEntity<>(PARK_UPDATE_SUCCESSFULLY, OK);
    }

    @DeleteMapping("/deleteCounty/{countyId}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<String> deleteCounty(@PathVariable("countyId") Long countyId) throws CountyNotFoundException {
        playgroundService.deleteCounty(countyId);
        return ResponseEntity.ok(COUNTY_DELETED_SUCCESSFULLY);
    }

    @DeleteMapping("/deletePark/{parkId}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<String> deletePark(@PathVariable("parkId") Long parkId) throws ParkNotFoundException {
        playgroundService.deletePark(parkId);
        return ResponseEntity.ok(PARK_DELETED_SUCCESSFULLY);
    }

    @DeleteMapping("/deleteFacility/{facilityId}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<String> deleteFacility(@PathVariable("facilityId") Long facilityId) throws ParkNotFoundException {
        playgroundService.deleteFacility(facilityId);
        return ResponseEntity.ok(FACILITY_DELETED_SUCCESSFULLY);
    }

}

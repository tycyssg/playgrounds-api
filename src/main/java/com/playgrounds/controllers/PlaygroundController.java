package com.playgrounds.controllers;

import com.playgrounds.exceptions.models.CountiesNotFoundException;
import com.playgrounds.exceptions.models.CountyNotFoundException;
import com.playgrounds.exceptions.models.ParkNotFoundException;
import com.playgrounds.exceptions.models.ParksDataNotFoundException;
import com.playgrounds.models.County;
import com.playgrounds.models.Park;
import com.playgrounds.services.interfaces.PlaygroundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping("/api")
public class PlaygroundController {

    private static final String PARK_DELETED_SUCCESSFULLY = "Park deleted successfully";
    private static final String COUNTY_DELETED_SUCCESSFULLY = "County deleted successfully";
    private static final String COUNTY_UPDATE_SUCCESSFULLY = "County updated successfully";
    private final PlaygroundService playgroundService;

    @Autowired
    public PlaygroundController(PlaygroundService playgroundService) {
        this.playgroundService = playgroundService;
    }


    @GetMapping("/listCounties")
    public ResponseEntity<List<County>> getAllCounties() throws CountiesNotFoundException {
        return new ResponseEntity<>(playgroundService.getCountiesWithParks(), OK);
    }

    @GetMapping("/getCounty/{countyName}")
    public ResponseEntity<County> getCounty(@PathVariable("countyName") String countyName) throws CountyNotFoundException {
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

    @GetMapping("/getParkByName/{parkName}")
    public ResponseEntity<Park> getParkByName(@PathVariable("parkName") String parkName) throws ParkNotFoundException {
        return new ResponseEntity<>(playgroundService.getParkByName(parkName), OK);
    }

    @PutMapping("/updateCountyName/{countyId}")
    @PreAuthorize("hasAnyAuthority('user:update')")
    public ResponseEntity<?> getParkByName(@PathVariable("countyId") Long countyId, @RequestParam String countyName) throws CountyNotFoundException {
        playgroundService.updateCountyName(countyId, countyName);
        return new ResponseEntity<>(COUNTY_UPDATE_SUCCESSFULLY, OK);
    }

    @DeleteMapping("/delete/{countyId}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<?> deleteCounty(@PathVariable("countyId") Long countyId) throws CountyNotFoundException {
        playgroundService.deleteCounty(countyId);
        return ResponseEntity.ok(COUNTY_DELETED_SUCCESSFULLY);
    }

    @DeleteMapping("/delete/{parkId}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<?> deletePark(@PathVariable("parkId") Long parkId) throws ParkNotFoundException {
        playgroundService.deletePark(parkId);
        return ResponseEntity.ok(PARK_DELETED_SUCCESSFULLY);
    }

}

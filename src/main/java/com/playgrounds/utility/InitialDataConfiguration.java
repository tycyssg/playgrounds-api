package com.playgrounds.utility;


import com.playgrounds.dto.XmlDto;
import com.playgrounds.enumeration.Options;
import com.playgrounds.html.HtmlParsing;
import com.playgrounds.models.*;
import com.playgrounds.repository.CountyRepository;
import com.playgrounds.xml.XmlParsing;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Configuration
public class InitialDataConfiguration {

    private static final Logger log = LoggerFactory.getLogger(InitialDataConfiguration.class);

    private final XmlParsing xmlParsing;
    private final HtmlParsing htmlParsing;
    private final CountyRepository countyRepository;

    @Autowired
    public InitialDataConfiguration(XmlParsing xmlParsing, HtmlParsing htmlParsing, CountyRepository countyRepository) {
        this.xmlParsing = xmlParsing;
        this.htmlParsing = htmlParsing;
        this.countyRepository = countyRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void saveParsedDataIntoDb() {
        log.info("XML Parsing Data Starting.");
        List<XmlDto> xmlData = xmlParsing.parse();
        log.info("Saving XML Data in DB Starting.");
        saveXmlData(xmlData);
    }


    private void saveXmlData(List<XmlDto> xmlData) {
        String COUNTY_NAME = "Fingal County Council";
        County county = new County();
        county.setCounty(COUNTY_NAME);
        List<Park> parksToSave = new ArrayList<>();

        for (XmlDto data : xmlData) {
            boolean existByName = parksToSave.stream().noneMatch(p -> p.getName().equals(data.getName()));
            if (!existByName) continue;

            Park parkToSave = new Park();
            parkToSave.setName(data.getName());
            parkToSave.setPhone(data.getPhone());
            parkToSave.setEmail(data.getEmail());
            parkToSave.setWebsite(data.getWebsite());
            parkToSave.setDirections(data.getDirections());
            parkToSave.setSurfaceType(data.getSurfaceType());

            parkToSave.setParkOpeningHours(parseStringToOpenHours(data.getOpeningHours()));
            parkToSave.setParkAddress(new ParkAddress(
                    null,
                    data.getAddress1() + " " + data.getAddress2() + " " + data.getAddress3() + " " + data.getAddress4(),
                    null
            ));
            parkToSave.setParkFacilities(getParkFacility(data));
            parksToSave.add(parkToSave);
        }

        county.setParks(parksToSave);
        try {
            countyRepository.save(county);
            log.info("XML Data has been successfully saved!");
        } catch (Exception e) {
            log.error("XML Data has not been saved");
        }

    }


    private List<ParkFacility> getParkFacility(XmlDto data) {
        ParkFacility parkFacility = new ParkFacility();
        parkFacility.setType(data.getType());
        parkFacility.setCategory(data.getCategory());
        parkFacility.setParkRanger(data.getParkRanger());
        parkFacility.setAccessiblePlayItems(data.getAccessiblePlayItems());


        if (!data.getDrinkingWater().equals("")) {
            parkFacility.setDrinkingWater(Options.valueOf(data.getDrinkingWater().toUpperCase()));
        } else {
            parkFacility.setDrinkingWater(Options.NO);
        }

        if (!data.getToilets().equals("")) {
            parkFacility.setToilets(Options.valueOf(data.getToilets().toUpperCase()));
        } else {
            parkFacility.setToilets(Options.NO);
        }


        return Collections.singletonList(parkFacility);
    }

    private List<ParkOpeningHours> parseStringToOpenHours(String openHoursData) {
        if (StringUtils.isEmpty(openHoursData)) return new ArrayList<>();

        if (!StringUtils.startsWith(openHoursData, "November")) {
            return Collections.singletonList(new ParkOpeningHours(
                    null, null, null, openHoursData, null
            ));
        }

        String replacedData = openHoursData.replaceAll("[\n,:]", "");
        String[] parsedData = StringUtils.normalizeSpace(replacedData).split("(?<=pm)");

        List<ParkOpeningHours> parkOpeningHours = new ArrayList<>();
        for (String s : parsedData) {
            String[] splitData = s.split("(?<=[a-zA-Z,\\s])(?=\\d)", 2);
            parkOpeningHours.add(new ParkOpeningHours(null, null, splitData[0], StringUtils.EMPTY, splitData[1]));
        }

        return parkOpeningHours;
    }
}

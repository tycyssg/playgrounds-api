package com.playgrounds.utility;


import com.playgrounds.dto.HtmlAddress;
import com.playgrounds.dto.HtmlDto;
import com.playgrounds.dto.HtmlOpeningHours;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


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
    public void saveParsedDataIntoDb() throws IOException {
        log.info("XML Parsing Data Starting.");
        List<XmlDto> xmlData = xmlParsing.parse();
        log.info("Saving XML Data in DB Starting.");
        // saveXmlData(xmlData);
        log.info("Html Parsing Data Starting.");
        List<HtmlDto> htmlData = htmlParsing.htmlParse();
        log.info("Saving Hml Data in DB Starting.");
        //saveHtmlData(htmlData);
    }

    private void saveHtmlData(List<HtmlDto> htmlData) {
        if (htmlData.isEmpty()) {
            log.error("Hml Data could not be extracted");
            return;
        }

        String COUNTY_NAME = "Dublin City Council ";
        County county = new County();
        county.setCounty(COUNTY_NAME);
        List<Park> parksToSave = new ArrayList<>();

        for (HtmlDto data : htmlData) {
            Park parkToSave = new Park();
            parkToSave.setName(data.getTitle());
            parkToSave.setDescription(data.getDescription());
            parkToSave.setPhone(data.getPhone());
            parkToSave.setEmail(data.getEmail());
            parkToSave.setAccessibility(data.getAccessibility());
            parkToSave.setParkOpeningHours(parseHtmlOpenHoursToOpenHours(data.getOpeningHours()));
            parkToSave.setParkFacilities(getParkFacility(data.getFacilities()));
            parkToSave.setParkAddress(parseHtmlAddressToParkAddress(data.getAddress()));
            parksToSave.add(parkToSave);
        }

        county.setParks(parksToSave);
        try {
            countyRepository.save(county);
            log.info("Html Data has been successfully saved!");
        } catch (Exception e) {
            log.error("Html Data has not been saved");
        }

    }

    private ParkAddress parseHtmlAddressToParkAddress(HtmlAddress htmlAddress) {
        return new ParkAddress(
                null,
                htmlAddress.getAddress1() + " " +
                        htmlAddress.getAddress2() + " " +
                        htmlAddress.getDependentLocality() + " " +
                        htmlAddress.getLocality() + " " +
                        htmlAddress.getAdministrativeArea() + " " +
                        htmlAddress.getCountry(),
                htmlAddress.getPostalCode()
        );
    }

    private List<ParkFacility> getParkFacility(List<String> facilities) {
        return facilities.stream().map(f -> new ParkFacility(f, "Park", Options.NO, Options.NO)).collect(Collectors.toList());
    }

    private List<ParkOpeningHours> parseHtmlOpenHoursToOpenHours(List<HtmlOpeningHours> htmlOpeningHours) {
        return htmlOpeningHours.stream().map(hoh -> new ParkOpeningHours(null, null, null, hoh.getDay(), hoh.getTime())).collect(Collectors.toList());
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

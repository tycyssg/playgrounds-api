package com.playgrounds.html;

import com.playgrounds.dto.HtmlAddress;
import com.playgrounds.dto.HtmlDto;
import com.playgrounds.dto.HtmlOpeningHours;
import lombok.NoArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class HtmlParsing {

    public List<HtmlDto> htmlParse() throws IOException {
        return getAllParksUrl().stream().map(this::getDetailsOfSpecificPark).collect(Collectors.toList());
    }

    private List<String> getAllParksUrl() throws IOException {
        int LAST_PAGE_VALUE = 4;
        String URL = "https://www.dublincity.ie/residential/parks/dublin-city-parks/visit-park?keys=&amp%3Bfacilities=All&amp%3Bpage=4&facilities=All&page=";
        List<String> parkUrls = new ArrayList<>();

        for (int i = 0; i < LAST_PAGE_VALUE; i++) {
            Document doc = Jsoup.connect(URL + i)
                    .data("query", "Java")
                    .userAgent("Mozilla")
                    .cookie("auth", "token")
                    .timeout(99999)
                    .post();

            Elements divs = doc.select("div.views-row");

            for (Element e : divs) {
                Element link = e.select("a").first();
                parkUrls.add(link.absUrl("href"));
            }
        }
        return parkUrls;
    }

    private HtmlDto getDetailsOfSpecificPark(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url)
                    .data("query", "Java")
                    .userAgent("Mozilla")
                    .cookie("auth", "token")
                    .timeout(99999)
                    .post();
        } catch (Exception ignored) {

        }

        if (doc == null) {
            return null;
        }

        String title = doc.select("h1.full__title > span.field--name-title").first().text();

        Element descriptionElement = doc.select("div.full__introduction > div > div > div > p").first();
        String description = "";
        if (descriptionElement != null) {
            description = descriptionElement.text();
        }

        Element openingTimesWeek = doc.select("table.opening-times__week").first();
        List<HtmlOpeningHours> htmlOpeningHours = new ArrayList<>();
        if (openingTimesWeek != null) {
            int counter = 1;
            while (counter < 8) {
                Element dayOpeningHour = openingTimesWeek.select("tr.opening-times-week-" + counter).first();
                htmlOpeningHours.add(new HtmlOpeningHours(
                        dayOpeningHour.select("th").first().text(),
                        dayOpeningHour.select("td").first().text()
                ));
                counter++;
            }
        }

        Element phoneElement = doc.select("div.location__contact-details-phone").first();
        String phone = "";
        if (phoneElement != null) {
            phone = phoneElement.text();
        }

        Element elementEmail = doc.select("div.location__contact-details-email").first();
        String email = "";
        if (elementEmail != null) {
            String cfCode = elementEmail.select("a > span").first().attr("data-cfemail");
            email = cfDecodeEmail(cfCode);
        }

        Elements facilitiesUl = doc.select("div.full__facilities > ul > li");
        List<String> facilities = new ArrayList<>();
        for (Element listItem : facilitiesUl) {
            facilities.add(listItem.text());
        }

        Element accessibilityElement = doc.select("div.full__accessibility > div > div > div > p").first();
        String accessibility = "";
        if (accessibilityElement != null) {
            accessibility = accessibilityElement.text();
        }

        Elements address = doc.select("div.location__address > div > div > div > p > span");
        HtmlAddress parsedAddress = new HtmlAddress(
                address.select("span.address-line1").text(),
                address.select("span.address-line2").text(),
                address.select("span.dependent-locality").text(),
                address.select("span.locality").text(),
                address.select("span.administrative-area").text(),
                address.select("span.postal-code").text(),
                address.select("span.country").text()
        );

        return new HtmlDto(title, description, htmlOpeningHours, phone, email, facilities, accessibility, parsedAddress);
    }


    private String cfDecodeEmail(final String encodedString) {
        final StringBuilder email = new StringBuilder(50);
        final int r = Integer.parseInt(encodedString.substring(0, 2), 16);
        for (int n = 2; n < encodedString.length(); n += 2) {
            final int i = Integer.parseInt(encodedString.substring(n, n + 2), 16) ^ r;
            email.append(Character.toString((char) i));
        }
        return email.toString();
    }
}

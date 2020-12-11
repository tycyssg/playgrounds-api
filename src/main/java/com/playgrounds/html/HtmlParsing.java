package com.playgrounds.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class HtmlParsing {

    public void htmlParse() throws IOException {


//        List<String> parkUrls = getAllParksUrl();
//
//        for (String s : parkUrls){
//            System.out.println(s);
//        }

        getDetailsOfSpecificPark("");
    }

    private List<String> getAllParksUrl() throws IOException {
        int LAST_PAGE_VALUE = 4;
        List<String> parkUrls = new ArrayList<>();

        for (int i = 0; i < LAST_PAGE_VALUE; i++) {
            Document doc = Jsoup.connect("https://www.dublincity.ie/residential/parks/dublin-city-parks/visit-park?keys=&amp%3Bfacilities=All&amp%3Bpage=4&facilities=All&page=" + i)
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

    private void getDetailsOfSpecificPark(String url) throws IOException {
        Document doc = Jsoup.connect("https://www.dublincity.ie/residential/parks/dublin-city-parks/visit-park/albert-college-park")
                .data("query", "Java")
                .userAgent("Mozilla")
                .cookie("auth", "token")
                .timeout(99999)
                .post();

        Elements title = doc.select("h1.full__title > span.field--name-title");

        for (Element e : title) {
            System.out.println(e.text());
        }

    }


}

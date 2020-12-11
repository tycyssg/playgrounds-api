package com.playgrounds.controllers;

import com.playgrounds.dto.HtmlDto;
import com.playgrounds.dto.XmlDto;
import com.playgrounds.html.HtmlParsing;
import com.playgrounds.xml.XmlParsing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PlaygroundController {

    private final XmlParsing xmlParsing;
    private final HtmlParsing htmlParsing;

    @Autowired
    public PlaygroundController(XmlParsing xmlParsing, HtmlParsing htmlParsing) {
        this.xmlParsing = xmlParsing;
        this.htmlParsing = htmlParsing;
    }

    @GetMapping("/xml")
    public List<XmlDto> home() {
        return xmlParsing.parse();
    }

    @GetMapping("/html")
    public List<HtmlDto> parseHtml() throws IOException {
        return htmlParsing.htmlParse();
    }
}

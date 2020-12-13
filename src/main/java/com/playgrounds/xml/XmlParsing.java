package com.playgrounds.xml;

import com.playgrounds.dto.XmlDto;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
@NoArgsConstructor
public class XmlParsing {

    public List<XmlDto> parse() {
        List<XmlDto> xmlDtoList = new ArrayList<>();

        try {
            File inputFile = new File("xml-files/dublin-playgrounds.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("Play_Areas");


            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    xmlDtoList.add(
                            new XmlDto(
                                    eElement.getElementsByTagName("Name").item(0).getTextContent(),
                                    eElement.getElementsByTagName("Address1").item(0).getTextContent(),
                                    eElement.getElementsByTagName("Address2").item(0).getTextContent(),
                                    eElement.getElementsByTagName("Address3").item(0).getTextContent(),
                                    eElement.getElementsByTagName("Address4").item(0).getTextContent(),
                                    eElement.getElementsByTagName("Phone").item(0).getTextContent(),
                                    eElement.getElementsByTagName("Email").item(0).getTextContent(),
                                    eElement.getElementsByTagName("Website").item(0).getTextContent(),
                                    eElement.getElementsByTagName("Type").item(0).getTextContent(),
                                    eElement.getElementsByTagName("Category").item(0).getTextContent(),
                                    eElement.getElementsByTagName("Opening_Hours").item(0).getTextContent(),
                                    eElement.getElementsByTagName("Directions").item(0).getTextContent(),
                                    eElement.getElementsByTagName("Surface_Type").item(0).getTextContent(),
                                    eElement.getElementsByTagName("Comments").item(0).getTextContent(),
                                    eElement.getElementsByTagName("Accessible_Play_Items").item(0).getTextContent(),
                                    eElement.getElementsByTagName("Disabled_Parking").item(0).getTextContent(),
                                    eElement.getElementsByTagName("Park_Ranger").item(0).getTextContent(),
                                    eElement.getElementsByTagName("Toilets").item(0).getTextContent(),
                                    eElement.getElementsByTagName("Disabled_Toilets").item(0).getTextContent(),
                                    eElement.getElementsByTagName("Baby_Changing").item(0).getTextContent(),
                                    eElement.getElementsByTagName("Seating").item(0).getTextContent(),
                                    eElement.getElementsByTagName("Drinking_Water").item(0).getTextContent(),
                                    eElement.getElementsByTagName("LAT").item(0).getTextContent(),
                                    eElement.getElementsByTagName("LONG").item(0).getTextContent()
                            )
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return xmlDtoList;
    }
}

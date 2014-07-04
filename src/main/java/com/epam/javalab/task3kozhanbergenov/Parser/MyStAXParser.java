package com.epam.javalab.task3kozhanbergenov.Parser;

import com.epam.javalab.task3kozhanbergenov.Entity.Gem;
import com.epam.javalab.task3kozhanbergenov.Entity.Pavilion;
import com.epam.javalab.task3kozhanbergenov.Entity.VisualParameters;
import com.epam.javalab.task3kozhanbergenov.Utility.XMLValidationViaXSD;
import org.apache.log4j.Logger;

import javax.xml.stream.*;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public final class MyStAXParser {
    final static String XSD_FILE_PATH = "src/main/resources/Schema.xsd";
    static Logger log = Logger.getLogger(MyStAXParser.class.getName());

    private MyStAXParser() {
    }

    public static Pavilion parse(String xmlFile) {
        List<Gem> gemList = null;
        Gem gem = null;
        VisualParameters visualParameters = null;
        String tagContent = null;
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = null;
        try {
            StringReader stringReader = new StringReader(xmlFile);
            reader = factory.createXMLStreamReader(stringReader);
        } catch (XMLStreamException e) {
            log.error(e);
        }
        try {
            while (reader.hasNext()) {
                int event = reader.next();
                switch (event) {

                    case XMLStreamConstants.START_DOCUMENT:
                        gemList = new ArrayList<>();
                        break;

                    case XMLStreamConstants.START_ELEMENT:
                        if ("gem".equals(reader.getLocalName())) {
                            gem = new Gem();
                            visualParameters = new VisualParameters();
                            gem.setId(reader.getAttributeValue(0));
                        }
                        if ("gems".equals(reader.getLocalName())) {
                            gemList = new ArrayList<>();
                        }
                        break;

                    case XMLStreamConstants.CHARACTERS:
                        tagContent = reader.getText().trim();
                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        switch (reader.getLocalName()) {
                            case "gem":
                                gemList.add(gem);
                                break;
                            case "name":
                                gem.setName(tagContent);
                                break;
                            case "preciousness":
                                if (tagContent.equals("PRECIOUS")) {
                                    gem.setPreciousness(Gem.Preciousness.PRECIOUS);
                                } else if (tagContent.equals("SEMIPRECIOUS")) {
                                    gem.setPreciousness(Gem.Preciousness.SEMIPRECIOUS);
                                }
                                break;
                            case "origin":
                                gem.setOrigin(tagContent);
                                break;
                            case "color":
                                visualParameters.setColor(tagContent);
                                break;
                            case "transparency":
                                visualParameters.setTransparency(new Integer(tagContent));
                                break;
                            case "gemCutting":
                                visualParameters.setGemCutting(new Integer(tagContent));
                                break;
                            case "value":
                                gem.setValue(new Double(tagContent));
                                gem.setVisualParameters(visualParameters);
                                break;
                        }
                        break;
                }
            }
        } catch (XMLStreamException e) {
            log.error(e);
        }
        return new Pavilion(gemList);
    }

    public static void createXML(String filePath, Pavilion pavilion) {
        XMLOutputFactory xmlof = XMLOutputFactory.newInstance();
        try {
            XMLStreamWriter xmlw = xmlof.createXMLStreamWriter(new FileOutputStream(filePath));
            xmlw.writeStartDocument();
            xmlw.writeCharacters("\n");
            xmlw.writeStartElement("mns", "pavilion", "http://www.example.com/mns");
            xmlw.writeNamespace("mns", "http://www.example.com/mns");
            xmlw.writeNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
            xmlw.writeAttribute("xsi:schemaLocation", "http://www.example.com/mns Schema.xsd");
            xmlw.writeCharacters("\n");
            xmlw.writeStartElement("gems");
            for (Gem gem : pavilion) {
                xmlw.writeStartElement("gem");
                xmlw.writeAttribute("id", gem.getId());
                xmlw.writeStartElement("name");
                xmlw.writeCharacters(String.valueOf(gem.getName()));
                xmlw.writeEndElement();
                xmlw.writeStartElement("preciousness");
                xmlw.writeCharacters(String.valueOf(gem.getPreciousness()));
                xmlw.writeEndElement();
                xmlw.writeStartElement("origin");
                xmlw.writeCharacters(String.valueOf(gem.getOrigin()));
                xmlw.writeEndElement();
                xmlw.writeStartElement("visualParameters");
                VisualParameters visualParameters = gem.getVisualParameters();
                xmlw.writeStartElement("color");
                xmlw.writeCharacters(visualParameters.getColor());
                xmlw.writeEndElement();
                xmlw.writeStartElement("transparency");
                xmlw.writeCharacters(String.valueOf(visualParameters.getTransparency()));
                xmlw.writeEndElement();
                xmlw.writeStartElement("gemCutting");
                xmlw.writeCharacters(String.valueOf(visualParameters.getGemCutting()));
                xmlw.writeEndElement();
                xmlw.writeEndElement();
                xmlw.writeStartElement("value");
                xmlw.writeCharacters(String.valueOf(gem.getValue()));
                xmlw.writeEndElement();
                xmlw.writeEndElement();
                xmlw.writeCharacters("\n");
            }
            xmlw.writeEndElement();
            xmlw.writeEndDocument();
            xmlw.close();
        } catch (Exception e) {
            log.error(e);
        }
        log.info("Generated XML file via StAX. Is it correct? " + XMLValidationViaXSD.validate(
                filePath, XSD_FILE_PATH));
    }
}

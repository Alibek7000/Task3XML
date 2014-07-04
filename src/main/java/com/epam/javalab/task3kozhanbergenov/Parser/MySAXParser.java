package com.epam.javalab.task3kozhanbergenov.Parser;

import com.epam.javalab.task3kozhanbergenov.Entity.Gem;
import com.epam.javalab.task3kozhanbergenov.Entity.Pavilion;
import com.epam.javalab.task3kozhanbergenov.Entity.VisualParameters;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class MySAXParser extends DefaultHandler {
    static Logger log = Logger.getLogger(MySAXParser.class.getName());

    private MySAXParser() {
    }

    private Pavilion pavilion;
    private List<Gem> gems = new ArrayList<>();
    private Gem gem;
    private VisualParameters visualParameters;
    private String thisElement;

    public static Pavilion parse(File xmlAsString) {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = null;
        try {
            sp = spf.newSAXParser();
        } catch (ParserConfigurationException e) {
            log.error(e);
        } catch (SAXException e) {
            log.error(e);
        }
        MySAXParser handler = new MySAXParser();
        try {
            sp.parse(xmlAsString, handler);
        } catch (SAXException e) {
            log.error(e);
        } catch (IOException e) {
            log.error(e);
        }
        return handler.pavilion;
    }

    @Override
    public void startDocument() throws SAXException {
    }

    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes attributes) throws SAXException {
        thisElement = qName;
        if (thisElement.equals("gem")) {
            gem = new Gem();
            visualParameters = new VisualParameters();
            gem.setId(new String(attributes.getValue("id")));
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (thisElement.equals("name")) {
            gem.setName(new String(ch, start, length));
        }
        if (thisElement.equals("preciousness")) {
            if (new String(ch, start, length).equals("PRECIOUS"))
                gem.setPreciousness(Gem.Preciousness.PRECIOUS);
            else if (new String(ch, start, length).equals("SEMIPRECIOUS"))
                gem.setPreciousness(Gem.Preciousness.SEMIPRECIOUS);
        }
        if (thisElement.equals("origin")) {
            gem.setOrigin(new String(ch, start, length));
        }

        if (thisElement.equals("color")) {
            visualParameters.setColor(new String(ch, start, length));
        }

        if (thisElement.equals("transparency")) {
            visualParameters.setTransparency(new Integer(new String(ch, start, length)));
        }
        if (thisElement.equals("gemCutting")) {
            visualParameters.setGemCutting(new Integer(new String(ch, start, length)));
        }
        if (thisElement.equals("value")) {
            gem.setValue(new Double(new String(ch, start, length)));
            gem.setVisualParameters(visualParameters);
            gems.add(gem);
        }
    }

    @Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        thisElement = "";
    }

    @Override
    public void endDocument() {
        pavilion = new Pavilion(gems);
    }

}

package com.epam.javalab.task3kozhanbergenov.Runner;

import com.epam.javalab.task3kozhanbergenov.Entity.Pavilion;
import com.epam.javalab.task3kozhanbergenov.Parser.MyDOMParser;
import com.epam.javalab.task3kozhanbergenov.Parser.MyJAXB;
import com.epam.javalab.task3kozhanbergenov.Parser.MySAXParser;
import com.epam.javalab.task3kozhanbergenov.Parser.MyStAXParser;
import com.epam.javalab.task3kozhanbergenov.Utility.FileReader;
import com.epam.javalab.task3kozhanbergenov.Utility.XMLValidationViaXSD;
import org.xml.sax.SAXException;
import org.apache.log4j.Logger;

import javax.xml.parsers.*;
import java.io.File;
import java.io.IOException;

public class Main {
    private static final String XML_FILE_PATH = "src/main/resources/Pavilion.xml";
    private static final String XSD_FILE_PATH = "src/main/resources/Schema.xsd";
    private static final String StAX_XML_FILE_PATH = "src/main/resources/XMLViaStAX.xml";
    private static final String JAXB_XML_FILE_PATH = "src/main/resources/XMLViaJAXB.xml";
    static Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

        File pavilionXmlFile = new File(XML_FILE_PATH);
        String xmlAsString = FileReader.readFile(XML_FILE_PATH);

        boolean isValid = XMLValidationViaXSD.validate(XML_FILE_PATH, XSD_FILE_PATH);
        log.info("XML is validly? " + isValid);

        Pavilion pavilionViaSAXParser = MySAXParser.parse(pavilionXmlFile);
        log.info("pavilionViaSAXParser: " + pavilionViaSAXParser);

        Pavilion pavilionViaStAX = MyStAXParser.parse(xmlAsString);
        log.info("pavilionViaStAX: " + pavilionViaStAX);

        MyStAXParser.createXML(StAX_XML_FILE_PATH, pavilionViaStAX);

        Pavilion pavilionViaDOM = MyDOMParser.parse(pavilionXmlFile);
        log.info("pavilionViaDOM: " + pavilionViaDOM);

        MyJAXB.marshall(JAXB_XML_FILE_PATH, pavilionViaDOM);

    }
}

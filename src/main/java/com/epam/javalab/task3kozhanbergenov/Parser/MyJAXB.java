package com.epam.javalab.task3kozhanbergenov.Parser;

import com.epam.javalab.task3kozhanbergenov.Entity.Gem;
import com.epam.javalab.task3kozhanbergenov.Entity.Pavilion;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;

public final class MyJAXB {
    private static final String XSD_FILE_PATH = "src/main/resources/Schema.xsd";
    static Logger log = Logger.getLogger(MyDOMParser.class.getName());

    private MyJAXB() {
    }

    public static void marshall(String filePath, Pavilion pav1) {
        try {
            File file = new File(filePath);
            JAXBContext jaxbContext = JAXBContext.newInstance(Pavilion.class, Gem.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, XSD_FILE_PATH);
            jaxbMarshaller.marshal(pav1, file);
        } catch (JAXBException e) {
            log.error(e);
        }
    }
}
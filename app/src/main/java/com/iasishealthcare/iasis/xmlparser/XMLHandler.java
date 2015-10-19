package com.iasishealthcare.iasis.xmlparser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLHandler extends DefaultHandler {

    String elementValue = null;
    Boolean elementOn = false;
    Boolean item = false;

    public static XMLGettersSetters data = null;

    public static XMLGettersSetters getXMLData() {
        return data;
    }

    public static void setXMLData(XMLGettersSetters data) {
        XMLHandler.data = data;
    }

    /**
     * This will be called when the tags of the XML starts.
     **/
    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {

        elementOn = true;

        if (localName.equals("item")) {
            item = true;
        }
        else if (localName.equals("title"))
        {
            data = new XMLGettersSetters();

        } else if (localName.equals("CD")) {
            /**
             * We can get the values of attributes for eg. if the CD tag had an attribute( <CD attr= "band">Akon</CD> )
             * we can get the value "band". Below is an example of how to achieve this.
             *
             * String attributeValue = attributes.getValue("attr");
             * data.setAttribute(attributeValue);
             *
             * */
        }
    }

    /**
     * This will be called when the tags of the XML end.
     **/
    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        elementOn = false;

        /**
         * Sets the values after retrieving the values from the XML tags
         * */
        if (localName.equalsIgnoreCase("item"))
            item = false;
        else if (localName.equalsIgnoreCase("title")) {
            if (item) {
                data.setTitle(elementValue);
            }
        }
    }

    /**
     * This is called to get the tags value
     **/
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {

        if (elementOn) {
            elementValue = new String(ch, start, length);
            elementOn = false;
        }

    }

}
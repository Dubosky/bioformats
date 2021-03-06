/*
 * #%L
 * OME-XML Java library for working with OME-XML metadata structures.
 * %%
 * Copyright (C) 2006 - 2013 Open Microscopy Environment:
 *   - Massachusetts Institute of Technology
 *   - National Institutes of Health
 *   - University of Dundee
 *   - Board of Regents of the University of Wisconsin-Madison
 *   - Glencoe Software, Inc.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * The views and conclusions contained in the software and documentation are
 * those of the authors and should not be interpreted as representing official
 * policies, either expressed or implied, of any organization.
 * #L%
 */

package ome.xml.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import ome.xml.model.AbstractOMEModelObject;
import ome.xml.model.OMEModelObject;
import ome.xml.model.enums.EnumerationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author rleigh
 *
 *
 * <dl><dt><b>Source code:</b></dt>
 * <dd><a href="http://trac.openmicroscopy.org.uk/ome/browser/bioformats.git/components/bio-formats/src/loci/formats/in/MapPairs.java">Trac</a>,
 * <a href="http://git.openmicroscopy.org/?p=bioformats.git;a=blob;f=components/bio-formats/src/loci/formats/in/MapPairs.java;hb=HEAD">Gitweb</a></dd></dl>
 */
public class MapPairs implements OMEModelObject {

    /** Logger for this class. */
    private static final Logger LOGGER =
        LoggerFactory.getLogger(MapPairs.class);

    public static final String PROPERTY_FILE = "omemodel.properties";

    public static final Properties VERSION_PROPERTIES = loadProperties();

    /** Version number of this schema release. */
    public static final String VERSION = VERSION_PROPERTIES.getProperty("schema.version");

    static Properties loadProperties() {
        Properties properties = new Properties();
        try {
            InputStream propertyFile = Class.forName(
                "ome.xml.model.MapPairs").getResourceAsStream(PROPERTY_FILE);
            properties.load(propertyFile);
        }
        catch (ClassNotFoundException e) {
            LOGGER.debug("Failed to load model properties", e);
        }
        catch (IOException e) {
            LOGGER.debug("Failed to load model properties", e);
        }
        return properties;
    }

    // -- Constants --

    public static final String MAPNAMESPACE = "http://www.openmicroscopy.org/Schemas/SA/" + VERSION;
    public static final String PAIRSNAMESPACE = "http://www.openmicroscopy.org/Schemas/OME/" + VERSION;

    private Map<String, String> map;

    /** Default constructor. */
    public MapPairs()
    {
        map = new HashMap<String, String>();
    }

    /** Construct from an existing Map. */
    public MapPairs(Map<String, String> m)
    {
       map = new HashMap<String, String>(m);
    }

    /** Copy constructor. */
    public MapPairs(MapPairs orig)
    {
        map = new HashMap<String, String>(orig.getMap());
    }

    /**
     * Constructs MapPairs recursively from an XML DOM tree.
     * @param element Root of the XML DOM tree to construct a model object
     * graph from.
     * @param model Handler for the OME model which keeps track of instances
     * and references seen during object population.
     * @throws EnumerationException If there is an error instantiating an
     * enumeration during model object creation.
     */
    public MapPairs(Element element, OMEModel model)
        throws EnumerationException
    {
        update(element, model);
    }

    public Map<String, String> getMap()
    {
        return map;
    }

    public void setMap(Map<String, String> m)
    {
       map = new HashMap<String, String>(m);
    }

    public Element asXMLElement(Document document)
    {
        return asXMLElement(document, null);
    }

    protected Element asXMLElement(Document document, Element pairs)
    {
        if (pairs == null) {
            // a node named "Map" is only desired if we are working with an
            // instance of Map (a subclass of MapPairs), in which case it
            // is the subclass' responsibility to ensure that 'pairs' is
            // a node of the correct name/type
            pairs = document.createElementNS(MAPNAMESPACE, "Value");
        }

        Iterator<Map.Entry<String, String>> entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();

            Element pair = document.createElementNS(PAIRSNAMESPACE, "M");
            pair.setAttribute("K", entry.getKey());
            pair.setTextContent(entry.getValue());
            pairs.appendChild(pair);
        }

        return pairs;
    }

    public void update(Element element, OMEModel model) throws EnumerationException
    {
        String tagName = element.getTagName();
        if (!("Map".equals(tagName) || "Value".equals(tagName))) {
            LOGGER.debug("Expecting node name of Map or Value, got {}", tagName);
        }

        for(Element child : AbstractOMEModelObject.getChildrenByTagName(element, "M")) {
            if (child.hasAttribute("K")) {
                String key = child.getAttribute("K");
                String value = child.getTextContent();
                map.put(key, value);
            } else {
                LOGGER.debug("MapPairs entry M does not contain key attribute K");
            }
        }
    }

    public boolean link(Reference reference, OMEModelObject o)
    {
        return false;
    }

}

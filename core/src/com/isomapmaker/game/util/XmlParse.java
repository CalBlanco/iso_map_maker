package com.isomapmaker.game.util;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;


import org.w3c.dom.NodeList;

public class XmlParse {
    /**
     * Reads a asset xml and returns the info as a array of arrays of strings
     * e.g [ [tile_name, tile_file, tile_type, tile_flags].... ]
     * @param fp
     * @return
     */
    public static String[][] readXML(String fp){
        try {
            File inputFile = new File(fp);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
            NodeList nodeList = doc.getElementsByTagName("tile_data");

            String[][] output = new String[nodeList.getLength()][5];

            for (int temp = 0; temp < nodeList.getLength(); temp++) {
                Node node = nodeList.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    output[temp][0] = element.getElementsByTagName("name").item(0).getTextContent();
                    output[temp][1] = element.getElementsByTagName("file").item(0).getTextContent();
                    output[temp][2] = element.getElementsByTagName("type").item(0).getTextContent();
                    output[temp][3] = element.getElementsByTagName("flags").item(0).getTextContent();
                    NodeList size = element.getElementsByTagName("size");
                    if(size.getLength() >= 1) output[temp][4] = size.item(0).getTextContent();
                    else output[temp][4] = "128x64";
                }
            }

            return output;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new String[][]{};
    }
}

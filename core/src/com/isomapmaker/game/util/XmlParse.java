package com.isomapmaker.game.util;

import java.io.File;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.isomapmaker.game.map.Atlas.AtlasContainer;
import com.isomapmaker.game.map.Atlas.enums.TileType;

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


    public static HashMap<TileType, AtlasContainer> getAssets(String assetFile){
        try{
            HashMap<TileType, AtlasContainer> assets = new HashMap<TileType, AtlasContainer>();
            assets.put(TileType.Floor, new AtlasContainer());
            assets.put(TileType.Wall, new AtlasContainer());
            assets.put(TileType.Object, new AtlasContainer());

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(assetFile);
            doc.getDocumentElement().normalize();
            System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

            Node floors = doc.getElementsByTagName("Floor").item(0);
            Node walls = doc.getElementsByTagName("Wall").item(0);
            Node objects = doc.getElementsByTagName("Object").item(0);

            //Floor parsing
            Element f = (Element) floors;
            NodeList nl = f.getElementsByTagName("asset");
            for(int i=0; i<nl.getLength(); i++){
                assets.get(TileType.Floor).addAtlas(parseAssetName((Element)nl.item(i)),parseAssetToAtlas((Element)nl.item(i)));
            }

            // Wall Parsing
            Element w = (Element) walls;
            NodeList n2 = w.getElementsByTagName("asset");
            for(int i=0; i<n2.getLength(); i++){
                assets.get(TileType.Wall).addAtlas(parseAssetName((Element)n2.item(i)),parseAssetToAtlas((Element)n2.item(i)));
            }

            // Object Parsing
            Element o = (Element) objects;
            NodeList n3 = o.getElementsByTagName("asset");
            for(int i=0; i<n3.getLength(); i++){
                assets.get(TileType.Object).addAtlas(parseAssetName((Element)n3.item(i)),parseAssetToAtlas((Element)n3.item(i)));
            }

            return assets;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private static TextureAtlas parseAssetToAtlas(Element assetElement){
        
        String path = assetElement.getElementsByTagName("path").item(0).getTextContent();
        System.out.println("ParseAssetToAtlas: " + path);
        return new TextureAtlas(path);
    }

    private static String parseAssetName(Element assetElement){
        String name = assetElement.getElementsByTagName("name").item(0).getTextContent();
        System.out.println("parseAssetName: " + name);
        return name;
    }

 

    // TileType {Floor, Wall, Object}
    // WallQuadrants {Left, Right, Top, Bottom}
    // return this Hashmap<TileType, AtlasContainer> to what ever is responsible for controlling our assets
    // AtlasContainer {
    //  Hashmap<String, TextureAtlas> (has names, and the corresponding atlas for them)    
    //}
    // 

}

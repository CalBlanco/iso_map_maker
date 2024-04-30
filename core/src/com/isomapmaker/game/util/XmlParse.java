package com.isomapmaker.game.util;


import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.isomapmaker.game.map.Atlas.AssetContainer;
import com.isomapmaker.game.map.Atlas.enums.TileType;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

/**
 * Class to parse xml documents 
 */
public class XmlParse {
    
    /**
     * Using an asset file allocate the AssetContainers needed for us to interact with all of them 
     * @param assetFile
     * @return
     */
    public static HashMap<TileType, AssetContainer> getAssets(String assetFile){
        try{
            HashMap<TileType, AssetContainer> assets = new HashMap<TileType, AssetContainer>(); // our final output 
            assets.put(TileType.Floor, new AssetContainer()); // Assign one per type, If I really wanted to do this better I could just loop over TileType values 
            assets.put(TileType.Wall, new AssetContainer());
            assets.put(TileType.Object, new AssetContainer());

            // XML parsing stuff
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(assetFile);
            doc.getDocumentElement().normalize();
            
            Node floors = doc.getElementsByTagName("Floor").item(0);
            Node walls = doc.getElementsByTagName("Wall").item(0);
            Node objects = doc.getElementsByTagName("Object").item(0);

            //Floor parsing
            Element f = (Element) floors;
            NodeList nl = f.getElementsByTagName("asset");
            for(int i=0; i<nl.getLength(); i++){
                Element e = (Element) nl.item(i);
                assets.get(TileType.Floor).addTextureAtlas(parseAssetName(e), TileType.Floor, e.getAttribute("uid") ,parseAssetToAtlas(e));
            }

            // Wall Parsing
            Element w = (Element) walls;
            NodeList n2 = w.getElementsByTagName("asset");
            for(int i=0; i<n2.getLength(); i++){
                Element e = (Element) n2.item(i);
                assets.get(TileType.Wall).addTextureAtlas(parseAssetName(e), TileType.Wall, e.getAttribute("uid") ,parseAssetToAtlas(e));
            }

            // Object Parsing
            Element o = (Element) objects;
            
            NodeList n3 = o.getElementsByTagName("asset");
            for(int i=0; i<n3.getLength(); i++){
                Element e = (Element) n3.item(i);
                assets.get(TileType.Object).addTextureAtlas(parseAssetName(e), TileType.Object, e.getAttribute("uid") ,parseAssetToAtlas(e));
            }

            return assets;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get the TextureAtlas we need from the file 
     * @param assetElement
     * @return
     */
    private static TextureAtlas parseAssetToAtlas(Element assetElement){
        
        String path = assetElement.getElementsByTagName("path").item(0).getTextContent();
        //System.out.println("ParseAssetToAtlas: " + path);
        return new TextureAtlas(path);
    }

    /**
     * Get the name of the asset for ui display 
     * @param assetElement
     * @return
     */
    private static String parseAssetName(Element assetElement){
        String name = assetElement.getElementsByTagName("name").item(0).getTextContent();
        //System.out.println("parseAssetName: " + name);
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

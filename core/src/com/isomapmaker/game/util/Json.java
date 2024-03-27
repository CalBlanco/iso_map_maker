package com.isomapmaker.game.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class Json {
    public static JsonValue readJson(String file){
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line; // iterate over lines
            String total = "";
            while((line = reader.readLine()) != null){ // assign and check
                total += line;
            }

            JsonValue root = new JsonReader().parse(total);
            
            System.out.println(root.name());

            return root;
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    } 
}

package com.isomapmaker.game.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.isomapmaker.game.util.IsoUtil;

/**
     * Struct for managing texture data
     */
    public class TextureData{
        public TextureRegion tr;
        public String name;
        public Vector2 size;
        public Vector2 pos;
        public Vector2 tilePos;
        public int selection;
        public boolean solid;
        public TextureData(TextureRegion tr, String name, String size_str, int row, int col, int sel, boolean solid){
            this.tr = tr;
            this.name = name;
            int width,height;
            this.selection = sel;
            width = Integer.parseInt(size_str.split("x",2)[0]);
            height = Integer.parseInt(size_str.split("x",2)[1]);
            tilePos = new Vector2(row,col);
            this.size = new Vector2(width,height);
            this.pos = IsoUtil.worldToIsometric(tilePos, size);
            this.solid = solid;
        }

               
    }
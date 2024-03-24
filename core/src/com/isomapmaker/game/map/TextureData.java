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
        public Vector2 tilePos;
        public int selection;
        public boolean solid;
        public TextureData(TextureRegion tr, String name, String size_str, int row, int col, int sel, boolean solid){
            this.tr = tr;
            this.name = name;
            this.selection = sel;
            tilePos = new Vector2(row,col);
            this.solid = solid;
        }

        public Vector2 getPos(){
            return IsoUtil.worldToIsometric(tilePos, IsoUtil.FLOOR_SIZE);
        }

               
    }
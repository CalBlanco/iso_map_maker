package com.isomapmaker.game.controls.commands;

import java.util.Vector;

import com.badlogic.gdx.math.Vector2;
import com.isomapmaker.game.controls.PaintTools;
import com.isomapmaker.game.map.Assets.Asset;
import com.isomapmaker.game.map.Assets.Floor;
import com.isomapmaker.game.map.TileMaps.TileLoader;
import com.isomapmaker.game.map.TileMaps.TileMap;


public class LineCommand extends Command{
    Vector2 clickPos, endPos;
    Asset floor;

    public LineCommand(Vector2 clickPos, Vector2 endPos, Asset floor , TileMap map) {
        super(map);
        this.clickPos = clickPos;
        this.endPos = endPos;
        this.floor = floor;
    }

    @Override
    public void executeCommand() {
        line();
    }

    private boolean line(){
        Vector<Integer[]> l = PaintTools.line(clickPos, endPos);
        

        for(int i = 0; i<l.size(); i++){
            map.setFloor(l.get(i)[0], l.get(i)[1],(Asset) floor);
        }
        return true;
    }
    
}

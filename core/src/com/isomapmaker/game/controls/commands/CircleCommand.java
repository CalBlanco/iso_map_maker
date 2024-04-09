package com.isomapmaker.game.controls.commands;

import java.util.Vector;

import com.badlogic.gdx.math.Vector2;
import com.isomapmaker.game.controls.ModeController;
import com.isomapmaker.game.controls.PaintTools;
import com.isomapmaker.game.map.Assets.Asset;
import com.isomapmaker.game.map.Assets.Floor;
import com.isomapmaker.game.map.Atlas.enums.TileType;
import com.isomapmaker.game.map.TileMaps.TileMap;


public class CircleCommand extends Command{
    int x0, y0, r;
    Asset floor;

    public CircleCommand(int x0, int y0, int r, Asset floor, TileMap map) {
        super(map);
        this.x0 = x0;
        this.y0 = y0;
        this.r = r;
        this.floor = floor;
        
    }

    @Override
    public void executeCommand() {
        circle();
    }
    

    private boolean circle(){
        Vector<Integer[]> c = PaintTools.circle(x0,y0,r);
        


        for(int i = 0; i<c.size(); i++){
            map.setFloor(c.get(i)[0], c.get(i)[1], (Asset) floor);
        }
        return true;
    }

}

package com.isomapmaker.game.controls.commands;

import java.util.Vector;

import com.badlogic.gdx.math.Vector2;
import com.isomapmaker.game.controls.PaintTools;
import com.isomapmaker.game.map.TileMaps.TileLoader;
import com.isomapmaker.game.map.TileMaps.TileMap;
import com.isomapmaker.game.map.Tiles.Floor;

public class CircleCommand extends Command{
    int x0, y0, r;
    Floor floor;

    public CircleCommand(int x0, int y0, int r, Floor floor, TileLoader loader, TileMap map) {
        super(loader, map);
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
            map.setFloor(c.get(i)[0], c.get(i)[1], floor);
        }
        return true;
    }

}

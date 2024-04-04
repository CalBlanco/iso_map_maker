package com.isomapmaker.game.controls.commands;

import java.util.Vector;

import com.badlogic.gdx.math.Vector2;
import com.isomapmaker.game.controls.PaintTools;
import com.isomapmaker.game.map.TileMaps.TileLoader;
import com.isomapmaker.game.map.TileMaps.TileMap;
import com.isomapmaker.game.map.Tiles.Floor;

public class LineCommand extends Command{
    Vector2 clickPos, endPos;
    Floor floor;

    public LineCommand(Vector2 clickPos, Vector2 endPos, Floor floor ,TileLoader loader, TileMap map) {
        super(loader, map);
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
        map.setSelection(l);

        for(int i = 0; i<l.size(); i++){
            map.setFloor(l.get(i)[0], l.get(i)[1], floor);
        }
        return true;
    }
    
}

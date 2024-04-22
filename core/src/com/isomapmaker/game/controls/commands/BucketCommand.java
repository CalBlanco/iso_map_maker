package com.isomapmaker.game.controls.commands;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import com.isomapmaker.game.controls.ModeController;
import com.isomapmaker.game.map.Assets.Asset;
import com.isomapmaker.game.map.Assets.Floor;
import com.isomapmaker.game.map.Assets.Tile;
import com.isomapmaker.game.map.Assets.TileDelta;
import com.isomapmaker.game.map.Atlas.enums.TileType;
import com.isomapmaker.game.map.TileMaps.TileMap;

/**
 * Use the FloodFill alg to replace floor tiles (Does not work on objects or walls nor do I want or intend it to )
 */
public class BucketCommand extends Command {
    int x0,y0;
    Asset newFloor;
    private class Point{
        public int x,y;
        public Point(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

    private static final int[] bucket_row = { -1, 0, 1, 0 };
    private static final int[] bucket_col = { 0, 1, 0, -1};
 

    public BucketCommand(int x0, int y0, Asset newFloor , TileMap map){
        super(map);
        this.x0 = x0;
        this.y0 = y0;
        this.newFloor = newFloor;
    }

    @Override
    public void executeCommand() {
       
        bucket();
        
    }

    private boolean isBuckatable(int x, int y, Asset oldFloor, Asset newFloor){
        if (map.getFloor(x,y) != null && map.getFloor(x,y).getName() == newFloor.getName()) return false;
        if (map.inBounds(x, y) && (map.getFloor(x, y) == null || (oldFloor != null && oldFloor.getName() == map.getFloor(x,y).getName())) ) return true;
        return false;
    }

    private boolean bucket(){
        if(ModeController.getInstance().getAssetState() != TileType.Floor) return false;
        Vector<Point> queue = new Vector<Point>(); // queue for points
        Set<String> visited = new HashSet<String>();


        Floor oldFloor = map.getFloor(x0, y0);
        queue.add(new Point(x0, y0)); // add our first point 
        while(queue.size() > 0){
            Point p = queue.remove(queue.size()-1);

            if(visited.contains(p.x+"x"+p.y)) continue; // dont do visited tiles 
            visited.add(p.x+"x"+p.y);

            Tile oldTile = new Tile(map.getTile(p.x, p.y)); // store the copy of original 

            
            map.setFloor(p.x,p.y, newFloor); // change the tile 

            Tile newTile = new Tile(map.getTile(p.x,p.y)); //store edited 
            TileDelta delta = new TileDelta(p.x, p.y, oldTile, newTile); // create and add delta 
            this.deltas.add(delta);
            
            for(int k=0; k<bucket_row.length; k++){ // add bucketable points to our queue
                if(isBuckatable(p.x+bucket_row[k], p.y+bucket_col[k], oldFloor, newFloor)){
                    queue.add(new Point(p.x+bucket_row[k], p.y+bucket_col[k]));
                }
            }

            
        }

        return true;
    }
    
}

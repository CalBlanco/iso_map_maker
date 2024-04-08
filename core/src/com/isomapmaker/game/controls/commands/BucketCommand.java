package com.isomapmaker.game.controls.commands;

import java.util.Vector;

import com.isomapmaker.game.map.Assets.Asset;
import com.isomapmaker.game.map.Assets.Floor;
import com.isomapmaker.game.map.TileMaps.TileLoader;
import com.isomapmaker.game.map.TileMaps.TileMap;


public class BucketCommand extends Command {
    int x0,y0;
    Asset newFloor;

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
        
        Vector<Integer[]> queue = new Vector<Integer[]>(); // queue for points
        Floor oldFloor = map.getFloor(x0, y0);
        queue.add(new Integer[]{x0, y0}); // add our first point 
        while(queue.size() > 0){
            Integer[] p = queue.get(queue.size()-1);
           
            queue.remove(queue.size()-1);

            map.setFloor(p[0],p[1], newFloor);

            for(int k=0; k<bucket_row.length; k++){
                if(isBuckatable(p[0]+bucket_row[k], p[1]+bucket_col[k], oldFloor, newFloor)){
                    queue.add(new Integer[]{p[0]+bucket_row[k], p[1]+bucket_col[k]});
                }
            }
        }

        return true;
    }
    
}

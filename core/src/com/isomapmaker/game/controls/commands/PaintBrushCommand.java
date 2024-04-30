package com.isomapmaker.game.controls.commands;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import com.badlogic.gdx.math.Vector2;
import com.isomapmaker.game.controls.ModeController;
import com.isomapmaker.game.controls.PaintTools;
import com.isomapmaker.game.map.Assets.Tile;
import com.isomapmaker.game.map.Assets.TileDelta;
import com.isomapmaker.game.map.Atlas.enums.TileType;
import com.isomapmaker.game.map.TileMaps.TileMap;
import com.isomapmaker.game.util.IsoUtil;

public class PaintBrushCommand extends Command {
    Set<String> points;
    int radius;
    TileType mode;
    public PaintBrushCommand(TileMap map, Set<String> points) {
        super(map);
        //TODO Auto-generated constructor stub
        this.points = points;
        this.radius = ModeController.getInstance().getBrushSize();
        mode = ModeController.getInstance().getAssetState();
    }

    String[] brushPointSplit = new String[2];
    Vector2 brushVector = new Vector2(0,0);
    public void paint(){
        Set<String> visited = new HashSet<String>();

        for(String p: points){
            brushPointSplit = p.split("x",2);
            brushVector.set(Float.parseFloat(brushPointSplit[0]), Float.parseFloat(brushPointSplit[1]));

            //generate circle at point based on brush size
            Vector<Integer[]> circlePoints = PaintTools.circle(brushVector, ModeController.getInstance().getBrushSize());
            int uniquePoints = circlePoints.size() / 4; // get unique number of points 

            TileType mode = ModeController.getInstance().getAssetState();
            for(int i=0; i< uniquePoints; i++){
                
                // want a line from points [(0+4*i)] to 1+4*i and 2+4*i to 3+4*i to fill circle
                int p1 = 0+4*i;
                int p2 = 1+4*i;
                int p3 = 2+4*i;
                int p4 = 3+4*i;
                Vector<Integer[]> topLine = PaintTools.line(circlePoints.get(p1)[0], circlePoints.get(p1)[1], circlePoints.get(p2)[0], circlePoints.get(p2)[1]);
                Vector<Integer[]> bottomLine = PaintTools.line(circlePoints.get(p3)[0], circlePoints.get(p3)[1], circlePoints.get(p4)[0], circlePoints.get(p4)[1]);


                for(int linePoint = 0; linePoint < topLine.size(); linePoint++){
                    int x1,y1,x2,y2;
                    x1 = topLine.get(linePoint)[0];
                    y1 = topLine.get(linePoint)[1];
                    x2 = bottomLine.get(linePoint)[0];
                    y2 = bottomLine.get(linePoint)[1];
                    
                    if(!visited.contains(x1+"x"+y1)){
                        Tile oldTile1 = map.getTile(x1, y1) == null ? null : new Tile(map.getTile(x1, y1));
                        switch(mode){
                            case Floor:
                                map.setFloor(x1, y1, ModeController.getInstance().getActiveAsset());
                            break;
                            case Wall:
                                map.setWall(x1, y1, ModeController.getInstance().getQuadrant(), ModeController.getInstance().getActiveAsset());
                            break;
                            case Object:
                                map.setObject(x1, y1, ModeController.getInstance().getActiveAsset());
                            break;
                        }
                        Tile newTile1 = new Tile(map.getTile(x1, y1));

                        TileDelta td1 = new TileDelta(x1, y1, oldTile1, newTile1);
                        this.deltas.add(td1);
                        visited.add(x1+"x"+y1);
                    }


                    if(!visited.contains(x2+"x"+y2)){
                        Tile oldTile2 = map.getTile(x2, y2) == null ? null : new Tile(map.getTile(x2, y2));
                        switch (mode) {
                            case Floor:
                                map.setFloor(x2, y2, ModeController.getInstance().getActiveAsset());
                                break;
                            case Wall:
                                map.setWall(x2, y2, ModeController.getInstance().getQuadrant(), ModeController.getInstance().getActiveAsset());
                                break;
                            case Object:
                                map.setObject(x2, y2, ModeController.getInstance().getActiveAsset());
                                break;
                            default:
                                break;
                        }
                        Tile newTile2 = new Tile(map.getTile(x2, y2));

                        TileDelta td2 = new TileDelta(x2, y2, oldTile2, newTile2);
                        this.deltas.add(td2);
                        visited.add(x2+"x"+y2);
                    }   

                }
            }
        }
    }

    @Override
    public void executeCommand() {
        paint();
    }
    
}

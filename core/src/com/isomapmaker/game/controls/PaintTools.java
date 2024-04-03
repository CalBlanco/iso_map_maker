package com.isomapmaker.game.controls;

import java.util.Vector;

import com.badlogic.gdx.math.Vector2;

public class PaintTools {
    // 0001 = 1
    // 0010 = 2
    // 0100 = 4
    // 1000 = 8
    /**
     * Using Bresenhams line algo for all quads return a vector adapted in Technikum-Wien 2012 http://members.chello.at/~easyfilter/Bresenham.pdf 
     * @param start
     * @param end
     * @return
     */
    private static void savePoint(int x, int y, Vector<Integer[]> l){
        l.add(new Integer[]{x,y});
    }

    public static Vector<Integer[]> line(int[] start, int[] end){
        int x0, y0, x1, y1;
        x0 = start[0];
        y0 = start[1];
        x1 = end[0];
        y1 = end[1];

        System.out.println("Line from (" + x0 +"," + y0+"), to (" + x1 +"," +y1+")\n");
        Vector<Integer[]> points = new Vector<Integer[]>();
        
        int dx = Math.abs(x1-x0);
        int sx = x0 < x1 ? 1 : -1;
        int dy = -Math.abs(y1-y0);
        int sy = y0 < y1 ? 1 : -1;
        int error = dx + dy;
        int e2;

        while (true){
            savePoint(x0,y0, points);
            e2 = 2*error;

            if(e2 >= dy){
                if (x0 == x1) break;
                error += dy;
                x0 += sx;
            }
            if( e2 <= dx){
                if (y0 == y1) break;
                error += dx;
                y0 += sy;
            }

        }

        return points;
    }
    public static Vector<Integer[]> line(Vector2 start, Vector2 end){
        return line(new int[]{(int)start.x, (int)start.y}, new int[]{(int)end.x, (int)end.y});
    }
    public static Vector<Integer[]> line(int x0, int y0, int x1, int y1){
        return line(new int[]{x0,y0}, new int[]{x1,y1});
    }
}

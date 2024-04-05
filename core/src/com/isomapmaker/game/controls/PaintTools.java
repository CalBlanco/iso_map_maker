package com.isomapmaker.game.controls;

// Most of this is pulled from http://members.chello.at/~easyfilter/Bresenham.pdf

import java.util.Vector;

import com.badlogic.gdx.math.Vector2;

public class PaintTools {
    
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

       //System.out.println("Line from (" + x0 +"," + y0+"), to (" + x1 +"," +y1+")\n");
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


    public static Vector<Integer[]> circle(int x0, int y0, int r){
        return circle(new Vector2(x0,y0), r);
    }
    public static Vector<Integer[]> circle(Vector2 start, int radius){
        Vector<Integer[]> points = new Vector<Integer[]>();
        int x, y, err;
        x = -radius;
        y = 0;
        err = 2-2*radius;
        int xm, ym;
        xm = (int) start.x;
        ym = (int) start.y;

        do {
            savePoint(xm-x, ym+y, points); // +x,+y
            savePoint(xm-y, ym-x, points); // -x,+y
            savePoint(xm+x, ym-y, points); // -x,-y
            savePoint(xm+y, ym+x, points); // +x, -y
        
            radius = err;
            if (radius <= y) err += ++y*2+1;
            if (radius > x || err > y) err += ++x*2+1;
        }
        while (x < 0);

        return points;
    }


    public static Vector<Vector2> box(Vector2 a, Vector2 b){
        int lx = (int) (a.x < b.x ? a.x : b.x);
        int ly = (int) (a.y < b.y ? a.y : b.y);
        //System.out.println("Making Box: from " + a.toString() + " to " + b.toString() +", Size: " + Math.abs(a.x-b.x) +", " + Math.abs(b.y-a.y));
        Vector<Vector2> box = new Vector<Vector2>();
        for(int i=lx; i<lx+(int)Math.abs(a.x-b.x)+1; i++){
            for(int j=ly; j<ly+(int)Math.abs(a.y-b.y)+1; j++){
                if(i == a.x || i == b.x || j == a.y || j == b.y) box.add(new Vector2(i,j));
                
            }
        }

        return box;
    }
}

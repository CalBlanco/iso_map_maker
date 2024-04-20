package com.isomapmaker.game.util;

import com.badlogic.gdx.math.Vector2;
import com.isomapmaker.game.map.Atlas.enums.WallQuadrant;

/**
 * Utility Class for converting between isometric and world cordinates, Also contains methods for converting an index to a 2d point, and getting a wall quadrant 
 */
public class IsoUtil {
    final public static Vector2 FLOOR_SIZE = new Vector2(128,64);
    /**
     * Convert Isometric(tile) cordinates into screen / world cordinates
     * @param isoPos the iso tile
     * @param size the size of the tile (should normally 128x64)
     * @return A Vector2 containing the world cordinates
     */
    private static Vector2 outVector = new Vector2();
    public static Vector2 isometricToWorldPos(Vector2 isoPos, Vector2 size, Vector2 seter) {
        float x = (isoPos.x - isoPos.y) * (FLOOR_SIZE.x / 2f);
        float y = (isoPos.x + isoPos.y) * (FLOOR_SIZE.y / 2f);
        seter.set((int)x, (int)y);
        return seter;
    }
 
    /**
     * Convert world cordinates into a tile
     * @param worldPos The screen/world cords
     * @param size the floor size (should normally be 128x64)
     * @return
     */
    public static Vector2 worldPosToIsometric(Vector2 worldPos, Vector2 size, Vector2 seter) {
        float x = (worldPos.x / (FLOOR_SIZE.x / 2f) + worldPos.y / (FLOOR_SIZE.y / 2f)) / 2f  - 0.5f;
        float y = (worldPos.y  / (FLOOR_SIZE.y / 2f) - worldPos.x / (FLOOR_SIZE.x / 2f)) / 2f - 0.05f;
        seter.set((int)Math.floor(x), (int)Math.floor(y));
        return seter;
    }

    /**
     * Convert a 1d point into a 2d point given a width 
     * @param index
     * @param width
     * @return
     */
    public static int[] convertTo2DPoint(int index, int width) {
        int[] point = new int[2];
        point[0] = index % width; // x-coordinate
        point[1] = index / width; // y-coordinate
        return point;
    }

    /**
     * Get the quadrant the mouse is in for placing down a wall correctly
     * @param worldPos the tile position
     * @param mousePos the mouse position (honestly dont know why im taking both when i can just generate one but not gonna fw that rn)
     * @return
     */
    public static WallQuadrant getTileQuadrant(Vector2 worldPos, Vector2 mousePos) {
        Vector2 nv = new Vector2();
        Vector2 isoPos = isometricToWorldPos(worldPos, FLOOR_SIZE, nv);
        int tileX = (int) ((worldPos.x - isoPos.x) / FLOOR_SIZE.x);
        int tileY = (int) ((worldPos.y - isoPos.y) / FLOOR_SIZE.y);
        float halfWidth = FLOOR_SIZE.x / 2;
        float halfHeight = FLOOR_SIZE.y / 2;

        if (mousePos.x > isoPos.x + tileX * FLOOR_SIZE.x + halfWidth) {
            if (mousePos.y < isoPos.y + tileY * FLOOR_SIZE.y + halfHeight) {
                return WallQuadrant.bottom;
            } else {
                return WallQuadrant.right;
            }
        } else {
            if (mousePos.y < isoPos.y + tileY * FLOOR_SIZE.y + halfHeight) {
                return WallQuadrant.left;
            } else {
                return WallQuadrant.right;
            }
        }
    }
}

package com.isomapmaker.game.util;

import com.badlogic.gdx.math.Vector2;

public class IsoUtil {
    final public static Vector2 FLOOR_SIZE = new Vector2(128,64);
    // Function to convert world cordinates to IsoMetric cordinates
    public static Vector2 worldToIsometric(Vector2 worldPos, Vector2 size) {
        float x = (worldPos.x - worldPos.y) * (size.x / 2f);
        float y = (worldPos.x + worldPos.y) * (size.y / 2f);
        return new Vector2((int)x, (int)y);
    }

    // Function to convert isometric coordinates to world space coordinates
    public static Vector2 isometricToWorld(Vector2 isoPos, Vector2 size) {
        float x = (isoPos.x / (size.x / 2f) + isoPos.y / (size.y / 2f)) / 2f;
        float y = (isoPos.y / (size.y / 2f) - isoPos.x / (size.x / 2f)) / 2f;
        return new Vector2((int)x, (int)y);
    }

    public static int[] convertTo2DPoint(int index, int width) {
        int[] point = new int[2];
        point[0] = index % width; // x-coordinate
        point[1] = index / width; // y-coordinate
        return point;
    }

    public static String getTileQuadrant(Vector2 worldPos, Vector2 mousePos) {
        Vector2 isoPos = worldToIsometric(worldPos, FLOOR_SIZE);
        int tileX = (int) ((mousePos.x - isoPos.x) / FLOOR_SIZE.x);
        int tileY = (int) ((mousePos.y - isoPos.y) / FLOOR_SIZE.y);
        float halfWidth = FLOOR_SIZE.x / 2;
        float halfHeight = FLOOR_SIZE.y / 2;

        if (mousePos.x > isoPos.x + tileX * FLOOR_SIZE.x + halfWidth) {
            if (mousePos.y < isoPos.y + tileY * FLOOR_SIZE.y + halfHeight) {
                return "right";
            } else {
                return "bottom";
            }
        } else {
            if (mousePos.y < isoPos.y + tileY * FLOOR_SIZE.y + halfHeight) {
                return "top";
            } else {
                return "left";
            }
        }
    }
}

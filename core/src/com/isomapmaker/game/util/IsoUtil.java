package com.isomapmaker.game.util;

import com.badlogic.gdx.math.Vector2;

public class IsoUtil {
    // Function to convert world cordinates to IsoMetric cordinates
    public static Vector2 worldToIsometric(Vector2 worldPos, Vector2 size) {
        float x = (worldPos.x - worldPos.y) * (size.x / 2f);
        float y = (worldPos.x + worldPos.y) * (size.y / 2f);
        return new Vector2(x, y);
    }

    // Function to convert isometric coordinates to world space coordinates
    public static Vector2 isometricToWorld(Vector2 isoPos, Vector2 size) {
        float x = (isoPos.x / (size.x / 2f) + isoPos.y / (size.y / 2f)) / 2f;
        float y = (isoPos.y / (size.y / 2f) - isoPos.x / (size.x / 2f)) / 2f;
        return new Vector2((int)Math.floor(x), (int)Math.floor(y));
    }
}

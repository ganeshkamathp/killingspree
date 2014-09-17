package com.sillygames.killingSpree.helpers;

import com.badlogic.gdx.math.Vector2;
import com.sillygames.killingSpree.managers.WorldRenderer;

public class Utils {
    
    public static boolean wrapBody(Vector2 position) {
        boolean wrap = false;
        if (position.x > WorldRenderer.VIEWPORT_WIDTH / WorldRenderer.SCALE) {
            wrap = true;
            position.x -= WorldRenderer.VIEWPORT_WIDTH / WorldRenderer.SCALE;
        } else if (position.x < 0) {
            wrap = true;
            position.x += WorldRenderer.VIEWPORT_WIDTH / WorldRenderer.SCALE;
        }
        if (position.y > WorldRenderer.VIEWPORT_HEIGHT / WorldRenderer.SCALE) {
            wrap = true;
            position.y -= WorldRenderer.VIEWPORT_HEIGHT / WorldRenderer.SCALE;
        } else if (position.y < 0) {
            wrap = true;
            position.y += WorldRenderer.VIEWPORT_HEIGHT / WorldRenderer.SCALE;
        }
        return wrap;
    }
    
}

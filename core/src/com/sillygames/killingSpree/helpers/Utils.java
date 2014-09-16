package com.sillygames.killingSpree.helpers;

import com.badlogic.gdx.math.Vector2;
import com.sillygames.killingSpree.managers.WorldRenderer;
import com.sillygames.killingSpree.networking.messages.ControlsMessage;

public class Utils {
    
    private static final Vector2 vector= new Vector2();
    
//    public static Vector2 parseDirections(ControlsMessage controls) {
//        int direction = controls.direction;
//        float x = 0, y = 0;
//        switch (direction) {
//            case 1:
//                x = 0; y = 1f;
//                break;
//            case 2:
//                x = 0.71f; y = 0.71f;
//                break;
//            case 3:
//                x = 1f; y = 0;
//                break;
//            case 4:
//                x = 0.71f; y = -0.71f;
//                break;
//            case 5:
//                x = 0; y = -1f;
//                break;
//            case 6:
//                x = -0.71f; y = -0.71f;
//                break;
//            case 7:
//                x = -1f; y = 0f;
//                break;
//            case 8:
//                x = -0.71f; y = 0.71f;
//                break;
//        }
//        vector.x = x;
//        vector.y = y;
//        return vector;
//    }
    
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

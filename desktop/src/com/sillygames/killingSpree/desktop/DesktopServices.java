package com.sillygames.killingSpree.desktop;

import com.badlogic.gdx.Gdx;
import com.sillygames.killingSpree.PlatformServices;

public class DesktopServices implements PlatformServices {

    @Override
    public void toast(String message) {
        Gdx.app.log("Toast", message);
    }
    
    @Override
    public void shortToast(String message) {
        Gdx.app.log("Toast", message);
    }

}

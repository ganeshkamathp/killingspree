package com.sillygames.killingSpree.pool;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

public class AssetLoader {
    AssetManager manager;
    
    public AssetLoader() {
        manager = new AssetManager();
    }
    
    public void loadAll() {
        manager.load("sprites/player.png", TextureFilter.class);
        manager.load("sprites/blob.png", TextureFilter.class);
    }

}

package com.sillygames.killingSpree.pool;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class AssetLoader {
    
    public static final AssetLoader instance = new AssetLoader();
    public AssetManager manager;
    
    public AssetLoader() {
        manager = new AssetManager();
    }
    
    public void loadAll() {
        manager.load("sprites/player.png", Texture.class);
        manager.load("sprites/blob.png", Texture.class);
    }
    
    public Texture getTexture(String path) {
        manager.finishLoading();
        return manager.get(path, Texture.class);
    }
    
}

package com.sillygames.killingSpree.managers;

import com.badlogic.gdx.Gdx;
import com.sillygames.killingSpree.PlatformServices;
import com.sillygames.killingSpree.serverEntities.ServerBlob;
import com.sillygames.killingSpree.serverEntities.ServerFly;
import com.sillygames.killingSpree.serverEntities.ServerFrog;

public class LevelLoader {
    public String[] level;
    public float currentTime;
    public float nextTime;
    WorldManager world;
    int i;
    int wave;
    public PlatformServices platformServices;
    
    public LevelLoader(String file, WorldManager world) {
        level = Gdx.files.internal(file).readString().split("\n");
        currentTime = 0;
        this.world = world;
        nextTime = 0;
        i = 0;
        wave = 2;
//        platformServices.toast("Next wave - 1");
    }

    public void loadNextLine(float delta) {
        currentTime += delta;
        i %= level.length;
        while ((world.entities.size() - world.playerList.size() <= 0 ||
                currentTime >= nextTime) && i < level.length) {
            if (nextTime == 1000) {
                platformServices.toast(level[i++].trim());
            }
            String entity = readNextLine();
            String param[] = readNextLine().split(" ");
            if (entity.contentEquals("fly")) {
                ServerFly fly = world.addFly(Float.parseFloat(param[0]), 
                        Float.parseFloat(param[1]));
            }
            else if (entity.contentEquals("blob")) {
                ServerBlob blob = world.addBlob(Float.parseFloat(param[0]), 
                        Float.parseFloat(param[1]));
                blob.setDirection(Float.parseFloat(param[2]));
            }
            else if (entity.contentEquals("frog")) {
                ServerFrog frog = world.addFrog(Float.parseFloat(param[0]), 
                        Float.parseFloat(param[1]));
                frog.setDirection(Float.parseFloat(param[2]));
            }
            currentTime = 0;
            nextTime = Long.parseLong(readNextLine());
        }
    }
    
    String readNextLine() {
        i %= level.length;
        String line = level[i++].trim();
        while (line.compareTo("") == 0 && i < level.length) {
            i %= level.length;
            line = level[i++].trim();
        }
        return line;
    }
}

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
    private boolean done;
    
    public LevelLoader(String file, WorldManager world) {
        level = Gdx.files.internal(file).readString().split("\n");
        currentTime = 0;
        this.world = world;
        nextTime = 0;
        i = 0;
        wave = 2;
        done = false;
//        platformServices.toast("Next wave - 1");
    }

    public void loadNextLine(float delta) {
        currentTime += delta;
//        Gdx.app.log(Float.toString(currentTime), Float.toString(nextTime));
        while ((world.entities.size() - world.playerList.size() <= 0 ||
                currentTime >= nextTime) && i < level.length) {
            if (nextTime == 1000) {
                platformServices.toast("Next wave - " + Integer.toString(wave++));
            }
            String entity = level[i++].trim();
            String param[] = level[i++].trim().split(" ");
            if (entity.contentEquals("fly")) {
                ServerFly fly = world.addFly(Float.parseFloat(param[0]), 
                        Float.parseFloat(param[1]));
            }
            else if (entity.contentEquals("blob")) {
                ServerBlob blob = world.addBlob(Float.parseFloat(param[0]), 
                        Float.parseFloat(param[1]));
                blob.setDirection(Float.parseFloat(param[2]));
                Gdx.app.log("direction", param[2]);
            }
            else if (entity.contentEquals("frog")) {
                ServerFrog frog = world.addFrog(Float.parseFloat(param[0]), 
                        Float.parseFloat(param[1]));
                frog.setDirection(Float.parseFloat(param[2]));
                Gdx.app.log("direction", param[2]);
            }
            currentTime = 0;
            nextTime = Long.parseLong(level[i++].trim());
        }
        if (!done && i > level.length) {
            done = true;
            platformServices.toast("wohooo.. you won");
        }
    }
}

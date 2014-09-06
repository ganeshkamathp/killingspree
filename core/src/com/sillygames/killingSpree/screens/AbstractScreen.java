package com.sillygames.killingSpree.screens;

import com.badlogic.gdx.Screen;
import com.sillygames.killingSpree.KillingSpree;

public abstract class AbstractScreen implements Screen {
    
    KillingSpree game;
    
    public AbstractScreen(KillingSpree game) {
        this.game = game;
    }

}

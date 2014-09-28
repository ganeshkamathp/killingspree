package com.sillygames.killingSpree.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.sillygames.killingSpree.networking.messages.AudioMessage;

public class SFXPlayer {
    
    private Sound jump;
    private Sound shoot;
    private Sound explode;
    private Sound hurt;
    private Sound jumpedOn;
    
    public SFXPlayer() {
        jump = Gdx.audio.newSound(Gdx.files.internal("sounds/jump.ogg"));
        shoot = Gdx.audio.newSound(Gdx.files.internal("sounds/shoot.ogg"));
        explode = Gdx.audio.newSound(Gdx.files.internal("sounds/explode.ogg"));
        hurt = Gdx.audio.newSound(Gdx.files.internal("sounds/hurt.ogg"));
        jumpedOn = Gdx.audio.newSound(Gdx.files.internal("sounds/jumpedon.ogg"));
    }

    public void jump() {
        jump.play();
    }
    
    public void shoot() {
        shoot.play();
    }
    
    public void hurt() {
        hurt.play();
    }

    public void explode() {
        explode.play();
    }
    
    public void jumpedOn() {
        jumpedOn.play();
    }

    public void dispose() {
        jump.dispose();
        shoot.dispose();
        hurt.dispose();
        explode.dispose();
        jumpedOn.dispose();
    }
    
    public void playAudioMessage(AudioMessage message) {
        if (message.getJump()) {
            jump();
        }
        if (message.getShoot()) {
            shoot();
        }
        if (message.getHurt()) {
            hurt();
        }
        if (message.getExplode()) {
            explode();
        }
        if (message.getJumpedOn()) {
            jumpedOn();
        }
    }

}

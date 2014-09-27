package com.sillygames.killingSpree.helpers;

public class EntityUtils {
    
    public enum ActorType { ERROR, PLAYER, BLOB, ARROW, BULLET, FLY, FROG, BOMB };
    
    public static byte actorTypeToByte(ActorType type) {
        switch (type) {
            case ERROR:
                return 0;
            case PLAYER:
                return 1;
            case BLOB:
                return 2;
            case ARROW:
                return 3;
            case BULLET:
                return 4;
            case FLY:
                return 5;
            case FROG:
                return 6;
            case BOMB:
                return 7;
        }
        return -1;
    }
    
    public static ActorType ByteToActorType(byte type) {
        switch (type) {
            case 1:
                return ActorType.PLAYER;
            case 2:
                return ActorType.BLOB;
            case 3:
                return ActorType.ARROW;
            case 4:
                return ActorType.BULLET;
            case 5:
                return ActorType.FLY;
            case 6:
                return ActorType.FROG;
            case 7:
                return ActorType.BOMB;
        }
        return ActorType.ERROR;
    }

}

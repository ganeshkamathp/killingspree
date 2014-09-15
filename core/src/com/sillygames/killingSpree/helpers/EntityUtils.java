package com.sillygames.killingSpree.helpers;

public class EntityUtils {
    
    public enum ActorType { ERROR, PLAYER, BLOB };
    
    public static byte actorTypeToByte(ActorType type) {
        switch (type) {
            case ERROR:
                return 0;
            case PLAYER:
                return 1;
            case BLOB:
                return 2;
        }
        return -1;
    }
    
    public static ActorType ByteToActorType(byte type) {
        switch (type) {
            case 1:
                return ActorType.PLAYER;
            case 2:
                return ActorType.BLOB;
        }
        return ActorType.ERROR;
    }

}

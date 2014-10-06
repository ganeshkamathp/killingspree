package com.sillygames.killingSpree.networking.messages;

public class ServerStatusMessage {
    public enum Status { INFO, DISCONNECT };
    public String toastText;
    public Status status;
}

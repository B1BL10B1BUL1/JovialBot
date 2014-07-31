package io.mking.jovialbot;

public interface Message {
    String getId();
    String getSenderName();
    String getContent();
    Chat getChat();
}

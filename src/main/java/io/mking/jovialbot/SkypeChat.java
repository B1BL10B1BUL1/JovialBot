package io.mking.jovialbot;

public class SkypeChat implements Chat {

    private final com.skype.Chat skypeChat;

    public SkypeChat(com.skype.Chat skypeChat) {
        this.skypeChat = skypeChat;
    }

    @Override
    public String getId() {
        return this.skypeChat.getId();
    }

    @Override
    public void sendMessage(String message) {
        try {
            this.skypeChat.send(message);
        } catch (com.skype.SkypeException e) {
            e.printStackTrace();
        }
    }
}
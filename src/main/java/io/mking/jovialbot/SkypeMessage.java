package io.mking.jovialbot;

public class SkypeMessage implements Message{

    private com.skype.ChatMessage skypeMessage;

    public SkypeMessage(com.skype.ChatMessage skypeMessage) {
        this.skypeMessage = skypeMessage;
    }

    @Override
    public String getId() {
        return this.skypeMessage.getId();
    }

    @Override
    public String getSenderName() {
        try {
            return this.skypeMessage.getSenderDisplayName();
        } catch (com.skype.SkypeException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getContent() {
        try {
            return this.skypeMessage.getContent();
        } catch (com.skype.SkypeException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Chat getChat() {
        try {
            return new SkypeChat(this.skypeMessage.getChat());
        } catch (com.skype.SkypeException e) {
            e.printStackTrace();
            return null;
        }
    }
}

package io.mking.jovialbot;

public abstract class MessageListener implements com.skype.ChatMessageListener, com.skype.ChatMessageEditListener {

    public void HandleMessage(Message message) {
        // Do nothing.
    }

    public void HandleEdit(Message message) {
        // Do nothing.
    }

    @Override
    public void chatMessageReceived(com.skype.ChatMessage chatMessage) {
        this.HandleMessage(new SkypeMessage(chatMessage));
    }

    @Override
    public void chatMessageSent(com.skype.ChatMessage chatMessage) {
        // Do nothing. We don't care about messages we send.
    }

    @Override
    public void chatMessageEdited(com.skype.ChatMessage chatMessage, java.util.Date date, com.skype.User user) {
        this.HandleEdit(new SkypeMessage(chatMessage));
    }
}

package io.mking.jovialbot;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.skype.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class HistoryWatcher implements ChatMessageListener, ChatMessageEditListener {

    private final Cache<String, String> messageCache;

    public HistoryWatcher() {
        this.messageCache = CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
    }

    @Override
    public void chatMessageReceived(ChatMessage chatMessage) throws SkypeException {
        String id = chatMessage.getId();
        String message = chatMessage.getContent();
        this.messageCache.put(id, message);
    }

    @Override
    public void chatMessageSent(ChatMessage chatMessage) throws SkypeException {
        // Do nothing.
    }

    @Override
    public void chatMessageEdited(ChatMessage chatMessage, Date date, User user) {
        try {
            String id = chatMessage.getId();
            String messageCurrent = chatMessage.getContent();
            String messageOriginal = this.messageCache.getIfPresent(id);
            if (messageOriginal != null && !messageOriginal.equals(messageCurrent)) {
                this.messageCache.put(id, messageCurrent);
                String alert = this.getAlert(chatMessage.getSenderDisplayName(), messageOriginal, messageCurrent);
                chatMessage.getChat().send(alert);
            }
        } catch (SkypeException e) {
            e.printStackTrace();
        }
    }

    private String getAlert(String sender, String oldMessage, String newMessage) {
        return (newMessage.length() == 0)
                ? String.format("%s deleted message '%s'", sender, oldMessage)
                : String.format("%s edited message '%s' to '%s'", sender, oldMessage, newMessage);
    }
}

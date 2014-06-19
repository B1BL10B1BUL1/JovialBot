package io.mking.jovialbot;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.skype.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class EditListener implements ChatMessageListener, ChatMessageEditListener {

    private Cache<String, String> messageCache;

    public EditListener() {
        this.messageCache = CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
    }

    @Override
    public void chatMessageEdited(ChatMessage chatMessage, Date date, User user) {
        try {
            String id = chatMessage.getId();
            String messageCurrent = chatMessage.getContent();
            String messageOriginal = this.messageCache.getIfPresent(id);
            if (messageOriginal != null && !messageOriginal.equals(messageCurrent)) {
                String alert = null;
                if (messageCurrent.length() == 0) {
                    alert = String.format(
                            "%s deleted message '%s'",
                            chatMessage.getSenderDisplayName(),
                            messageOriginal);
                } else {
                    alert = String.format(
                            "%s edited message '%s' to '%s'",
                            chatMessage.getSenderDisplayName(),
                            messageOriginal,
                            messageCurrent);
                }
                this.messageCache.put(id, messageCurrent);
                chatMessage.getChat().send(alert);
            }
        } catch (Exception ex) {
            // #YOLO
        }
    }

    @Override
    public void chatMessageReceived(ChatMessage chatMessage) throws SkypeException {
        String id = chatMessage.getId();
        String message = chatMessage.getContent();
        this.messageCache.put(id, message);
    }

    @Override
    public void chatMessageSent(ChatMessage chatMessage) throws SkypeException {
        String id = chatMessage.getId();
        String message = chatMessage.getContent();
        this.messageCache.put(id, message);
    }
}

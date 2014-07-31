package io.mking.jovialbot;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.concurrent.TimeUnit;

public class HistoryWatcher1 extends MessageListener {

    private final Cache<String, String> messageCache;

    public HistoryWatcher1() {
        this.messageCache = CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
    }

    @Override
    public void HandleMessage(Message message) {
        String id = message.getId();
        String content = message.getContent();
        this.messageCache.put(id, content);
    }

    @Override
    public void HandleEdit(Message message) {
        String id = message.getId();
        String contentCurrent = message.getContent();
        String contentOriginal = this.messageCache.getIfPresent(id);
        if (contentOriginal != null && !contentOriginal.equals(contentCurrent)) {
            this.messageCache.put(id, contentCurrent);
            String alert = this.getAlert(message.getSenderName(), contentOriginal, contentCurrent);
            message.getChat().sendMessage(alert);
        }
    }

    private String getAlert(String sender, String oldMessage, String newMessage) {
        return (newMessage.length() == 0)
            ? String.format("%s deleted message '%s'", sender, oldMessage)
            : String.format("%s edited message '%s' to '%s'", sender, oldMessage, newMessage);
    }
}

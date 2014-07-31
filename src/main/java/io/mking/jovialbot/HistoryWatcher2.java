package io.mking.jovialbot;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class HistoryWatcher2 extends MessageListener {

    private final Cache<String, String> messageCache;
    private final Cache<String, MessageEditNotification> notificationCache;

    public HistoryWatcher2() {
        this.messageCache = CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
        this.notificationCache = CacheBuilder.newBuilder()
                .expireAfterWrite(2, TimeUnit.MINUTES)
                .build();
    }

    @Override
    public void HandleMessage(Message message) {
        Chat chat = message.getChat();
        String messageId = message.getId();
        String content = message.getContent();
        this.messageCache.put(messageId, content);

        if (content.equalsIgnoreCase("jovialbot show edits")) {
            String chatId = chat.getId();
            MessageEditNotification[] notifications = notificationCache.asMap().values().stream()
                    .filter(n -> n.getChatId().equals(chatId))
                    .sorted((n1, n2) -> n1.getTimestamp().compareTo(n2.getTimestamp()))
                    .toArray(MessageEditNotification[]::new);

            for (MessageEditNotification notification : notifications) {
                chat.sendMessage(notification.getText());
            }
        }
    }

    @Override
    public void HandleEdit(Message message) {
        Date timestamp = new Date();
        String chatId = message.getChat().getId();
        String messageId = message.getId();
        String contentCurrent = message.getContent();
        String contentOriginal = this.messageCache.getIfPresent(messageId);
        String sender = message.getSenderName();
        if (contentOriginal != null && !contentOriginal.equals(contentCurrent)) {
            this.messageCache.put(messageId, contentCurrent);
            String notificationId = messageId + "@" + timestamp.getTime();
            String notificationText = getNotificationText(sender, contentOriginal, contentCurrent);
            MessageEditNotification notification = new MessageEditNotification(chatId, notificationText, timestamp);
            this.notificationCache.put(notificationId, notification);
        }
    }

    private static String getNotificationText(String sender, String oldMessage, String newMessage) {
        return (newMessage.length() == 0)
            ? String.format("%s deleted message '%s'", sender, oldMessage)
            : String.format("%s edited message '%s' to '%s'", sender, oldMessage, newMessage);
    }

    private class MessageEditNotification {

        private String chatId;
        private String text;
        private Date timestamp;

        public MessageEditNotification(String chatId, String text, Date timestamp) {
            this.chatId = chatId;
            this.text = text;
            this.timestamp = timestamp;
        }

        public String getChatId() {
            return this.chatId;
        }

        public String getText() {
            return this.text;
        }

        public Date getTimestamp() {
            return this.timestamp;
        }
    }
}

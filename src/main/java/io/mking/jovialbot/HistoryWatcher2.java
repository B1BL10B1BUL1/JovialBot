package io.mking.jovialbot;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.skype.*;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class HistoryWatcher2 implements ChatMessageListener, ChatMessageEditListener {

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
    public void chatMessageReceived(ChatMessage chatMessage) throws SkypeException {
        Chat chat = chatMessage.getChat();
        String messageId = chatMessage.getId();
        String message = chatMessage.getContent();
        this.messageCache.put(messageId, message);

        if (message.equalsIgnoreCase("jovialbot show edits")) {
            String chatId = chat.getId();
            MessageEditNotification[] notifications = notificationCache.asMap().values().stream()
                    .filter(n -> n.getChatId().equals(chatId))
                    .sorted((n1, n2) -> n1.getTimestamp().compareTo(n2.getTimestamp()))
                    .toArray(MessageEditNotification[]::new);

            for (MessageEditNotification notification : notifications) {
                chat.send(notification.getText());
            }
        }
    }

    @Override
    public void chatMessageSent(ChatMessage chatMessage) throws SkypeException {
        // Do nothing.
    }

    @Override
    public void chatMessageEdited(ChatMessage chatMessage, Date date, User user) {
        try {
            Date timestamp = new Date();
            String chatId = chatMessage.getChat().getId();
            String messageId = chatMessage.getId();
            String messageCurrent = chatMessage.getContent();
            String messageOriginal = this.messageCache.getIfPresent(messageId);
            String sender = chatMessage.getSenderDisplayName();
            if (messageOriginal != null && !messageOriginal.equals(messageCurrent)) {
                this.messageCache.put(messageId, messageCurrent);
                String notificationId = messageId + "@" + timestamp.getTime();
                String notificationText = getNotificationText(sender, messageOriginal, messageCurrent);
                MessageEditNotification notification = new MessageEditNotification(chatId, notificationText, timestamp);
                this.notificationCache.put(notificationId, notification);
            }
        } catch (SkypeException e) {
            e.printStackTrace();
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

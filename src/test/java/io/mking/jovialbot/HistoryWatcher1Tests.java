package io.mking.jovialbot;

import static org.mockito.Mockito.*;

import org.junit.Test;

public class HistoryWatcher1Tests {

    @Test
    public void chatMessageReceivedButNoEditsMade_DoesNothing(){
        // Arrange.
        Chat chat = mock(Chat.class);
        Message originalMessage = mock(Message.class);
        when(originalMessage.getChat()).thenReturn(chat);
        when(originalMessage.getId()).thenReturn("id");
        when(originalMessage.getSenderName()).thenReturn("sender");
        when(originalMessage.getContent()).thenReturn("original message");
        HistoryWatcher1 watcher = new HistoryWatcher1();

        // Act.
        watcher.HandleMessage(originalMessage);

        // Assert.
        verifyZeroInteractions(chat);
    }

    @Test
    public void chatMessageReceivedThenEdited_RepliesWithEditDescription() {
        // Arrange.
        Chat chat = mock(Chat.class);
        Message originalMessage = mock(Message.class);
        when(originalMessage.getChat()).thenReturn(chat);
        when(originalMessage.getId()).thenReturn("id");
        when(originalMessage.getSenderName()).thenReturn("sender");
        when(originalMessage.getContent()).thenReturn("original message");
        Message editedMessage = mock(Message.class);
        when(editedMessage.getChat()).thenReturn(chat);
        when(editedMessage.getId()).thenReturn("id");
        when(editedMessage.getSenderName()).thenReturn("sender");
        when(editedMessage.getContent()).thenReturn("edited message");
        HistoryWatcher1 watcher = new HistoryWatcher1();

        // Act.
        watcher.HandleMessage(originalMessage);
        watcher.HandleEdit(editedMessage);

        // Assert.
        verify(chat).sendMessage("sender edited message 'original message' to 'edited message'");
    }

    @Test
    public void chatMessageReceivedThenDeleted_RepliesWithDeleteDescription() {
        // Arrange.
        Chat chat = mock(Chat.class);
        Message originalMessage = mock(Message.class);
        when(originalMessage.getChat()).thenReturn(chat);
        when(originalMessage.getId()).thenReturn("id");
        when(originalMessage.getSenderName()).thenReturn("sender");
        when(originalMessage.getContent()).thenReturn("original message");
        Message editedMessage = mock(Message.class);
        when(editedMessage.getChat()).thenReturn(chat);
        when(editedMessage.getId()).thenReturn("id");
        when(editedMessage.getSenderName()).thenReturn("sender");
        when(editedMessage.getContent()).thenReturn("");
        HistoryWatcher1 watcher = new HistoryWatcher1();

        // Act.
        watcher.HandleMessage(originalMessage);
        watcher.HandleEdit(editedMessage);

        // Assert.
        verify(chat).sendMessage("sender deleted message 'original message'");
    }
}

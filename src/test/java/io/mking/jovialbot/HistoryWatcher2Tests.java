package io.mking.jovialbot;

import static org.mockito.Mockito.*;

import org.junit.Test;

public class HistoryWatcher2Tests {

    @Test
     public void chatMessageReceivedThenEdited_DoesNotReply() {
        // Arrange.
        Chat chat = mock(Chat.class);
        when(chat.getId()).thenReturn("c");
        Message originalMessage = mock(Message.class);
        when(originalMessage.getChat()).thenReturn(chat);
        when(originalMessage.getId()).thenReturn("1");
        when(originalMessage.getSenderName()).thenReturn("sender");
        when(originalMessage.getContent()).thenReturn("original message");
        Message editedMessage = mock(Message.class);
        when(editedMessage.getChat()).thenReturn(chat);
        when(editedMessage.getId()).thenReturn("1");
        when(editedMessage.getSenderName()).thenReturn("sender");
        when(editedMessage.getContent()).thenReturn("edited message");
        HistoryWatcher2 watcher = new HistoryWatcher2();

        // Act.
        watcher.HandleMessage(originalMessage);
        watcher.HandleEdit(editedMessage);

        // Assert.
        verify(chat, never()).sendMessage(anyString());
    }

    @Test
    public void chatMessageReceivedThenEdited_HistoryRequested_RepliesWithEditDescription() {
        // Arrange.
        Chat chat = mock(Chat.class);
        when(chat.getId()).thenReturn("c");
        Message originalMessage = mock(Message.class);
        when(originalMessage.getChat()).thenReturn(chat);
        when(originalMessage.getId()).thenReturn("1");
        when(originalMessage.getSenderName()).thenReturn("sender");
        when(originalMessage.getContent()).thenReturn("original message");
        Message editedMessage = mock(Message.class);
        when(editedMessage.getChat()).thenReturn(chat);
        when(editedMessage.getId()).thenReturn("1");
        when(editedMessage.getSenderName()).thenReturn("sender");
        when(editedMessage.getContent()).thenReturn("edited message");
        Message requestMessage = mock(Message.class);
        when(requestMessage.getChat()).thenReturn(chat);
        when(requestMessage.getId()).thenReturn("2");
        when(requestMessage.getSenderName()).thenReturn("sender");
        when(requestMessage.getContent()).thenReturn("jovialbot show edits");
        HistoryWatcher2 watcher = new HistoryWatcher2();

        // Act.
        watcher.HandleMessage(originalMessage);
        watcher.HandleEdit(editedMessage);
        watcher.HandleMessage(requestMessage);

        // Assert.
        verify(chat).sendMessage("sender edited message 'original message' to 'edited message'");
    }

    @Test
    public void chatMessageReceivedThenDeleted_DoesNotReply() {
        // Arrange.
        Chat chat = mock(Chat.class);
        when(chat.getId()).thenReturn("c");
        Message originalMessage = mock(Message.class);
        when(originalMessage.getChat()).thenReturn(chat);
        when(originalMessage.getId()).thenReturn("1");
        when(originalMessage.getSenderName()).thenReturn("sender");
        when(originalMessage.getContent()).thenReturn("original message");
        Message editedMessage = mock(Message.class);
        when(editedMessage.getChat()).thenReturn(chat);
        when(editedMessage.getId()).thenReturn("1");
        when(editedMessage.getSenderName()).thenReturn("sender");
        when(editedMessage.getContent()).thenReturn("");
        HistoryWatcher2 watcher = new HistoryWatcher2();

        // Act.
        watcher.HandleMessage(originalMessage);
        watcher.HandleEdit(editedMessage);

        // Assert.
        verify(chat, never()).sendMessage(anyString());
    }

    @Test
    public void chatMessageReceivedThenDeleted_HistoryRequested_RepliesWithEditDescription() {
        // Arrange.
        Chat chat = mock(Chat.class);
        when(chat.getId()).thenReturn("c");
        Message originalMessage = mock(Message.class);
        when(originalMessage.getChat()).thenReturn(chat);
        when(originalMessage.getId()).thenReturn("1");
        when(originalMessage.getSenderName()).thenReturn("sender");
        when(originalMessage.getContent()).thenReturn("original message");
        Message editedMessage = mock(Message.class);
        when(editedMessage.getChat()).thenReturn(chat);
        when(editedMessage.getId()).thenReturn("1");
        when(editedMessage.getSenderName()).thenReturn("sender");
        when(editedMessage.getContent()).thenReturn("");
        Message requestMessage = mock(Message.class);
        when(requestMessage.getChat()).thenReturn(chat);
        when(requestMessage.getId()).thenReturn("2");
        when(requestMessage.getSenderName()).thenReturn("sender");
        when(requestMessage.getContent()).thenReturn("jovialbot show edits");
        HistoryWatcher2 watcher = new HistoryWatcher2();

        // Act.
        watcher.HandleMessage(originalMessage);
        watcher.HandleEdit(editedMessage);
        watcher.HandleMessage(requestMessage);

        // Assert.
        verify(chat).sendMessage("sender deleted message 'original message'");
    }

    @Test
    public void multipleMessagesReceivedAndEditedAndDeleted_HistoryRequested_RepliesWithDescriptionOfEveryChange(){
        // Arrange.
        Chat chat = mock(Chat.class);
        when(chat.getId()).thenReturn("c");
        Message m1 = mock(Message.class);
        when(m1.getChat()).thenReturn(chat);
        when(m1.getId()).thenReturn("1");
        when(m1.getSenderName()).thenReturn("sender1");
        when(m1.getContent()).thenReturn("message #1");
        Message e1 = mock(Message.class);
        when(e1.getChat()).thenReturn(chat);
        when(e1.getId()).thenReturn("1");
        when(e1.getSenderName()).thenReturn("sender1");
        when(e1.getContent()).thenReturn("message #1 (edit)");
        Message m2 = mock(Message.class);
        when(m2.getChat()).thenReturn(chat);
        when(m2.getId()).thenReturn("2");
        when(m2.getSenderName()).thenReturn("sender2");
        when(m2.getContent()).thenReturn("message #2");
        Message e2 = mock(Message.class);
        when(e2.getChat()).thenReturn(chat);
        when(e2.getId()).thenReturn("2");
        when(e2.getSenderName()).thenReturn("sender2");
        when(e2.getContent()).thenReturn("");
        Message r = mock(Message.class);
        when(r.getChat()).thenReturn(chat);
        when(r.getId()).thenReturn("2");
        when(r.getSenderName()).thenReturn("sender3");
        when(r.getContent()).thenReturn("jovialbot show edits");
        HistoryWatcher2 watcher = new HistoryWatcher2();

        // Act.
        watcher.HandleMessage(m1);
        watcher.HandleEdit(e1);
        watcher.HandleMessage(m2);
        watcher.HandleEdit(e2);
        watcher.HandleMessage(r);

        // Assert.
        verify(chat).sendMessage("sender1 edited message 'message #1' to 'message #1 (edit)'");
        verify(chat).sendMessage("sender2 deleted message 'message #2'");
    }
}

package io.mking.jovialbot;

import static org.mockito.Mockito.*;

import org.junit.Test;

public class YouTubeTitleAnnouncerTests {

    @Test
    public void chatMessageReceived_DoesNotContainYouTubeUrl_DoesNothing() {
        // Arrange.
        Chat c = mock(Chat.class);
        Message m = mock(Message.class);
        when(m.getChat()).thenReturn(c);
        YouTubeTitleAnnouncer a = new YouTubeTitleAnnouncer();

        // Act.
        a.HandleMessage(m);

        // Assert.
        verifyZeroInteractions(c);
    }

    @Test
    public void chatMessageReceived_ContainsYouTubeUrl_CanNotParseContent_DoesNothing() {
        // Arrange.
        Chat c = mock(Chat.class);
        WebClient wc = mock(WebClient.class);
        Message m = mock(Message.class);
        when(m.getChat()).thenReturn(c);
        when(m.getContent()).thenReturn("https://www.youtube.com/watch?v=test");
        when(wc.getContent("https://www.youtube.com/watch?v=test")).thenReturn("Garbage");
        YouTubeTitleAnnouncer a = new YouTubeTitleAnnouncer(wc);

        // Act.
        a.HandleMessage(m);

        // Assert.
        verifyZeroInteractions(c);
    }

    @Test
    public void chatMessageReceived_ContainsYouTubeUrl_CanParseContent_RepliesWithTitle() {
        // Arrange.
        Chat c = mock(Chat.class);
        WebClient wc = mock(WebClient.class);
        Message m = mock(Message.class);
        when(m.getChat()).thenReturn(c);
        when(m.getContent()).thenReturn("https://www.youtube.com/watch?v=test");
        when(wc.getContent("https://www.youtube.com/watch?v=test")).thenReturn("<html><head><title>Example Title - YouTube</title></head></html>");
        YouTubeTitleAnnouncer a = new YouTubeTitleAnnouncer(wc);

        // Act.
        a.HandleMessage(m);

        // Assert.
        verify(c).sendMessage("Example Title");
    }
}
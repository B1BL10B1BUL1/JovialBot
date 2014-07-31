package io.mking.jovialbot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YouTubeTitleAnnouncer extends MessageListener {

    private final WebClient webClient;

    public YouTubeTitleAnnouncer() {
        this.webClient = new WebClient();
    }

    public YouTubeTitleAnnouncer(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public void HandleMessage(Message message) {

        String content = message.getContent();
        if (content == null || !content.contains("youtube")) {
            return;
        }

        String clipId = this.getId(content);
        if (clipId == null) {
            return;
        }

        String clipTitle = this.getTitle(clipId);
        if (clipTitle == null) {
            return;
        }

        Chat chat = message.getChat();
        if (chat == null) {
            return;
        }

        chat.sendMessage(clipTitle);
    }

    private String getId(String url) {
        String id = null;
        Pattern p = Pattern.compile("https?:\\/\\/www\\.youtube\\.com\\/watch.*?v=([a-zA-Z0-9\\-_]+).*");
        Matcher m = p.matcher(url);
        if (m.matches()) {
            id = m.group(1);
        }

        return id;
    }

    private String getTitle(String id) {
        String address = String.format("https://www.youtube.com/watch?v=%s", id);
        String content = this.webClient.getContent(address);
        Pattern p = Pattern.compile("<title>(.*?)</title>", Pattern.MULTILINE);
        Matcher m = p.matcher(content);
        while (m.find()) {
            return m.group(1).replace(" - YouTube", "");
        }

        return null;
    }
}

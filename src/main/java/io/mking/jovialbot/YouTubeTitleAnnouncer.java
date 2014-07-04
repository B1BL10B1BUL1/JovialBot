package io.mking.jovialbot;

import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.ChatMessageListener;
import com.skype.SkypeException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YouTubeTitleAnnouncer implements ChatMessageListener {

    @Override
    public void chatMessageReceived(ChatMessage chatMessage) throws SkypeException {
        Chat chat = chatMessage.getChat();
        String content = chatMessage.getContent();
        if (content.contains("youtube")) {
            String id = getId(content);
            if (id != null) {
                String title = getTitle(id);
                if (title != null) {
                    chat.send(title);
                }
            }
        }
    }

    @Override
    public void chatMessageSent(ChatMessage chatMessage) throws SkypeException {
        // Do nothing.
    }

    private static String getId(String url) {
        String id = null;
        Pattern p = Pattern.compile("https?:\\/\\/www\\.youtube\\.com\\/watch.*?v=([a-zA-Z0-9\\-]+).*");
        Matcher m = p.matcher(url);
        if (m.matches()) {
            id = m.group(1);
        }

        return id;
    }

    private static String getTitle(String id) {
        String title = null;
        String address = String.format("https://www.youtube.com/watch?v=%s", id);

        try {
            URL url = new URL(address);
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            Pattern p = Pattern.compile(".*?<title>(.*?)</title>.*?");

            String inputLine;
            while ((inputLine = in.readLine()) != null)
            {
                Matcher m = p.matcher(inputLine);
                if (m.matches()) {
                    title = m.group(1);
                    break;
                }
            }
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return title.replace(" - YouTube", "");
    }
}

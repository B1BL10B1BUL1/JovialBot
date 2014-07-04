package io.mking.jovialbot;

import com.skype.Skype;
import com.skype.SkypeException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        HistoryWatcher2 listener = new HistoryWatcher2();
        YouTubeTitleAnnouncer titleAnnouncer = new YouTubeTitleAnnouncer();

        try {
            Skype.addChatMessageListener(listener);
            Skype.addChatMessageEditListener(listener);
            Skype.addChatMessageListener(titleAnnouncer);
        } catch (SkypeException e) {
            e.printStackTrace();
        }

        try {
            System.out.println("JovialBot running...");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

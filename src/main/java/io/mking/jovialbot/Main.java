package io.mking.jovialbot;

import com.skype.Skype;
import com.skype.SkypeException;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        ArrayList<MessageListener> listeners = new ArrayList<MessageListener>();
        listeners.add((new HistoryWatcher2()));
        listeners.add(new YouTubeTitleAnnouncer());

        for (MessageListener listener : listeners) {
            try {
                Skype.addChatMessageListener(listener);
                Skype.addChatMessageEditListener(listener);
            } catch (SkypeException e) {
                e.printStackTrace();
            }
        }

        try {
            System.out.println("JovialBot running...");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

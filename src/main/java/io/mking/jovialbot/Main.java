package io.mking.jovialbot;

import com.skype.Skype;
import com.skype.SkypeException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        EditListener listener = new EditListener();

        try {
            Skype.addChatMessageListener(listener);
            Skype.addChatMessageEditListener(listener);
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


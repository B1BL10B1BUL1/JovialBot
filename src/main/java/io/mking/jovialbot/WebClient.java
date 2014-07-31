package io.mking.jovialbot;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class WebClient {

    public String getContent(String address) {
        URL url = null;
        try {
            url = new URL(address);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

        StringBuffer content = new StringBuffer();
        try {
            InputStream stream = url.openStream();
            int count;
            while ((count = stream.read()) != -1) {
                content.append(new String(Character.toChars(count)));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return content.toString();
    }
}

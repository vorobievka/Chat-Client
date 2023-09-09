package org.example;

import java.util.Date;

public class Message implements Comparable<Message> {
    public long timestamp;
    public String nickname;
    public String message;

    public Message() {
        // Пустой конструктор
    }


    public Message(long timestamp, String nickname, String message) {
        this.timestamp = timestamp;
        this.nickname = nickname;
        this.message = message;

    }

    public static Date dateFrom(long timestamp) {
        return new Date(timestamp * 1000);
    }


    public String toString() {
        String date = dateFrom(timestamp) + "";
        return "# " + date.substring(0, 20) + " " + nickname + ": " + message + " ";
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public int compareTo(Message o) {
        if (getTimestamp() > o.timestamp) {
            return 1;
        } else if (getTimestamp() < o.timestamp) {
            return -1;
        }
        return 0;
    }
}

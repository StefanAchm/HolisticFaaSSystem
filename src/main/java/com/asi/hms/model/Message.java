package com.asi.hms.model;

import java.util.UUID;

public class Message {

    private final UUID id;

    private final int step;
    private final int steps;

    private final String text;

    public Message(UUID id, int step, int steps, String text) {
        this.id = id;
        this.step = step;
        this.steps = steps;
        this.text = text;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", step=" + step +
                ", steps=" + steps +
                ", message='" + text + '\'' +
                '}';
    }
}

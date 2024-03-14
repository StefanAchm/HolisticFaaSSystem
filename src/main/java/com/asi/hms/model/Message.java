package com.asi.hms.model;

import java.util.UUID;

public class Message {

    private final UUID id;

    private final int step;
    private final int steps;

    private final String text;

    private final String status;

    private final String statusMessage;

    public Message(UUID id, int step, int steps, String text, String status, String statusMessage) {
        this.id = id;
        this.step = step;
        this.steps = steps;
        this.text = text;
        this.status = status;
        this.statusMessage = statusMessage;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", step=" + step +
                ", steps=" + steps +
                ", message='" + text + '\'' +
                ", status='" + status + '\'' +
                ", statusMessage='" + statusMessage + '\'' +
                '}';
    }
}

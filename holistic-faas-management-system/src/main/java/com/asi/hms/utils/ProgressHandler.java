package com.asi.hms.utils;

import com.asi.hms.exceptions.HolisticFaaSException;
import com.asi.hms.utils.cloudproviderutils.model.Message;
import com.asi.hms.model.db.ProgressObjectInterface;
import com.asi.hms.service.WebSocketSessionService;

public class ProgressHandler {

    private final ProgressObjectInterface progressObject;

    private final WebSocketSessionService sessionService;

    /**
     * Number of steps, including the start and finish
     * E.g. with 5 steps, you should call update 4 times, like this:
     * start, update, update, update, update, finish
     */
    private final int steps;

    private int step;

    public ProgressHandler(ProgressObjectInterface progressObject, int updateSteps, WebSocketSessionService sessionService) {

        this.progressObject = progressObject;

        this.sessionService = sessionService;

        this.steps = updateSteps + 2;

        this.step = 0;

    }

    public void update(String message) {

        if(this.step >= this.steps) {
            throw new HolisticFaaSException("Progress already finished");
        }

        this.send(message, this.step++);
    }

    public void start() {
        this.step = 1; // Start at 1
        this.send("Progress started", this.step++);
    }

    public void finish() {
        this.send("Progress finished", this.steps);
    }

    private void send(String message, int step) {

        sessionService.sendMessage(
                new Message(
                        this.progressObject.getId(),
                        step,
                        this.steps,
                        message,
                        this.progressObject.getStatus().toString(),
                        this.progressObject.getStatusMessage()
                )
        );

    }

}

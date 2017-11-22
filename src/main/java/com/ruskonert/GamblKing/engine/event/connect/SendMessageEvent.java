package com.ruskonert.GamblKing.engine.event.connect;

import com.ruskonert.GamblKing.engine.event.Event;
import com.ruskonert.GamblKing.entity.MessageDispatcher;

public class SendMessageEvent extends Event
{
    private  MessageDispatcher dispatcher;
    public void setDispatcher(MessageDispatcher dispatcher) { this.dispatcher = dispatcher; }
    public  MessageDispatcher getDispatcher() { return this.dispatcher; }

    private  String message;
    public void setMessage(String message) { this.message = message; }
    public  String getMessage() { return this.message; }

    public SendMessageEvent(MessageDispatcher dispatcher, String message)
    {
        this.dispatcher = dispatcher;
        this.message = message;
    }
}

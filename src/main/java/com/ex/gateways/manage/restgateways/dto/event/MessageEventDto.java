package com.ex.gateways.manage.restgateways.dto.event;

import com.ex.gateways.manage.restgateways.utils.MessageEventType;

import java.util.Date;

public class MessageEventDto {
    private String message;
    private MessageEventType type;
    private Date date;

    public MessageEventDto(String message, MessageEventType type, Date date) {
        setMessage(message);
        setType(type);
        setDate(date);
    }


    public String getMessage() {
        return message;
    }

    public MessageEventType getType() {
        return type;
    }

    public Date getDate() {
        return date;
    }

    private void setMessage(String message) {
        this.message = message;
    }

    private void setType(MessageEventType type) {
        this.type = type;
    }

    private void setDate(Date date) {
        this.date = date;
    }
}

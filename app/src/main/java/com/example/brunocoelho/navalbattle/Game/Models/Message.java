package com.example.brunocoelho.navalbattle.Game.Models;

import com.example.brunocoelho.navalbattle.Game.Constants;

public class Message {

    private String objectType;
    private String content;

    public Message(String content) {
        this.objectType = Constants.CLASS_MESSAGE; //className
        this.content = content;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Message{" +
                "objectType='" + objectType + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}

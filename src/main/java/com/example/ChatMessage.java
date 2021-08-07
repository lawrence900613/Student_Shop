package com.example;

public class ChatMessage {
    
    private String senderName;
    private String senderID;
    private String recipientName;
    private String recipientID;
    private String content;

    public ChatMessage(String senderName, String senderID, String content) {
        this.senderName = senderName;
        this.senderID = senderID;
        this.content = content;
    }
      
    public String getSenderName() {return this.senderName;}

    public String getSenderID() {return this.senderID;}
      
    public String getRecipientName() {return this.recipientName;}

    public String getRecipientID() {return this.recipientID;}

    public String getContent() {return this.content;}
    
    public void setSenderName(String senderName) {this.senderName = senderName;}

    public void setSenderID(String senderID) {this.senderID = senderID;}

    public void setRecipientName(String recipientName) {this.recipientName = recipientName;}

    public void setRecipientID(String recipientID) {this.recipientID = recipientID;}

    public void setContent(String content) {this.content = content;}
}

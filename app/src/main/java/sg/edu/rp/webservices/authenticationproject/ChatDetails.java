package sg.edu.rp.webservices.authenticationproject;

import java.io.Serializable;

/**
 * Created by 15004557 on 15/8/2017.
 */

public class ChatDetails implements Serializable {
    private String messageText;
    private String messageTime;
    private String messageUser;

    public ChatDetails(){}

    public ChatDetails(String messageText, String messageTime, String messageUser) {
        this.messageText = messageText;
        this.messageTime = messageTime;
        this.messageUser = messageUser;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }
}

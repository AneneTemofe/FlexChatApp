package uk.ac.tees.scdt.mad.c2360539.flexchat;

public class ModelClass {

    String message;
    String from;

    public ModelClass() {
    }

    public ModelClass(String message, String from) {
        this.message = message;
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}

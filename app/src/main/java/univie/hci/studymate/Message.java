package univie.hci.studymate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Message implements Comparable<Message> {
    private LocalDateTime dateTime;
    private User from;
    private String message;

    private boolean vote = false;

    public Message(User from, String message) {
        this.from = from;
        this.message = message;
        this.dateTime = LocalDateTime.now(ZoneId.of("Europe/Vienna"));
    }

    public User getFrom() {
        return from;
    }

    public String getMessageContent() {
        return message;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return dateTime.format(formatter);
    }

    @Override
    public int compareTo(Message other) {
        return this.dateTime.compareTo(other.dateTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return Objects.equals(dateTime, message1.dateTime) &&
                Objects.equals(from, message1.from) &&
                Objects.equals(message, message1.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTime, from, message);
    }

    @Override
    public String toString() {
        return "Message{" +
                "dateTime=" + dateTime +
                ", from=" + from +
                ", message='" + message + '\'' +
                '}';
    }

    public void setVote(boolean newVote){
        vote = newVote;
    }

    public boolean getVote(){
        return vote;
    }
}

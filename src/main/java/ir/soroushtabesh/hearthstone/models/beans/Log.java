package ir.soroushtabesh.hearthstone.models.beans;

import javax.persistence.*;

@Entity
@Table(name = "logger")
public class Log {
    //todo
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int log_id;
    private int user_id;
    private String date;
    private String event;
    private String description;
    @Enumerated(EnumType.STRING)
    private Severity severity;

    public Log() {
    }

    public Log(int user_id, String date, String event, String description, Severity severity) {
        this.user_id = user_id;
        this.date = date;
        this.event = event;
        this.description = description;
        this.severity = severity;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public int getLog_id() {
        return log_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public enum Severity {
        INFO, WARNING, FATAL
    }
}

package com.timebook.timebook.models.events;

import java.util.Objects;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String email;
    private String title;
    private String description;
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private LocalDateTime startDateTime;
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private LocalDateTime endDateTime;
    private int priority;
    private Boolean isPublic = true;

    public Event() {
    }

    Event(
            long id,
            String email,
            String title,
            String description,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            int priority,
            Boolean isPublic) {
        this.id = id;
        this.email = email;
        this.title = title;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.priority = priority;
        this.isPublic = isPublic;
    }

    public long getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public LocalDateTime getStartDateTime() {
        return this.startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return this.endDateTime;
    }

    public int getPriority() {
        return this.priority;
    }

    public Boolean getIsPublic() {
        return this.isPublic;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof Event))
            return false;

        Event event = (Event) o;
        return Objects.equals(this.id, event.id) && Objects.equals(this.email, event.email);

    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.email, this.title, this.description, this.startDateTime, this.endDateTime,
                this.priority);
    }

    @Override
    public String toString() {
        return "event{" + "id=" + this.id + ", email='" + this.email + '\'' + ", title='" + this.title + '\''
                + ", description='" + this.description + '\'' +
                ", startDateTime='" + this.startDateTime + ", endDateTime='" + this.endDateTime + ", priority='"
                + this.priority + ", isPublic='" + this.isPublic + '}';
    }
}

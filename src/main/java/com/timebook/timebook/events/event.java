package com.timebook.timebook.events;

import java.util.Objects;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;

@Entity
public class event {
    private @Id @GeneratedValue long id;
    private String email;
    private String title;
    private String description;
    private String startDateTime;
    private String endDateTime;
    private int priority;

    event(){}

    event(
        long id, 
        String email, 
        String title, 
        String description, 
        String startDateTime,
        String endDateTime,
        int priority
        ){
            this.id = id;
            this.email = email;
            this.title = title;
            this.description = description;
            this.startDateTime = startDateTime;
            this.endDateTime = endDateTime;
            this.priority = priority;
        }

        public Long getId(){
            return this.id;
        }

        public String getEmail(){
            return this.email;
        }

        public String getTitle(){
            return this.title;
        }

        public String getDescription(){
            return this.description;
        }

        public String getStartDateTime(){
            return this.startDateTime;
        }

        public String getEndDateTime(){
            return this.endDateTime;
        }

        public int getPriority(){
            return this.priority;
        }

        public void setId(Long id){
            this.id = id;
        }

        public void setEmail(String email){
            this.email = email;
        }

        public void setTitle(String title){
            this.title = title;
        }

        public void setDescription(String description){
            this.description = description;
        }

        public void setStartDateTime(String startDateTime){
            this.startDateTime = startDateTime;
        }

        public void setEndDateTime(String endDateTime){
            this.endDateTime = endDateTime;
        }   

        public void setPriority(int priority){
            this.priority = priority;
        }

    @Override
    public boolean equals(Object o){
        if(this == o)
        return true;

        if(!(o instanceof event))
        return false;

        event event = (event) o;
        return Objects.equals(this.id, event.id) 
        && Objects.equals(this.email, event.email)
        && Objects.equals(this.title, event.title)
        && Objects.equals(this.description, event.description)
        && Objects.equals(this.startDateTime, event.startDateTime)
        && Objects.equals(this.endDateTime, event.endDateTime)
        && Objects.equals(this.priority, priority);

    }

    @Override
    public int hashCode() {
      return Objects.hash(this.id, this.email, this.title, this.description, this.startDateTime, this.endDateTime, this.priority);
    }

    @Override
    public String toString() {
      return "event{" + "id=" + this.id + ", email='" + this.email + '\'' + ", title='" + this.title + '\'' + ", description='" + this.description + '\'' + 
      ", startDateTime='" + this.startDateTime + ", endDateTime='" + this.endDateTime + ", priority='" + this.priority + '}';
    }
}

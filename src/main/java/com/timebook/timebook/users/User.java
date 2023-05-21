package com.timebook.timebook.users;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String cognitoId;
    private String email;
    private String firstName;
    private String lastName;
    private List subscriber;
    private List subscribed;
    private Object metadata;

    User(
            long id,
            String cognitoId,
            String email,
            String firstName,
            String lastName,
            List subscriber,
            List subscribed,
            Object metadata) {
        this.id = id;
        this.cognitoId = cognitoId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.subscriber = subscriber;
        this.subscribed = subscribed;
        this.metadata = metadata;
    }

    public long getUserId() {
        return this.id;
    }

    public String getCognitoId() {
        return this.cognitoId;
    }

    public String getEmail() {
        return this.email;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public List getSubscriber() {
        return this.subscriber;
    }

    public List getSubscribed() {
        return this.subscribed;
    }

    public Object getMetadata() {
        return this.metadata;
    }

    public void setUserId(long id) {
        this.id = id;
    }

    public void setCognitoId(String cognitoId) {
        this.cognitoId = cognitoId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setSubscriber(List subscriber) {
        this.subscriber = subscriber;
    }

    public void setSubscribed(List subscribed) {
        this.subscribed = subscribed;
    }

    public void setMetadata(Object metadata) {
        this.metadata = metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof User))
            return false;

        User user = (User) o;
        return Objects.equals(this.id, user.id)
                && Objects.equals(this.cognitoId, user.cognitoId)
                && Objects.equals(this.email, user.email)
                && Objects.equals(this.firstName, user.firstName)
                && Objects.equals(this.lastName, user.lastName)
                && Objects.equals(this.subscriber, user.subscriber)
                && Objects.equals(this.subscribed, user.subscribed)
                && Objects.equals(this.metadata, user.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                this.id,
                this.cognitoId,
                this.email,
                this.firstName,
                this.lastName,
                this.subscriber,
                this.subscribed,
                this.metadata);
    }

    @Override
    public String toString() {
        return "event{" +
                "id=" + this.id +
                ", cognitoId='" + this.cognitoId + '\'' +
                ", email='" + this.email + '\'' +
                ", firstName='" + this.firstName + '\'' +
                ", lastName='" + this.lastName +
                ", subscriber='" + this.subscriber +
                ", subscribed='" + this.subscribed +
                ", metadata='" + this.metadata + '}';
    }
}

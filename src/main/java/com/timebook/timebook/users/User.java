package com.timebook.timebook.users;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import net.minidev.json.JSONObject;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String cognitoId;
    private String email;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<User> subscriber;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<User> subscribed;
    private JSONObject metadata;

    public User() {
    }

    public User(
            long id,
            String cognitoId,
            String email,
            List<User> subscriber,
            List<User> subscribed,
            JSONObject metadata) {
        this.id = id;
        this.cognitoId = cognitoId;
        this.email = email;
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

    public List<User> getSubscriber() {
        return this.subscriber;
    }

    public List<User> getSubscribed() {
        return this.subscribed;
    }

    public JSONObject getMetadata() {
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

    public void setSubscriber(List<User> subscriber) {
        this.subscriber = subscriber;
    }

    public void setSubscribed(List<User> subscribed) {
        this.subscribed = subscribed;
    }

    public void setMetadata(JSONObject metadata) {
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
                ", subscriber='" + this.subscriber +
                ", subscribed='" + this.subscribed +
                ", metadata='" + this.metadata +
                '}';
    }
}

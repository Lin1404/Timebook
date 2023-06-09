package com.timebook.timebook.models.users;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
    private List<User> subscribers;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<User> subscriptions;
    private JSONObject metadata;

    public User() {
    }

    public User(
            long id,
            String cognitoId,
            String email,
            List<User> subscribers,
            List<User> subscriptions,
            JSONObject metadata) {
        this.id = id;
        this.cognitoId = cognitoId;
        this.email = email;
        this.subscribers = subscribers;
        this.subscriptions = subscriptions;
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

    public List<User> getSubscribers() {
        return this.subscribers;
    }

    public List<User> getSubscriptions() {
        return this.subscriptions;
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

    public void setSubscribers(List<User> subscribers) {
        this.subscribers = subscribers;
    }

    public void setSubscriptions(List<User> subscriptions) {
        this.subscriptions = subscriptions;
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
                && Objects.equals(this.email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                this.id,
                this.cognitoId,
                this.email,
                this.subscribers,
                this.subscriptions,
                this.metadata);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + this.id +
                ", cognitoId='" + this.cognitoId + '\'' +
                ", email='" + this.email + '\'' +
                ", subscribers='" +
                this.subscribers.stream().map(User::getEmail).collect(Collectors.toList()) +
                ", subscriptions='" +
                this.subscriptions.stream().map(User::getEmail).collect(Collectors.toList())
                +
                ", metadata='" + this.metadata.toJSONString() +
                '}';
    }
}

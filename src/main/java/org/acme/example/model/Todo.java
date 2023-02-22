package org.acme.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;
import java.util.Vector;

@Entity
@Table(name = "todo")
public class Todo {

    private String id;
    private String content;
    private String owner;

    public Todo() {
    }

    public Todo(String id, String content, String owner) {
        this.content = content;
        this.id = id;
        this.owner = owner;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String description) {
        this.content = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Id
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof final Todo group)) {
            return false;
        }
        return Objects.equals(this.getContent(), group.getContent())
                && Objects.equals(this.getOwner(), group.getOwner())
                && Objects.equals(this.getId(), group.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, id, owner);
    }

    @Override
    public String toString() {
        if (id != null) {
            return id + ": " + content;
        } else {
            return content;
        }
    }
}


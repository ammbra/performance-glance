package org.acme.example.model;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "todo")
public class Todo {

    @Id
    @Column(name = "ID")
    private UUID id;

    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on")
    @CreatedDate
    private Date createdOn;

    private String owner;

    public Todo() {
    }

    public Todo(UUID id, String content, String owner) {
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

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Id
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
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


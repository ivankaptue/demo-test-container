package com.klid.demotestcontainer;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.util.UUID;

/**
 * @author Ivan Kaptue
 */
@Entity
public class Task {

    @Id
    private String id;

    private String title;

    @Enumerated(EnumType.STRING)
    private Status status;

    public Task() {
    }

    public Task(String title) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.status = Status.PENDING;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        PENDING, DONE
    }
}

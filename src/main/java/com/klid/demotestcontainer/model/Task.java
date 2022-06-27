package com.klid.demotestcontainer.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.util.Objects;
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

  public Task(String id, String title) {
    this.id = id;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Task)) return false;
    Task task = (Task) o;
    return getId().equals(task.getId()) && getTitle().equals(task.getTitle()) && getStatus() == task.getStatus();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getTitle(), getStatus());
  }
}

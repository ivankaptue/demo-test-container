package com.klid.demotestcontainer.service;

import com.klid.demotestcontainer.model.Task;
import com.klid.demotestcontainer.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Ivan Kaptue
 */
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

  @Mock
  private TaskRepository taskRepository;

  @InjectMocks
  private TaskService taskService;

  @BeforeEach
  void setup() {
    reset(taskRepository);
  }

  @Test
  void givenTaskTitle_whenCreate_thenCreateNewTask() {
    var taskTitle = "Task 1";
    var task = new Task(taskTitle);
    when(taskRepository.save(any(Task.class))).thenReturn(task);

    var id = taskService.create(taskTitle);

    assertThat(id).isNotNull();
    assertThat(id).isEqualTo(task.getId());
  }

  @Nested
  @DisplayName("Test update task")
  class Update {
    @Test
    void givenExistingTaskIdAndTitle_whenUpdate_thenUpdateTaskTitle() {
      var id = UUID.randomUUID().toString();
      var taskTitle1 = "Task 1";
      var taskTitle2 = "Task 2";
      var task1 = new Task(id, taskTitle1);

      when(taskRepository.findById(id)).thenReturn(Optional.of(task1));
      when(taskRepository.save(any(Task.class))).thenReturn(null);

      var savedId = taskService.update(id, taskTitle2);

      var updatedTask = ArgumentCaptor.forClass(Task.class);

      verify(taskRepository).save(updatedTask.capture());

      assertThat(savedId).isEqualTo(id);
      assertThat(updatedTask.getValue().getTitle()).isEqualTo(taskTitle2);
    }

    @Test
    void givenNotExistingTaskIdAndTitle_whenUpdate_thenThrowNoSuchElementException() throws NoSuchElementException {
      var id = UUID.randomUUID().toString();
      var taskTitle2 = "Task 2";

      when(taskRepository.findById(id)).thenReturn(Optional.empty());

      assertThatThrownBy(() -> taskService.update(id, taskTitle2)).isInstanceOf(NoSuchElementException.class);
    }
  }

  @Nested
  @DisplayName("Test complete task")
  class Complete {
    @Test
    void givenExistingTaskId_whenCompleteTask_thenTaskCompleted() {
      var id = UUID.randomUUID().toString();
      var taskTitle = "Task 1";
      var task1 = new Task(id, taskTitle);

      when(taskRepository.findById(id)).thenReturn(Optional.of(task1));
      when(taskRepository.save(any(Task.class))).thenReturn(null);

      taskService.completeTask(id);

      var completedTask = ArgumentCaptor.forClass(Task.class);

      verify(taskRepository).save(completedTask.capture());

      assertThat(completedTask.getValue().getId()).isEqualTo(id);
      assertThat(completedTask.getValue().getTitle()).isEqualTo(taskTitle);
      assertThat(completedTask.getValue().getStatus()).isEqualTo(Task.Status.DONE);
    }

    @Test
    void givenNotExistingTaskId_whenCompleteTask_thenThrowNoSuchElementException() throws NoSuchElementException {
      var id = UUID.randomUUID().toString();

      when(taskRepository.findById(id)).thenReturn(Optional.empty());

      assertThatThrownBy(() -> taskService.completeTask(id)).isInstanceOf(NoSuchElementException.class);
    }
  }

  @Nested
  @DisplayName("Test uncomplete task")
  class UnComplete {
    @Test
    void givenExistingTaskId_whenUnCompleteTask_thenTaskUnCompleted() {
      var id = UUID.randomUUID().toString();
      var taskTitle = "Task 1";
      var task1 = new Task(id, taskTitle);

      when(taskRepository.findById(id)).thenReturn(Optional.of(task1));
      when(taskRepository.save(any(Task.class))).thenReturn(null);

      taskService.unCompleteTask(id);

      var unCompletedTask = ArgumentCaptor.forClass(Task.class);

      verify(taskRepository).save(unCompletedTask.capture());

      assertThat(unCompletedTask.getValue().getId()).isEqualTo(id);
      assertThat(unCompletedTask.getValue().getTitle()).isEqualTo(taskTitle);
      assertThat(unCompletedTask.getValue().getStatus()).isEqualTo(Task.Status.PENDING);
    }

    @Test
    void givenNotExistingTaskId_whenUnCompleteTask_thenThrowNoSuchElementException() throws NoSuchElementException {
      var id = UUID.randomUUID().toString();

      when(taskRepository.findById(id)).thenReturn(Optional.empty());

      assertThatThrownBy(() -> taskService.unCompleteTask(id)).isInstanceOf(NoSuchElementException.class);
    }
  }

  @Nested
  @DisplayName("Test delete task")
  class Delete {
    @Test
    void givenExistingTaskId_whenDeleteTask_thenTaskDeleted() {
      var id = UUID.randomUUID().toString();
      var taskTitle = "Task 1";
      var task = new Task(id, taskTitle);

      when(taskRepository.findById(id)).thenReturn(Optional.of(task));
      doNothing().when(taskRepository).delete(any(Task.class));

      taskService.delete(id);

      var deletedTask = ArgumentCaptor.forClass(Task.class);

      verify(taskRepository).delete(deletedTask.capture());

      assertThat(deletedTask.getValue().getId()).isEqualTo(task.getId());
      assertThat(deletedTask.getValue().getTitle()).isEqualTo(task.getTitle());
      assertThat(deletedTask.getValue().getStatus()).isEqualTo(task.getStatus());
    }

    @Test
    void givenNotExistingTaskId_whenDeleteTask_thenThrowNoSuchElementException() throws NoSuchElementException {
      var id = UUID.randomUUID().toString();

      when(taskRepository.findById(id)).thenReturn(Optional.empty());

      assertThatThrownBy(() -> taskService.delete(id)).isInstanceOf(NoSuchElementException.class);
    }
  }

  @Nested
  @DisplayName("Test getById task")
  class FindById {
    @Test
    void givenExistingTaskId_whenGetById_thenReturnsTask() {
      var id = UUID.randomUUID().toString();
      var taskTitle = "Task 1";
      var task = new Task(id, taskTitle);

      when(taskRepository.findById(id)).thenReturn(Optional.of(task));

      var foundTask = taskService.getById(id);

      assertThat(foundTask.getId()).isEqualTo(task.getId());
      assertThat(foundTask.getTitle()).isEqualTo(task.getTitle());
      assertThat(foundTask.getStatus()).isEqualTo(task.getStatus());
    }

    @Test
    void givenNotExistingTaskId_whenGetById_thenThrowNoSuchElementException() throws NoSuchElementException {
      var id = UUID.randomUUID().toString();

      when(taskRepository.findById(id)).thenReturn(Optional.empty());

      assertThatThrownBy(() -> taskService.getById(id)).isInstanceOf(NoSuchElementException.class);
    }
  }

  @Test
  void givenPageAndSize_whenAll_thenReturnsAllTasksPaginated() {
    int page = 0;
    int size = 5;
    var pageRequest = PageRequest.of(page, size);
    var tasks = List.of(new Task("Task 1"), new Task("Task 2"), new Task("Task 3"));

    when(taskRepository.all(pageRequest)).thenReturn(new PageImpl<>(tasks, pageRequest, tasks.size()));

    var taskPage = taskService.all(page, size);

    assertThat(taskPage.getContent()).hasSize(3);
    assertThat(tasks.get(0).getTitle()).isEqualTo("Task 1");
    assertThat(tasks.get(1).getTitle()).isEqualTo("Task 2");
    assertThat(tasks.get(2).getTitle()).isEqualTo("Task 3");
  }
}

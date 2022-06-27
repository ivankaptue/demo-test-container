package com.klid.demotestcontainer.repository;

import com.klid.demotestcontainer.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Ivan Kaptue
 */
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TaskRepositoryIntegrationTest {

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setup() {
        taskRepository.deleteAll();
    }

    @Test
    void givenNewTask_whenSave_thenTaskSaved() {
        var task = new Task("Create task");

        var createdTask = taskRepository.save(task);

        assertThat(createdTask).isNotNull();
        assertThat(createdTask.getId()).isNotNull();
    }

    @Test
    void givenTaskId_whenFindById_thenReturnTask() {
        var task = new Task("Create task");
        var createdTask = taskRepository.save(task);

        var foundTask = taskRepository.findById(createdTask.getId());

        assertThat(foundTask).isPresent();
    }

    @Test
    void givenTask_whenDelete_thenDeleteTask() {
        var task = new Task("Create task");
        var createdTask = taskRepository.save(task);

        taskRepository.delete(createdTask);

        var foundTask = taskRepository.findById(createdTask.getId());

        assertThat(foundTask).isEmpty();
    }

    @Test
    void givenTwoTasks_whenFindAll_thenReturnsAllTasks() {
        var task1 = new Task("Task 1");
        var task2 = new Task("Task 2");
        var task3 = new Task("Task 3");
        taskRepository.saveAll(List.of(task3, task1, task2));

        var taskPage = taskRepository.all(PageRequest.of(0, 5));

        assertThat(taskPage).isNotNull();

        var tasks = taskPage.getContent();
        assertThat(tasks).hasSize(3);
        assertThat(tasks.get(0).getTitle()).isEqualTo("Task 1");
        assertThat(tasks.get(1).getTitle()).isEqualTo("Task 2");
    }
}

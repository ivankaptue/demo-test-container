package com.klid.demotestcontainer.service;

import com.klid.demotestcontainer.model.Task;
import com.klid.demotestcontainer.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Ivan Kaptue
 */
@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional
    public String create(String title) {
        var task = new Task(title);
      task = taskRepository.save(task);
        return task.getId();
    }

    @Transactional
    public String update(String id, String title) {
        var task = taskRepository.findById(id).orElseThrow();
        task.setTitle(title);
        taskRepository.save(task);
        return task.getId();
    }

    @Transactional
    public void completeTask(String id) {
        var task = taskRepository.findById(id).orElseThrow();
        task.setStatus(Task.Status.DONE);
        taskRepository.save(task);
    }

    @Transactional
    public void unCompleteTask(String id) {
        var task = taskRepository.findById(id).orElseThrow();
        task.setStatus(Task.Status.PENDING);
        taskRepository.save(task);
    }

    @Transactional
    public void delete(String id) {
        var task = taskRepository.findById(id).orElseThrow();
        taskRepository.delete(task);
    }

    @Transactional(readOnly = true)
    public Page<Task> all(int page, int size) {
        return taskRepository.all(PageRequest.of(page, size));
    }

    @Transactional(readOnly = true)
    public Task getById(String id) {
        return taskRepository.findById(id).orElseThrow();
    }
}

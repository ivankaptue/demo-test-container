package com.klid.demotestcontainer.controller;

import com.klid.demotestcontainer.model.Task;
import com.klid.demotestcontainer.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Ivan Kaptue
 */
@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody String title) {
        var id = taskService.create(title);

        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable String id, @RequestBody String title) {
        var uuid = taskService.update(id, title);

        return ResponseEntity.ok(uuid);
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<Void> completeTask(@PathVariable String id) {
        taskService.completeTask(id);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/{id}/un-complete")
    public ResponseEntity<Void> unCompleteTask(@PathVariable String id) {
        taskService.unCompleteTask(id);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        taskService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<Task>> all(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        var tasks = taskService.all(page - 1, size);

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getById(@PathVariable String id) {
        var task = taskService.getById(id);

        return ResponseEntity.ok(task);
    }
}

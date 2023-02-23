package org.acme.example.controller;

import org.acme.example.repository.TodoRepository;
import org.acme.example.model.Todo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

@RestController
@RequestMapping(value="/api/todo")
public class TodoController {

    private static final Logger logger = LoggerFactory.getLogger(TodoController.class);
    private final TodoRepository todoRepository;

    public TodoController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @RequestMapping(method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getAllTodoItems() {
        logger.debug("GET request access '/api/todo' path.");
        try {
            List<Todo> todos = new ArrayList<>();
            Iterable<Todo> iterable = todoRepository.findAll();
            iterable.forEach(todos::add);
            return new ResponseEntity<>(todos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Nothing found", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addNewTodoItem(@RequestBody Todo item) {
        logger.debug("POST request access '/api/todo' path with item: {}", item);
        try {
            item.setId(UUID.randomUUID().toString());
            todoRepository.save(item);
            return new ResponseEntity<>("Entity created", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Entity creation failed", HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateTodoItem(@RequestBody Todo item) {
        logger.debug("PUT request access '/api/todo' path with item {}", item);
        try {
            Optional<Todo> todoItem = todoRepository.findById(item.getId());
            if (todoItem.isPresent()) {
                todoRepository.deleteById(item.getId());
                todoRepository.save(item);
                return new ResponseEntity<>("Entity updated", HttpStatus.OK);
            }
            return new ResponseEntity<>("Entity not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Edit errors: ", e);
            return new ResponseEntity<>("Update entity failed", HttpStatus.NOT_FOUND);
        }
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteTodoItem(@PathVariable("id") String id) {
        logger.debug("DELETE request access '/api/todo/{}' path.", id);
        try {
            Optional<Todo> todoItem = todoRepository.findById(id);
            if (todoItem.isPresent()) {
                todoRepository.deleteById(id);
                return new ResponseEntity<>("Entity deleted", HttpStatus.OK);
            }
            return new ResponseEntity<>("Not found the entity", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error occurred during delete: ", e);
            return new ResponseEntity<>("Deleting entity failed", HttpStatus.NOT_FOUND);
        }

    }
}

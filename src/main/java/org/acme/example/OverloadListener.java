package org.acme.example;

import org.acme.example.controller.TodoController;
import org.acme.example.model.Todo;
import org.acme.example.repository.TodoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.UUID;

@Component
@Order(1)
public class OverloadListener implements ApplicationListener<ApplicationReadyEvent> {

	private static final Logger logger = LoggerFactory.getLogger(OverloadListener.class);

	TodoController controller;

	TodoRepository repository;

	public OverloadListener(TodoController controller, TodoRepository repository) {
		this.controller = controller;
		this.repository = repository;
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		logger.debug("Code that triggers an OOM");
		while(true) {
			Todo todo = new Todo();
			for (int i = 0; i < 100; i++) {
				todo.setContent(String.format("The %d todo item ", i));
				todo.setOwner("one");
				controller.addNewTodoItem(todo);
			}

			List<UUID> ids = repository.findAll().stream().map(t -> t.getId()).toList();
			for (UUID id : ids) {
				todo.setContent("Another todo item " + todo.getContent()+id);
				controller.updateTodoItem(todo);
			}
		}
	}

}
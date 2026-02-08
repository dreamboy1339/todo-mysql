package com.asdf.todo.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.asdf.todo.entity.Todo;
import com.asdf.todo.repository.TodoInMemoryRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TodoServiceTests {

    @Autowired private TodoService todoService;

    @BeforeEach
    void setUp() {
        todoService = new TodoService(new TodoInMemoryRepository());
        todoService.save(new Todo(null, "Test Todo 1", "Description 1", false));
        todoService.save(new Todo(null, "Test Todo 2", "Description 2", false));
    }

    @AfterEach
    void tearDown() {}

    @Test
    void testFindAll() throws Exception {
        List<Todo> todos = todoService.findAll();

        assertThat(todos).hasSize(2);
    }

    @Test
    void testSaveTodo() throws Exception {
        Todo todo = new Todo(null, "New Todo", "New Description", false);
        todoService.save(todo);

        assertThat(todoService.findAll()).hasSize(3);
    }

    @Test
    void testFindById() throws Exception {
        Todo todo = todoService.findById(1L);

        assertThat(todo).isNotNull();
        assertThat(todo.getTitle()).isEqualTo("Test Todo 1");
    }

    @Test
    void testUpdateTodo() throws Exception {
        Todo updatedTodo = new Todo(1L, "Updated Todo", "Updated Description", true);
        todoService.update(1L, updatedTodo);
        Todo todo = todoService.findById(1L);

        assertThat(todo.getTitle()).isEqualTo("Updated Todo");
        assertThat(todo.getDescription()).isEqualTo("Updated Description");
        assertThat(todo.isCompleted()).isTrue();
    }

    @Test
    void testDeleteTodo() throws Exception {
        todoService.delete(1L);
        assertThat(todoService.findAll()).hasSize(1);
        assertThat(todoService.findById(1L)).isNull();
    }
}

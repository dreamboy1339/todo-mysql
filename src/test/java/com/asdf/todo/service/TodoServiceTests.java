package com.asdf.todo.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.asdf.todo.dto.TodoRequestDto;
import com.asdf.todo.dto.TodoResponseDto;
import com.asdf.todo.repository.TodoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

@SpringBootTest
@Testcontainers
@ExtendWith(SpringExtension.class)
public class TodoServiceTests {

    @Container
    public static MySQLContainer<?> mysqlContainer =
            new MySQLContainer<>("mysql:8.0.32")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
    }

    @Autowired private TodoService todoService;
    @Autowired private TodoRepository todoRepository;

    private Long todo1Id;
    private Long todo2Id;

    @BeforeEach
    void setUp() {
        todoRepository.deleteAll();
        todoService = new TodoService(todoRepository);
        todo1Id = todoService.save(new TodoRequestDto("Test Todo 1", "Description 1")).getId();
        todo2Id = todoService.save(new TodoRequestDto("Test Todo 2", "Description 2")).getId();
    }

    @Test
    void testFindAll() throws Exception {
        List<TodoResponseDto> todos = todoService.findAll();
        assertThat(todos).hasSize(2);
    }

    @Test
    void testSaveTodo() throws Exception {
        TodoRequestDto todo = new TodoRequestDto("New Todo", "New Description");
        todoService.save(todo);
        assertThat(todoService.findAll()).hasSize(3);
    }

    @Test
    void testFindById() throws Exception {
        TodoResponseDto todo = todoService.findById(todo1Id);
        assertThat(todo).isNotNull();
        assertThat(todo.getTitle()).isEqualTo("Test Todo 1");
    }

    @Test
    void testUpdateTodo() throws Exception {
        TodoRequestDto updatedTodo = new TodoRequestDto("Updated Todo", "Updated Description", true);
        todoService.update(todo1Id, updatedTodo);
        TodoResponseDto todo = todoService.findById(todo1Id);
        assertThat(todo.getTitle()).isEqualTo("Updated Todo");
        assertThat(todo.getDescription()).isEqualTo("Updated Description");
        assertThat(todo.isCompleted()).isTrue();
    }

    @Test
    void testDeleteTodo() throws Exception {
        todoService.delete(todo1Id);
        assertThat(todoService.findAll()).hasSize(1);
        assertThat(todoService.findById(todo1Id)).isNull();
    }
}

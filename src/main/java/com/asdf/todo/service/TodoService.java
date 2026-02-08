package com.asdf.todo.service;

import com.asdf.todo.dto.TodoRequestDto;
import com.asdf.todo.dto.TodoResponseDto;
import com.asdf.todo.entity.Todo;
import com.asdf.todo.repository.TodoRepository;
import com.asdf.todo.util.EntityDtoMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Transactional(readOnly = true)
    public List<TodoResponseDto> findAll() {
        return todoRepository.findAll().stream()
                .map(EntityDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TodoResponseDto findById(Long id) {
        return todoRepository.findById(id).map(EntityDtoMapper::toDto).orElse(null);
    }

    @Transactional
    public TodoResponseDto save(TodoRequestDto todoRequestDto) {
        Todo todo = EntityDtoMapper.toEntity(todoRequestDto);
        Todo savedTodo = todoRepository.save(todo);
        return EntityDtoMapper.toDto(savedTodo);
    }

    @Transactional
    public TodoResponseDto update(Long id, TodoRequestDto todoRequestDto) {
        Todo todo = EntityDtoMapper.toEntity(todoRequestDto);
        todo.setId(id);
        Todo updatedTodo = todoRepository.save(todo);
        return EntityDtoMapper.toDto(updatedTodo);
    }

    @Transactional
    public void delete(Long id) {
        todoRepository.deleteById(id);
    }
}

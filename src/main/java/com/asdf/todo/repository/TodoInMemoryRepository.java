package com.asdf.todo.repository;

import com.asdf.todo.entity.Todo;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

// 데이터에 접근하는 계층을 추상화하여 데이터 저장소와 상호 작용을 처리하는 리포지토리.
@Repository
public class TodoInMemoryRepository {
    private final Map<Long, Todo> todoMap = new HashMap<>();
    private final AtomicLong counter = new AtomicLong();

    public List<Todo> findAll() {
        return new ArrayList<>(todoMap.values());
    }

    public Todo findById(Long id) {
        return todoMap.get(id);
    }

    public Todo save(Todo todo) {
        if (todo.getId() == null) {
            todo.setId(counter.incrementAndGet());
        }
        todoMap.put(todo.getId(), todo);
        return todo;
    }

    public void deleteById(Long id) {
        todoMap.remove(id);
    }
}

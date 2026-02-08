package com.asdf.todo.repository;

import com.asdf.todo.entity.Todo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// 데이터에 접근하는 계층을 추상화하여 데이터 저장소와 상호 작용을 처리하는 리포지토리.
@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {}

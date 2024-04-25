package com.hodolog.hodolog.repository;

import com.hodolog.hodolog.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
}

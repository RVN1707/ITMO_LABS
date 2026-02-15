package com.example.backend.repository;

import com.example.backend.model.PointResult;
import com.example.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PointResultRepository extends JpaRepository<PointResult, Long> {
    List<PointResult> findByUserOrderByCreatedAtDesc(User user);
    void deleteByUser(User user);
    long countByUser(User user);
}
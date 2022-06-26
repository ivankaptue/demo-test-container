package com.klid.demotestcontainer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Ivan Kaptue
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
}

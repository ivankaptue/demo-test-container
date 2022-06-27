package com.klid.demotestcontainer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Ivan Kaptue
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, String> {

    @Query("SELECT t FROM Task t ORDER BY t.title ASC")
    Page<Task> all(Pageable pageable);
}

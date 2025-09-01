package org.example.db.News;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface NewsRepository extends JpaRepository<News, Long>{
    Page<News> findAll(Pageable pageable);
}

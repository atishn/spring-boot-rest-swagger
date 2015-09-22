package com.example.repo;

import com.example.model.Memo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Repository can be used to delegate CRUD operations against the data source: http://goo.gl/P1J8QH
 */
public interface MemoRepository extends PagingAndSortingRepository<Memo, Long> {
    /**
     * Find all.
     *
     * @param pageable the pageable
     * @return the page
     */
    Page findAll(Pageable pageable);
}
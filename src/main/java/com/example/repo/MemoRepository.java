package com.example.repo;

import com.example.model.Memo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Repository to delegate CRUD operations against the data source.
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

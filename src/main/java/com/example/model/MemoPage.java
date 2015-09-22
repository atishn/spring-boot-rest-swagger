package com.example.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * MemoPage Data Model.
 */
@Data
public class MemoPage implements Serializable {
    /**
     * The constant serialVersionUID.
     */
    private static final long serialVersionUID = 5846773639244185780L;
    /**
     * The Memo list.
     */
    private List<Memo> memoList;
    /**
     * The Total memos size.
     */
    private Long totalMemosSize;

    /**
     * The Total page size.
     */
    private Integer totalPages;

    /**
     * The Next page number.
     */
    private Integer nextPageNumber;
    /**
     * The Prev page number.
     */
    private Integer prevPageNumber;
}

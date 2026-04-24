package com.nurse.scheduling.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果
 *
 * @author nurse-scheduling
 */
@Data
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据列表
     */
    private List<T> list;

    /**
     * 总数
     */
    private Integer total;

    /**
     * 当前页码
     */
    private Integer page;

    /**
     * 每页条数
     */
    private Integer pageSize;

    /**
     * 是否有更多数据
     */
    private Boolean hasMore;

    public PageResult() {
    }

    public PageResult(List<T> list, Integer total, Integer page, Integer pageSize) {
        this.list = list;
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
        this.hasMore = page * pageSize < total;
    }
}

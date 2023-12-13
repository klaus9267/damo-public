package com.damo.server.domain.common.pagination;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Getter
public class CustomSchedulePage<T> extends PageImpl<T> {
    private final Integer totalAmount;

    public CustomSchedulePage(Page<T> page, Integer totalAmount) {
        super(page.getContent(), page.getPageable(), page.getTotalElements());
        this.totalAmount = totalAmount;
    }

    public CustomSchedulePage(List<T> content, Integer totalAmount) {
        super(content);
        this.totalAmount = totalAmount;
    }
}
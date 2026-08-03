package com.ailearn.common;

import lombok.Data;

@Data
public class PageParam {

    private Long current = 1L;
    private Long size = 10L;
}

package com.bubble.xds.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * @author zhoujingbo/Bob
 * @version 1.0
 * @date 2020/8/23
 */
@Data
public class StockRequest {

    private Integer id;

    private String flag;

    private Integer page;

    private Integer pageSize;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String createTime;

    private List<StockDetail> detailList;

}

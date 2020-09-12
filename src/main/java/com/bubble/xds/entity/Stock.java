package com.bubble.xds.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author zhoujingbo/Bob
 * @version 1.0
 * @date 2020/8/23
 */
@Data
@Table(name = "t_stock")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    private Integer id;

    @Column(columnDefinition = "create_time")
    private Date createTime;

    @Column(columnDefinition = "stock_num")
    private Integer stockNum;

    @Column(columnDefinition = "stock_total")
    private Double stockTotal;

    @Column(columnDefinition = "status")
    private String status;

    @Column(columnDefinition = "flag")
    private String flag;

}

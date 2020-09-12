package com.bubble.xds.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

/**
 * @author zhoujingbo/Bob
 * @version 1.0
 * @date 2020/8/23
 */
@Data
@Builder
@Table(name = "t_stock_detail")
public class StockDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    private Integer id;

    @Column(columnDefinition = "stock_id")
    private Integer stockId;

    @Column(columnDefinition = "current_label")
    private String currentLabel;

    @Column(columnDefinition = "current_value")
    private Integer currentValue;

    @Column(columnDefinition = "current_weight")
    private Double currentWeight;

    @Column(columnDefinition = "num")
    private Double num;

    @Column(columnDefinition = "weight")
    private Double weight;

    @Column(columnDefinition = "total_weight")
    private Double totalWeight;

    @Column(columnDefinition = "piece_num")
    private Double pieceNum;

    @Column(columnDefinition = "total")
    private Double total;

}

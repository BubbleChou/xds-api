package com.bubble.xds.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author zhoujingbo/Bob
 * @version 1.0
 * @date 2020/8/23
 */
@Data
@Table(name = "sys_code")
public class SysCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    private Integer id;

    @Column(columnDefinition = "type")
    private String type;

    @Column(columnDefinition = "label")
    private String label;

    @Column(columnDefinition = "sort_label")
    private String sortLabel;

    @Column(columnDefinition = "value")
    private String value;

    @Column(columnDefinition = "weight")
    private String weight;


}

package com.bubble.xds.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author zhoujingbo/Bob
 * @version 1.0
 * @date 2020/8/23
 */
@Data
public class SysCodeRequest {

    private Integer id;

    private String type;

    private String label;

    private String value;

    private String weight;

    private Integer page;

    private Integer pageSize;

}

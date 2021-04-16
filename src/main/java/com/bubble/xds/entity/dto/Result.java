package com.bubble.xds.entity.dto;

import lombok.Data;

/**
 * @author zhoujingbo/Bob
 * @version 1.0
 * @date 2020/8/23
 */
@Data
public class Result<T> {

    private String code;
    private String msg;
    private boolean success;
    private T data;

    public static Result create(){
        return new Result();
    }

    public Result success(){
        this.success = true;
        return this;
    }

    public Result success(T data){
        this.success = true;
        this.data = data;
        return this;
    }

    public Result failure(String code, String Msg){
        this.success = false;
        this.code = code;
        this.msg = Msg;
        return this;
    }


}

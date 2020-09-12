package com.bubble.xds.service;

import com.bubble.xds.entity.SysCodeRequest;
import com.bubble.xds.entity.dto.Result;

/**
 * @author zhoujingbo/Bob
 * @version 1.0
 * @date 2020/8/23
 */
public interface SysCodeService {

    /**
     * 查询码表
     * @param sysCodeRequest
     */
    Result getCodeList(SysCodeRequest sysCodeRequest);

    /**
     * 保存码表
     * @param sysCodeRequest
     */
    Result saveCode(SysCodeRequest sysCodeRequest);

    /**
     * 删除码表
     * @param sysCodeRequest
     */
    Result deleteCode(SysCodeRequest sysCodeRequest);
}

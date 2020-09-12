package com.bubble.xds.service;

import com.bubble.xds.entity.StockRequest;
import com.bubble.xds.entity.dto.Result;

import javax.servlet.http.HttpServletResponse;

/**
 * @author zhoujingbo/Bob
 * @version 1.0
 * @date 2020/8/23
 */

public interface IngredientsService {

    /**
     * 保存进货详情
     * @param stockRequest
     * @return
     */
    Result saveStockDetail(StockRequest stockRequest);

    /**
     * 查询进货
     * @param stockRequest
     * @return
     */
    Result getStock(StockRequest stockRequest);

    /**
     * 查询进货详情
     * @param stockRequest
     * @return
     */
    Result getDetail(StockRequest stockRequest);

    /**
     * 重新编辑
     * @param stockRequest
     * @return
     */
    Result reEditDetail(StockRequest stockRequest);

    /**
     * 重新编辑
     * @param stockRequest
     */
    void exportExcel(StockRequest stockRequest, HttpServletResponse response);

}

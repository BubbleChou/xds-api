package com.bubble.xds.ctrl;

import com.bubble.xds.entity.StockRequest;
import com.bubble.xds.entity.SysCodeRequest;
import com.bubble.xds.entity.dto.Result;
import com.bubble.xds.service.IngredientsService;
import com.bubble.xds.service.SysCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author zhoujingbo/Bob
 * @version 1.0
 * @date 2020/8/23
 */
@RestController
@RequestMapping("ingredients")
public class IngredientsCtrl {

    @Autowired
    private IngredientsService ingredientsService;

    @Autowired
    private SysCodeService sysCodeService;

    @PostMapping("/getCodeList")
    public Result getCodeList(@RequestBody SysCodeRequest sysCode) {
        return sysCodeService.getCodeList(sysCode);
    }

    @PostMapping("/saveCode")
    public Result saveCode(@RequestBody SysCodeRequest sysCodeRequest) {
        return sysCodeService.saveCode(sysCodeRequest);
    }

    @PostMapping("/deleteCode")
    public Result deleteCode(@RequestBody SysCodeRequest sysCodeRequest) {
        return sysCodeService.deleteCode(sysCodeRequest);
    }

    @PostMapping("/saveDetail")
    public Result saveDetail(@RequestBody StockRequest stockRequest) {
        return ingredientsService.saveStockDetail(stockRequest);
    }

    @PostMapping("/getStock")
    public Result getStock(@RequestBody StockRequest stockRequest) {
        return ingredientsService.getStock(stockRequest);
    }

    @PostMapping("/getDetail")
    public Result getDetail(@RequestBody StockRequest stockRequest) {
        return ingredientsService.getDetail(stockRequest);
    }

    @PostMapping("/reEditDetail")
    public Result reEditDetail(@RequestBody StockRequest stockRequest) {
        return ingredientsService.reEditDetail(stockRequest);
    }

    @PostMapping("/exportExcel")
    public void exportExcel(@RequestBody StockRequest stockRequest, HttpServletResponse response) {
        ingredientsService.exportExcel(stockRequest, response);
    }

}


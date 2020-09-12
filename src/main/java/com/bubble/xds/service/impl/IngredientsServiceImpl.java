package com.bubble.xds.service.impl;

import com.bubble.xds.dao.StockDetailMapper;
import com.bubble.xds.dao.StockMapper;
import com.bubble.xds.entity.Stock;
import com.bubble.xds.entity.StockDetail;
import com.bubble.xds.entity.StockRequest;
import com.bubble.xds.entity.dto.Result;
import com.bubble.xds.service.IngredientsService;
import com.bubble.xds.util.ExcelUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @author zhoujingbo/Bob
 * @version 1.0
 * @date 2020/8/23
 */
@Service
public class IngredientsServiceImpl implements IngredientsService {

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private StockDetailMapper stockDetailMapper;

    private String NUM_0 = "0";

    private String NUM_1 = "1";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result saveStockDetail(StockRequest stockRequest) {
        Result result = Result.create();
        // 新增
        Example example = new Example(Stock.class);
        Date date = new Date();
        example.createCriteria().andEqualTo("createTime", new SimpleDateFormat("yyyy-MM-dd").format(date));
        Stock stock = stockMapper.selectOneByExample(example);
        if (stock == null) {
            stock = new Stock();
            stock.setCreateTime(date);
            stockMapper.insertSelective(stock);
        }
        Integer stockId = stock.getId();
        List<StockDetail> detailList = stockRequest.getDetailList();
        AtomicBoolean flag = new AtomicBoolean(false);
        // 保存
        if (NUM_0.equals(stockRequest.getFlag())) {
            if (NUM_1.equals(stock.getStatus())) {
                return result.failure("666", "保存失败，今天已录入进货单，如需更改详情请重新编辑！");
            }
            // 新增detail
            detailList.forEach(detail -> {
                if (detail.getId() != null) {
                    stockDetailMapper.updateByPrimaryKeySelective(detail);
                } else {
                    detail.setStockId(stockId);
                    stockDetailMapper.insert(detail);
                }
            });
        } else {
            // 合并重复
            List<StockDetail> finalDetail = new ArrayList<>(detailList.stream().collect(Collectors.toMap(StockDetail::getCurrentLabel, stockDetail -> stockDetail, (oldStock, newStock) -> {
                // 判断单价
                if (!oldStock.getCurrentValue().equals(newStock.getCurrentValue()) || !oldStock.getCurrentWeight().equals(newStock.getCurrentWeight())) {
                    flag.set(true);
                }
                oldStock.setNum(oldStock.getNum() + newStock.getNum());
                oldStock.setTotalWeight(oldStock.getTotalWeight() + newStock.getTotalWeight());
                oldStock.setTotal(oldStock.getTotal() + newStock.getTotal());
                oldStock.setWeight(oldStock.getTotalWeight() / oldStock.getNum());
                oldStock.setPieceNum(oldStock.getTotalWeight() / oldStock.getCurrentWeight());
                return oldStock;
            })).values());
            if (flag.get()) {
                return result.failure("666", "提交失败，存在进货单价或者菜品重量不一样的相同品类！");
            }
            // 删除旧detail
            example = new Example(StockDetail.class);
            example.createCriteria().andEqualTo("stockId", stockId);
            stockDetailMapper.deleteByExample(example);
            finalDetail.forEach(detail -> {
                detail.setStockId(stockId);
                stockDetailMapper.insert(detail);
            });
            // 更新总数
            double count = finalDetail.stream().mapToDouble(StockDetail::getTotal).sum();
            stock.setStockNum(finalDetail.size());
            stock.setStockTotal(count);
            stock.setStatus("1");
        }
        stockMapper.updateByPrimaryKeySelective(stock);
        return result.success();
    }

    @Override
    public Result getStock(StockRequest stockRequest) {
        Result result = Result.create();
        Example example = new Example(Stock.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtil.isNotEmpty(stockRequest.getCreateTime())) {
            criteria.andEqualTo("createTime", stockRequest.getCreateTime());
        }
        if (stockRequest.getPage() != null && stockRequest.getPageSize() != null) {
            PageHelper.startPage(stockRequest.getPage(), stockRequest.getPageSize());
        } else {
            PageHelper.startPage(1, 0);
        }
        List<Stock> stocks = stockMapper.selectByExample(example);
        PageInfo<Stock> page = new PageInfo<>(stocks);
        return result.success(page);
    }

    @Override
    public Result getDetail(StockRequest stockRequest) {
        Result result = Result.create();
        Example example = new Example(StockDetail.class);
        example.createCriteria().andEqualTo("stockId", stockRequest.getId());
        List<StockDetail> stockDetails = stockDetailMapper.selectByExample(example);
        return result.success(stockDetails);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result reEditDetail(StockRequest stockRequest) {
        Result result = Result.create();
        Stock stock = new Stock();
        stock.setId(stockRequest.getId());
        stock.setStatus("0");
        stockMapper.updateByPrimaryKeySelective(stock);
        return result.success();
    }

    @Override
    public void exportExcel(StockRequest stockRequest, HttpServletResponse response) {
        Example example = new Example(StockDetail.class);
        example.createCriteria().andEqualTo("stockId", stockRequest.getId());
        List<StockDetail> stockDetails = stockDetailMapper.selectByExample(example);
        // 添加总数 数据
        double total = stockDetails.stream().mapToDouble(StockDetail::getTotal).sum();
        double weight = stockDetails.stream().mapToDouble(StockDetail::getTotalWeight).sum();
        double num = stockDetails.stream().mapToDouble(StockDetail::getNum).sum();
        StockDetail stockDetail = StockDetail.builder().currentLabel("总计").total(total).totalWeight(weight).num(num).build();
        stockDetails.add(stockDetail);
        String[] headers = new String[]{"品名", "菜品重量/kg", "数量", "单价重量/kg", "总重量/kg", "份数", "单价", "总价"};
        String[] fields = new String[]{"getCurrentLabel", "getCurrentWeight", "getNum", "getWeight", "getTotalWeight", "getPieceNum", "getCurrentValue", "getTotal"};
        ExcelUtil.exportExcel(stockDetails, fields, headers, StockDetail.class, response);
    }

}

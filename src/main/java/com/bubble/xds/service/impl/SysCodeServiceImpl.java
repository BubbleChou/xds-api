package com.bubble.xds.service.impl;

import com.bubble.xds.dao.SysCodeMapper;
import com.bubble.xds.entity.SysCode;
import com.bubble.xds.entity.SysCodeRequest;
import com.bubble.xds.entity.dto.Result;
import com.bubble.xds.service.SysCodeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.util.List;

/**
 * @author zhoujingbo/Bob
 * @version 1.0
 * @date 2020/8/23
 */
@Service
@Slf4j
public class SysCodeServiceImpl implements SysCodeService {

    @Autowired
    private SysCodeMapper sysCodeMapper;

    @Override
    public Result getCodeList(SysCodeRequest sysCodeRequest) {
        Result result = Result.create();
        Example example = new Example(SysCode.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtil.isNotEmpty(sysCodeRequest.getType())) {
            criteria.andLike("type", "%" + sysCodeRequest.getType() + "%");
        }
        if (StringUtil.isNotEmpty(sysCodeRequest.getLabel())) {
            criteria.andLike("label", "%" + sysCodeRequest.getLabel() + "%");
        }
        if (sysCodeRequest.getPage() != null && sysCodeRequest.getPageSize() != null) {
            PageHelper.startPage(sysCodeRequest.getPage(), sysCodeRequest.getPageSize());
        } else {
            PageHelper.startPage(1,0);
        }
        List<SysCode> sysCodes = sysCodeMapper.selectByExample(example);
        PageInfo<SysCode> page = new PageInfo<>(sysCodes);
        return result.success(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result saveCode(SysCodeRequest sysCodeRequest) {
        Result result = Result.create();
        SysCode sysCode = new SysCode();
        BeanUtils.copyProperties(sysCodeRequest, sysCode);
        // 获取首字母
        StringBuilder pinyinStr = new StringBuilder();
        //转为单个字符
        char[] newChar = sysCode.getLabel().toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (char c : newChar) {
            if (c > 128) {
                try {
                    pinyinStr.append(PinyinHelper.toHanyuPinyinStringArray(c, defaultFormat)[0].charAt(0));
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    log.error(e.getMessage());
                }
            } else {
                pinyinStr.append(c);
            }
        }
        sysCode.setSortLabel(pinyinStr.toString());
        if (sysCodeRequest.getId() != null) {
            sysCodeMapper.updateByPrimaryKey(sysCode);
        } else {
            sysCodeMapper.insert(sysCode);
        }
        return result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result deleteCode(SysCodeRequest sysCodeRequest) {
        Result result = Result.create();
        sysCodeMapper.deleteByPrimaryKey(sysCodeRequest.getId());
        return result.success();
    }
}

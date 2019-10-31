package com.restful.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.restful.poi.model.Excel;
import com.restful.system.mapper.ExcelMapper;
import com.restful.system.service.IExcelService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description xxx
 * @date 2019-09-28 17:19
 */
@Service
public class ExcelServiceImpl extends ServiceImpl<ExcelMapper, Excel> implements IExcelService {
    @Override
    public List<Excel> selectByDate() {
        return baseMapper.selectByDate();
    }
}

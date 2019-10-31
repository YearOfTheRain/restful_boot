package com.restful.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.restful.poi.model.Excel;

import java.util.List;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description xxx
 * @date 2019-09-28 17:18
 */
public interface IExcelService extends IService<Excel> {

    List<Excel> selectByDate();
}

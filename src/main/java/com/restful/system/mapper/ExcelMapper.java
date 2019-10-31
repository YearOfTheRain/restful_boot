package com.restful.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.restful.poi.model.Excel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description ddd
 * @date 2019-09-28 17:18
 */
@Repository
@Mapper
public interface ExcelMapper extends BaseMapper<Excel> {

    List<Excel> selectByDate();
}

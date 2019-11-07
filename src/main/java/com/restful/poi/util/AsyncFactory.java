package com.restful.poi.util;

import com.restful.common.spring.SpringUtils;
import com.restful.poi.model.Excel;
import com.restful.poi.model.ExcelHead;
import com.restful.poinew.CastType;
import com.restful.system.service.IExcelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimerTask;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description 异步工厂（产生任务）
 * @date 2019-10-10 11:10
 */
@Slf4j
public class AsyncFactory {


    /** 
     * 方法描述: 异步解析 excel 完成后记录 
     *
     * @param 
     * @return java.util.TimerTask 
     * @author LiShuLin
     * @date 2019/10/10
     */
    public static TimerTask resolveExcel(InputStream in, String fileName, List<ExcelHead> excelHeads) {
        return new TimerTask() {
            @Override
            public void run() {
                System.out.println("开始解析excel文件 ：" + fileName);
                log.info("开始解析excel文件 ：" + fileName);
                int size = 0;
                try {
                    List<Excel> list = PoiUtils.readExcelToEntity(Excel.class, in, fileName, excelHeads);
                    size = list.size();
                    list.forEach(excel -> SpringUtils.getBean(IExcelService.class).saveOrUpdate(excel) );
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                log.info("解析excel文件结束，共取得 " + size + " 条数据");
                System.out.println("解析excel文件结束，共取得 " + size + " 条数据");
            }
        };
    }

    /**
     * 方法描述: 异步解析 excel 完成后记录
     *
     * @param
     * @return java.util.TimerTask
     * @author LiShuLin
     * @date 2019/10/10
     */
    public static TimerTask resolveExcel(MultipartFile file, List<ExcelHead> excelHeads) {
        return new TimerTask() {
            @Override
            public void run() {
                log.info("开始解析excel文件 ：" + file.getOriginalFilename());
                try (InputStream in = file.getInputStream()) {
                    PoiUtils.readExcelToEntity(Excel.class, in, file.getOriginalFilename(), excelHeads);
                } catch (Exception e) {
                    log.error("转 InputStream 失败，{}", e.getMessage());
                    e.printStackTrace();
                }
            }
        };
    }

    /**
     * 方法描述: 分段保存数据
     *
     * @param list 待保存的数据
     * @return java.util.TimerTask
     * @author LiShuLin
     * @date 2019/10/10
     */
    public static TimerTask saveData(List<Excel> list) {
        Set<Excel> excelSet = new HashSet<>(list);
        log.info("取得 " + list.size() + " 条数据");
        log.info("取得 " + excelSet.size() + " 条不重复数据");
        return new TimerTask() {
            @Override
            public void run() {
                SpringUtils.getBean(IExcelService.class).saveOrUpdateBatch(excelSet);
            }
        };
    }

    /**
     * 方法描述: 分段保存数据
     *
     * @param list 待保存的数据
     * @return java.util.TimerTask
     * @author LiShuLin
     * @date 2019/10/10
     */
    public static TimerTask saveData(Set<Excel> list) {
        Set<Excel> excelSet = new HashSet<>(list);
        log.info("取得 " + excelSet.size() + " 条不重复数据");
        return new TimerTask() {
            @Override
            public void run() {
                SpringUtils.getBean(IExcelService.class).saveOrUpdateBatch(excelSet);
            }
        };
    }

    /**
     * 方法描述: 分段保存数据
     *
     * @param list 待保存的数据
     * @return java.util.TimerTask
     * @author LiShuLin
     * @date 2019/10/10
     */
    public static TimerTask saveDataString(List<String> list) {
        Excel excel = CastType.string2Excel(list);
        return new TimerTask() {
            @Override
            public void run() {
                SpringUtils.getBean(IExcelService.class).saveOrUpdate(excel);
            }
        };
    }
}

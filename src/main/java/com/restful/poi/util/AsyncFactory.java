package com.restful.poi.util;

import com.restful.common.spring.SpringUtils;
import com.restful.poi.model.Excel;
import com.restful.poinew.CastType;
import com.restful.poinew.ExcelReader;
import com.restful.system.service.IExcelService;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

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
    public static TimerTask saveData(Set<Excel> list, Set<String> orderId) {
        Set<Excel> excelSet = new TreeSet<>(list);
        List<String> orderIdList = new ArrayList<>(orderId);
        log.info("取得 " + excelSet.size() + " 条不重复数据");
        return new TimerTask() {
            @Override
            public void run() {
                IExcelService service = SpringUtils.getBean(IExcelService.class);
                List<String> hasExistList = service.selectByIdList(orderIdList);
                List<Excel> excelList = new ArrayList<>(excelSet);
                int excelListSize = excelList.size();
                //针对没有重复数据的情况，那么可以直接入库新增
                if (hasExistList.size() < 1) {
                    service.saveBatch(excelList);
                    return;
                }
                //相等则说明全是重复数据，那么直接做更新操作
                if (hasExistList.size() == excelListSize) {
                    service.updateBatchById(excelList);
                    return;
                }

                List<Excel> addList = new ArrayList<>(ExcelReader.BATCH_NUMBER);
                List<Excel> updateList = new ArrayList<>(ExcelReader.BATCH_NUMBER);
                for (int i = 0, j = 0; i < excelListSize; i++) {
                    if (!Objects.equals(excelList.get(i).getOrderId(), hasExistList.get(j))) {
                        addList.add(excelList.get(i));
                        continue;
                    }

                    updateList.add(excelList.get(i));
                    j++;
                    //如果重复项都已匹配完，那么剩下的就是不重复的，就可以执行新增入库
                    if (i + 1 <excelListSize && j == hasExistList.size()) {
                        List<Excel> excels = excelList.subList(i + 1, excelListSize);
                        service.saveBatch(new ArrayList<>(excels));
                        break;
                    }

                }
                service.saveBatch(addList);
                service.updateBatchById(updateList);
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

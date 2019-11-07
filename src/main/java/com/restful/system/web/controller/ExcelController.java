package com.restful.system.web.controller;

import com.restful.common.core.ResponseEntity;
import com.restful.poi.model.Excel;
import com.restful.poi.model.ExcelExport;
import com.restful.poi.model.ExcelHead;
import com.restful.poi.util.PoiUtils;
import com.restful.poinew.ExcelReaderUtil;
import com.restful.system.service.IExcelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description excel 数据导入
 * @date 2019-09-29 10:11
 */
@RestController
@RequestMapping("/api/excel")
@Slf4j
public class ExcelController implements Cloneable {

    @Autowired
    private IExcelService excelService;

    @PostMapping("/import")
    public ResponseEntity excelImport(@RequestParam MultipartFile file){

        log.info("size: {}K", (file.getSize() / 1024));

        //中文表头与 entity 对应
        List<ExcelHead> excelHeads = new ArrayList<>(50);
        String[] entityName = {"orderId", "onlineOrderId", "shopId", "shopName", "buyerName", "orderTime", "buyTime", "sendTime", "shouldPay", "hasPay", "discountPay", "fare", "status", "shopStatus", "errorType", "courierCompany", "trackingNumber", "consigneeName", "province", "city", "district", "street", "address", "mobilePhone", "fixedTelephone", "orderType", "buyerMessage", "orderRemark", "sendStorehouse", "tag", "payNumber", "platform", "childOrderNumber", "originalOnlineOrder", "styleNumber", "produceNumber", "produceName", "colorAndFormat", "amount", "producePrice", "produceMoney", "gift", "childOrderStatus", "costPrice", "resendAmount", "resendAmountReal", "resendMoney"};
        String[] excelName = {"订单号", "线上订单号", "店铺编号", "店铺", "买家账号", "下单时间", "付款日期", "发货日期", "应付金额", "已付金额", "抵扣金额", "运费", "状态", "店铺状态", "异常类型", "快递公司", "快递单号", "收货人姓名", "省份", "城市", "区县", "街道", "地址", "手机", "固话", "订单类型", "买家留言", "订单备注", "发货仓", "标签", "支付单号", "平台站点", "子订单编号", "原始线上订单号", "款号", "商品编码", "商品名称", "颜色及规格", "数量", "商品单价", "商品金额", "是否赠品", "子订单状态", "成本价", "申请退货数量", "实退数量", "订单退款金额"};
        for (int i = 0; i < excelName.length; i++) {
            excelHeads.add(new ExcelHead(excelName[i], entityName[i]));
        }
        //异步执行解析并入库
//        AsyncManager.getInstance().execute(AsyncFactory.resolveExcel(file, excelHeads));
        log.info("开始解析excel文件 ：" + file.getOriginalFilename());
        long start = System.currentTimeMillis();
        try (InputStream in = file.getInputStream()) {
            PoiUtils.readExcelToEntity(Excel.class, in, file.getOriginalFilename(), excelHeads);
        } catch (Exception e) {
            log.error("转 InputStream 失败，{}", e.getMessage());
            e.printStackTrace();
        }
        long time = System.currentTimeMillis() - start;
        return ResponseEntity.success("花费 " + time + " 毫秒来解析excel");
    }
    @PostMapping("/importNew")
    public ResponseEntity excelImportNew(@RequestParam MultipartFile file){

        log.info("size: {}K", (file.getSize() / 1024));
        long start = System.currentTimeMillis();
        try(InputStream inputStream = file.getInputStream()) {
            ExcelReaderUtil.readExcel(inputStream, (int sheetIndex, int totalRowCount, int curRow, List<String> cellList) -> {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.success("花费 " + (System.currentTimeMillis() - start) + " 毫秒来解析excel");
    }



    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        List<Excel> excelList = excelService.selectByDate();
        ExcelExport excelExport = new ExcelExport(response, "execlExport", "sheet1");
        excelExport.writeExcel(PoiUtils.ENTITY_NAME, PoiUtils.EXCEL_NAME , PoiUtils.SIZE, excelList);
    }
}

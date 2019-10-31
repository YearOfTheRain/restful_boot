package com.restful.system.service.impl;

import com.restful.poi.model.Excel;
import com.restful.poi.model.ExcelHead;
import com.restful.poi.util.PoiUtils;
import com.restful.system.service.IExcelService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExcelServiceImplTest {

    @Autowired
    private IExcelService excelService;

    @Test
    public void add() {
        File file = new File("D:\\小程序ERP导入字段BBBig-nosame.xlsx");
        FileInputStream in = null;
        StringBuilder stringBuilder = new StringBuilder();
        long start = System.currentTimeMillis();
        System.out.println("------------------开始---------------------");
        Integer size = 0;
        try {
            in = new FileInputStream(file);
            List<ExcelHead> excelHeads = new ArrayList<ExcelHead>();
            String[] entityName = {"orderId","onlineOrderId","shopId","shopName","buyerName","orderTime","buyTime","sendTime","shouldPay","hasPay","discountPay","fare","status","shopStatus","errorType","courierCompany","trackingNumber","consigneeName","province","city","district","street","address","mobilePhone","fixedTelephone","orderType","buyerMessage","orderRemark","sendStorehouse","tag","payNumber","platform","childOrderNumber","originalOnlineOrder","styleNumber","produceNumber","produceName","colorAndFormat","amount","producePrice","produceMoney","gift","childOrderStatus","costPrice","resendAmount","resendAmountReal","resendMoney"};
            String[] excelName= {"订单号",    "线上订单号" , "店铺编号","店铺"  ,"买家账号" ,"下单时间" ,"付款日期","发货日期","应付金额","已付金额","抵扣金额" ,"运费","状态"  ,"店铺状态"  ,"异常类型" ,"快递公司"      ,"快递单号"      ,"收货人姓名"   ,"省份"    ,"城市","区县"    ,"街道"  ,"地址"   ,"手机"       ,"固话"          ,"订单类型","买家留言" ,"订单备注"   ,"发货仓"        ,"标签","支付单号","平台站点","子订单编号"      ,"原始线上订单号"     ,"款号"       ,"商品编码"     ,"商品名称"   ,"颜色及规格"    ,"数量"  ,"商品单价"    ,"商品金额"    ,"是否赠品","子订单状态"  ,"成本价"   ,"申请退货数量","实退数量","订单退款金额"};
            for (int i = 0; i< excelName.length; i++) {
                excelHeads.add(new ExcelHead(excelName[i], entityName[i]));
            }
            List<Excel> list = PoiUtils.readExcelToEntity(Excel.class, file, file.getName(), excelHeads);
            stringBuilder.append("解析excel文件，花费" + (System.currentTimeMillis() - start) + "毫秒\n");
            size = list.size();
            long sqlStart = System.currentTimeMillis();
            /*list.forEach(excel -> excelService.saveOrUpdate(excel));*/
            excelService.saveOrUpdateBatch(list, 5000);

            stringBuilder.append("执行sql，花费" + (System.currentTimeMillis() - sqlStart) + "毫秒\n");
            stringBuilder.append("共取得" + size + "条数据，执行完毕共花费" + (System.currentTimeMillis() - start) + "毫秒");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(in!=null) {
                try {
                    in.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        System.out.println("------------------结束---------------------");
        System.out.println(stringBuilder.toString());
    }

    @Test
    public void testForeach() {
        long start1 = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 1000; j++) {
                int jjjj = j + i + 1;
            }
        }
        System.out.println("耗时:" + (System.currentTimeMillis() - start1));
        long start2 = System.currentTimeMillis();
        for (int i1 = 0; i1 < 1000; i1++) {
            for (int j1 = 0; j1 < 100; j1++) {
                int jjjj = j1 + i1 + 1;
            }
        }
        System.out.println("耗时:" + (System.currentTimeMillis() - start2));
    }

    @Test
    public void forEach() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            int j = i;
        }
        System.out.println("time :" + PoiUtils.getRunningTime(start));
        long startNew = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            if (i > 10000) {
                continue;
            }
            int j = i;
        }
        System.out.println("time :" + PoiUtils.getRunningTime(startNew));
    }

}
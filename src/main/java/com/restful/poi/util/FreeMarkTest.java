package com.restful.poi.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description freemarker 测试
 * @date 2019-10-08 16:22
 */
public class FreeMarkTest {

    public static void main(String[] args) throws IOException {
        Random random = new Random();
        Map dataMap = new HashMap(8);
        dataMap.put("name", "孟姜女");
        dataMap.put("reason", "老公被抓去修长城");
        List<Map<String, Object>> dataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> userMap = new HashMap<>(8);
            userMap.put("name", "张三" + i);
            userMap.put("sex", (i & 1) == 1 ? "男" : "女");
            userMap.put("age", random.nextInt(70) + 1);
            userMap.put("profession", "法师" + i);
            userMap.put("score", random.nextInt(4) + 1);
            dataList.add(userMap);
        }
        dataMap.put("userList", dataList);
        //获取服务根路径
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        Configuration configuration = new Configuration();
        configuration.setDirectoryForTemplateLoading(new File(path + "/ftl"));
        configuration.setEncoding(Locale.getDefault(), "UTF-8");
        Template template = configuration.getTemplate("告大人书.ftl");
        Template templateExcel = configuration.getTemplate("excel.ftl");
        File f = new File("D:\\new\\new.docx");
        FileOutputStream outputStream = new FileOutputStream(f);
        BufferedWriter wirteur = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8), 10240);
        try {
            template.process(dataMap, wirteur);
        } catch (TemplateException e) {
            e.printStackTrace();
        } finally {
            wirteur.flush();
            wirteur.close();
        }

        File excel = new File("D:\\new\\newExcel.xlsx");
        FileOutputStream outputStreamExcel = new FileOutputStream(excel);
        BufferedWriter wirteurExcel = new BufferedWriter(new OutputStreamWriter(outputStreamExcel, StandardCharsets.UTF_8), 10240);
        try {
            templateExcel.process(dataMap, wirteurExcel);
        } catch (TemplateException e) {
            e.printStackTrace();
        } finally {
            wirteurExcel.flush();
            wirteurExcel.close();
        }
    }
}

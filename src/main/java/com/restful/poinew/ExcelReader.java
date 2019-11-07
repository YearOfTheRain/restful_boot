package com.restful.poinew;

import com.restful.common.thread.manage.AsyncManager;
import com.restful.poi.model.Excel;
import com.restful.poi.util.AsyncFactory;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Shulin Li
 * @version v1.0
 * @program restful_boot
 * @description excel 辅助类
 * @date 2019-11-06 16:00
 */
public class ExcelReader extends DefaultHandler {


    private ExcelReadDataDelegated excelReadDataDelegated;

    public ExcelReader(ExcelReadDataDelegated excelReadDataDelegated) {
        this.excelReadDataDelegated = excelReadDataDelegated;
    }

    /**
     * 单元格中的数据可能的数据类型
     */
    enum CellDataType {
        /*** 依次对应 excel类型*/
        BOOL, ERROR, FORMULA, INLINESTR, SSTINDEX, NUMBER, DATE, NULL
    }

    /**
     * 共享字符串表
     */
    private SharedStringsTable sst;

    /**
     * 上一次的索引值
     */
    private String lastIndex;

    /**
     * 工作表索引
     */
    private int sheetIndex = 0;

    /**
     * 总行数
     */
    private int totalRows = 0;

    /**
     * 判断整行是否为空行的标记
     */
    private boolean flag = false;

    /**
     * 当前行
     */
    private int curRow = 1;

    /**
     * 当前列
     */
    private int curCol = 0;

    /**
     * 单元格数据类型，默认为字符串类型
     */
    private CellDataType nextDataType = CellDataType.SSTINDEX;

    private final DataFormatter formatter = new DataFormatter();

    /**
     * 单元格日期格式的索引
     */
    private short formatIndex;

    /**
     * 日期格式字符串
     */
    private String formatString;

    /**
     * 定义前一个元素和当前元素的位置，用来计算其中空的单元格数量，如A6和A8等
     * 新增加一个 preTempRef ，用来保存前一个元素的缓存，目的是为了准确检验 空值
     */
    private String preRef = null, ref = null, preTempRef = null;

    /**
     * 定义该文档一行最大的单元格数，用来补全一行最后可能缺失的单元格
     */
    private String maxRef = null;

    /**
     * 单元格
     */
    private StylesTable stylesTable;

    /**
     * 总行号
     */
    private Integer totalRowCount;

    private Field[] fields = Excel.class.getDeclaredFields();

    private Excel excel = null;

    private Set<Excel> excelSet = new HashSet<>(1024);

    private int processCore(OPCPackage pkg) throws Exception {
        XSSFReader xssfReader = new XSSFReader(pkg);
        stylesTable = xssfReader.getStylesTable();
        SharedStringsTable sst = xssfReader.getSharedStringsTable();
        XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
        this.sst = sst;
        parser.setContentHandler(this);
        XSSFReader.SheetIterator sheets = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
        while (sheets.hasNext()) {
            //标记初始行为第一行
            curRow = 1;
            sheetIndex++;
            //sheets.next()和sheets.getSheetName()不能换位置，否则sheetName报错
            InputStream sheet = sheets.next();
            InputSource sheetSource = new InputSource(sheet);
            //解析excel的每条记录，在这个过程中startElement()、characters()、endElement()这三个函数会依次执行
            parser.parse(sheetSource);
            sheet.close();
        }
        //返回该excel文件的总行数，不包括首列和空行
        return totalRows;
    }

    /**
     * 遍历工作簿中所有的电子表格
     * 并缓存在mySheetList中
     *
     * @param filename
     * @throws Exception
     */
    int process(String filename) throws Exception {
        return processCore(OPCPackage.open(filename));
    }

    /**
     * 遍历工作簿中所有的电子表格
     * 并缓存在mySheetList中
     *
     * @param inputStream 输入流
     * @throws Exception
     */
    int process(InputStream inputStream) throws Exception {
        return processCore(OPCPackage.open(inputStream));
    }

    /**
     * 第一个执行
     *
     * @param uri
     * @param localName
     * @param name
     * @param attributes
     * @throws SAXException
     */
    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {

        // 获取总行号  格式： A1:B5    取最后一个值即可(从 : 后第三位开始截取)
        if ("dimension".equals(name)) {
            String dimensionStr = attributes.getValue("ref");
            totalRowCount = Integer.parseInt(dimensionStr.substring(dimensionStr.indexOf(":") + 3)) - 1;
        }

        //c => 单元格
        if ("c".equals(name)) {
            //前一个单元格的位置
            if (preRef == null) {
                preRef = attributes.getValue("r");
            } else {
                preRef = ref;
                //更新 preTempRef
                if (preTempRef == null) {
                    preTempRef = preRef;
                }
            }

            //当前单元格的位置
            ref = attributes.getValue("r");
            //设定单元格类型
            this.setNextDataType(attributes);
        }
        if ("v".equals(name)) {
            //如果当前单元格有值，则将 preTempRef 赋值给 preRef ,并清空 preTempRef
            if (preTempRef != null) {
                preRef = preTempRef;
                preTempRef = null;
            }
        }

        //置空
        lastIndex = "";
    }


    /**
     * 第二个执行
     * 得到单元格对应的索引值或是内容值
     * 如果单元格类型是字符串、INLINESTR、数字、日期，lastIndex则是索引值
     * 如果单元格类型是布尔值、错误、公式，lastIndex则是内容值
     *
     * @param ch
     * @param start
     * @param length
     * @throws SAXException
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        lastIndex += new String(ch, start, length);
    }


    /**
     * 第三个执行
     *
     * @param uri
     * @param localName
     * @param name
     * @throws SAXException
     */
    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        if ("v".equals(name)) {
            //第一行为表头，不解析
            if (curRow == 1) {
                return;
            }
            if (excel == null) {
                excel = new Excel();
            }
            //v => 单元格的值，如果单元格是字符串，则v标签的值为该字符串在SST中的索引
            //根据索引值获取对应的单元格值
            String value = this.getDataValue(lastIndex.trim());
            //补全单元格之间的空单元格
            if (!ref.equals(preRef)) {
                int len = countNullCell(ref, preRef);
                for (int i = 0; i < len; i++) {
                    CastType.methodInvoke("", excel, fields[curCol]);
                    curCol++;
                }
            }
            CastType.methodInvoke(value, excel, fields[curCol]);
            curCol++;
            //如果里面某个单元格含有值，则标识该行不为空行
            if (value != null && !"".equals(value)) {
                flag = true;
            }
        } else {
            //如果标签名称为row，这说明已到行尾，调用optRows()方法
            if ("row".equals(name)) {
                //默认第一行为表头，以该行单元格数目为最大数目
                if (curRow == 1) {
                    maxRef = ref;
                }
                //补全一行尾部可能缺失的单元格
                if (maxRef != null) {
                    int len = countNullCell(maxRef, ref);
                    for (int i = 0; i <= len; i++) {
                        CastType.methodInvoke("", excel, fields[curCol]);
                        curCol++;
                    }
                }
                //该行不为空行且该行不是第一行，则发送（第一行为列名，不需要）
                if (flag && curRow != 1) {
                    // 调用excel读数据委托类进行读取插入操作
                    /*excelReadDataDelegated.readExcelDate(sheetIndex, totalRowCount, curRow, cellList);*/
                    excelSet.add(excel);
                    if (excelSet.size() > 500) {
                        AsyncManager.getInstance().setFixedThreadPool(AsyncFactory.saveData(excelSet));
                        excelSet.clear();
                    }
                    totalRows++;
                }

                curRow++;
                curCol = 0;
                preRef = null;
                ref = null;
                excel = null;
                flag = false;
                if (totalRows == totalRowCount && !(excelSet.isEmpty())) {
                    AsyncManager.getInstance().setFixedThreadPool(AsyncFactory.saveData(excelSet));
                    excelSet.clear();
                }
            }
        }
    }

    /**
     * 处理数据类型
     *
     * @param attributes
     */
    public void setNextDataType(Attributes attributes) {
        //cellType为空，则表示该单元格类型为数字
        nextDataType = CellDataType.NUMBER;
        formatIndex = -1;
        formatString = null;
        //单元格类型
        String cellType = attributes.getValue("t");
        String cellStyleStr = attributes.getValue("s");

        //处理布尔值
        if ("b".equals(cellType)) {
            nextDataType = CellDataType.BOOL;
            //处理错误
        }
        if ("e".equals(cellType)) {
            nextDataType = CellDataType.ERROR;
        }
        if ("inlineStr".equals(cellType)) {
            nextDataType = CellDataType.INLINESTR;
            //处理字符串
        }
        if ("s".equals(cellType)) {
            nextDataType = CellDataType.SSTINDEX;
        }
        if ("str".equals(cellType)) {
            nextDataType = CellDataType.FORMULA;
        }
        //处理日期
        if (cellStyleStr != null) {
            int styleIndex = Integer.parseInt(cellStyleStr);
            XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);
            formatIndex = style.getDataFormat();
            formatString = style.getDataFormatString();
            if (formatString.contains("m/d/yy") || formatString.contains("yyyy/mm/dd") || formatString.contains("yyyy/m/d")) {
                nextDataType = CellDataType.DATE;
                formatString = "yyyy-MM-dd hh:mm:ss";
            }

            if (formatString == null) {
                nextDataType = CellDataType.NULL;
                formatString = BuiltinFormats.getBuiltinFormat(formatIndex);
            }
        }
    }

    /**
     * 对解析出来的数据进行类型处理
     *
     * @param value 单元格的值，
     *              value代表解析：BOOL的为0或1， ERROR的为内容值，FORMULA的为内容值，INLINESTR的为索引值需转换为内容值，
     *              SSTINDEX的为索引值需转换为内容值， NUMBER为内容值，DATE为内容值
     * @return
     */
    @SuppressWarnings("deprecation")
    public String getDataValue(String value) {
        String thisStr;
        switch (nextDataType) {
            // 这几个的顺序不能随便交换，交换了很可能会导致数据错误
            //布尔值
            case BOOL:
                char first = value.charAt(0);
                thisStr = first == '0' ? "FALSE" : "TRUE";
                break;
            //错误
            case ERROR:
                thisStr = "\"ERROR:" + value + '"';
                break;
            //公式
            case FORMULA:
                thisStr = '"' + value + '"';
                break;
            case INLINESTR:
                XSSFRichTextString rtsi = new XSSFRichTextString(value);
                thisStr = rtsi.toString();
                break;
            //字符串
            case SSTINDEX:
                String sstIndex = value;
                try {
                    int idx = Integer.parseInt(sstIndex);
                    //根据idx索引值获取内容值
                    XSSFRichTextString rtss = new XSSFRichTextString(sst.getEntryAt(idx));
                    thisStr = rtss.toString();
                } catch (NumberFormatException ex) {
                    thisStr = value;
                }
                break;
            //数字
            case NUMBER:
                if (formatString != null) {
                    thisStr = formatter.formatRawCellContents(Double.parseDouble(value), formatIndex, formatString).trim();
                } else {
                    thisStr = value;
                }
                thisStr = thisStr.replace("_", "").trim();
                break;
            //日期
            case DATE:
                thisStr = formatter.formatRawCellContents(Double.parseDouble(value), formatIndex, formatString);
                // 对日期字符串作特殊处理，去掉T
                thisStr = thisStr.replace("T", " ");
                break;
            default:
                thisStr = " ";
                break;
        }
        return thisStr;
    }

    public int countNullCell(String ref, String preRef) {
        //excel2007最大行数是1048576，最大列数是16384，最后一列列名是XFD
        String xfd = ref.replaceAll("\\d+", "");
        String xfd_1 = preRef.replaceAll("\\d+", "");

        xfd = fillChar(xfd, 3, '@', true);
        xfd_1 = fillChar(xfd_1, 3, '@', true);

        char[] letter = xfd.toCharArray();
        char[] letter_1 = xfd_1.toCharArray();
        int res = (letter[0] - letter_1[0]) * 26 * 26 + (letter[1] - letter_1[1]) * 26 + (letter[2] - letter_1[2]);
        return res - 1;
    }

    public String fillChar(String str, int len, char let, boolean isPre) {
        int len_1 = str.length();
        if (len_1 < len) {
            if (isPre) {
                for (int i = 0; i < (len - len_1); i++) {
                    str = let + str;
                }
            } else {
                for (int i = 0; i < (len - len_1); i++) {
                    str = str + let;
                }
            }
        }
        return str;
    }
}

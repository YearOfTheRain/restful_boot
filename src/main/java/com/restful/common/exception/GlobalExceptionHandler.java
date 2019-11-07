package com.restful.common.exception;

import com.restful.common.core.ParameterInvalidItem;
import com.restful.common.core.ResponseEntity;
import com.restful.common.enums.resultenum.ResultCode;
import com.restful.common.exception.data.DataConflictException;
import com.restful.common.exception.data.DataNotFoundException;
import com.restful.common.util.RequestContextHolderUtil;
import com.restful.poi.model.Mail;
import com.restful.poi.util.MailUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description 全局异常处理
 * @date 2019-09-17 17:58
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /*** 读取接收邮件人*/
    @Value("${mail.to}")
    private String emailTo;

    /*** 读取附件地址*/
    @Value("${mail.filePath}")
    private String filePath;

    /**
     * 方法描述: 处理参数不合法
     *
     * @return com.restful.common.core.ResponseEntity
     * @author LiShuLin
     * @date 2019/9/18
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.info("handleMethodArgumentNotValidException start, uri:{}, caused by: ",
                RequestContextHolderUtil.getHttpServletRequest().getRequestURI(), e);
        return ResponseEntity.failure(ResultCode.PARAM_IS_INVALID, this.getErrorInfo(e.getBindingResult().getFieldErrors()));
    }

    /**
     * 方法描述: 处理参数不合法
     *
     * @return com.restful.common.core.ResponseEntity
     * @author LiShuLin
     * @date 2019/9/18
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ResponseEntity handleMethodBindException(BindException e) {
        logger.info("handleMethodBindException start, uri:{}, caused by: ",
                RequestContextHolderUtil.getHttpServletRequest().getRequestURI(), e);
        return ResponseEntity.failure(ResultCode.PARAM_IS_INVALID, this.getErrorInfo(e.getBindingResult().getFieldErrors()));
    }

    /**
     * 方法描述: 处理请求内容不支持
     *
     * @return com.restful.common.core.ResponseEntity
     * @author LiShuLin
     * @date 2019/9/18
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity handleMethodHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        logger.info("handleMethodHttpMediaTypeNotSupportedException start, uri:{}, caused by: ",
                RequestContextHolderUtil.getHttpServletRequest().getRequestURI(), e);
        return ResponseEntity.failure(ResultCode.REQUEST_CONTENT_TYPE_NOT_SUPPORTED, e.getMessage());
    }

    /**
     * 方法描述: 处理请求方法不支持
     *
     * @return com.restful.common.core.ResponseEntity
     * @author LiShuLin
     * @date 2019/9/18
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity handleMethodHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        logger.info("handleMethodHttpRequestMethodNotSupportedException start, uri:{}, caused by: ",
                RequestContextHolderUtil.getHttpServletRequest().getRequestURI(), e);
        return ResponseEntity.failure(ResultCode.REQUEST_METHOD_NOT_SUPPORTED, e.getMessage());
    }

    /**
     * 方法描述: 数据已存在异常
     *
     * @return com.restful.common.core.ResponseEntity
     * @author LiShuLin
     * @date 2019/9/18
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataConflictException.class)
    public ResponseEntity handleMethodDataConflictException(DataConflictException e) {
        logger.info("handleMethodDataConflictException start, uri:{}, caused by: ",
                RequestContextHolderUtil.getHttpServletRequest().getRequestURI(), e);
        return ResponseEntity.failure(ResultCode.DATA_ALREADY_EXISTED, e.getData());
    }

    /**
     * 方法描述: 数据未找到异常
     *
     * @return com.restful.common.core.ResponseEntity
     * @author LiShuLin
     * @date 2019/9/18
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity handleMethodDataNotFoundException(DataNotFoundException e) {
        logger.info("handleMethodDataNotFoundException start, uri:{}, caused by: ",
                RequestContextHolderUtil.getHttpServletRequest().getRequestURI(), e);
        return ResponseEntity.failure(ResultCode.RESULT_DATA_NONE);
    }

    /**
     * 方法描述: 消息不可读异常
     *
     * @return com.restful.common.core.ResponseEntity
     * @author LiShuLin
     * @date 2019/9/20
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity handleMethodHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        logger.info("handleMethodHttpMessageNotReadableException start, uri:{}, caused by: ",
                RequestContextHolderUtil.getHttpServletRequest().getRequestURI(), e);
        return ResponseEntity.failure(ResultCode.PARAM_IS_BLANK, e.getMessage());
    }

    /**
     * 方法描述: 服务器错误，需要查找问题
     *
     * @return com.restful.common.core.ResponseEntity
     * @author LiShuLin
     * @date 2019/9/18
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity handleMethodRuntimeException(RuntimeException e) {
        logger.info("handleMethodRuntimeException start, uri:{}, caused by: ",
                RequestContextHolderUtil.getHttpServletRequest().getRequestURI(), e);
        //todo 给管理员发短信、微信或者 QQ 消息提示
        String[] strings = emailTo.split(",");
        for (String s : strings) {
            Mail mail = new Mail();
            mail.setTo(s);
            mail.setSubject(e.getMessage());
            mail.setText("详情请看附件日志记录");
            mail.setFileName(filePath);
            MailUtils.buildMessage(mail);
        }
        return ResponseEntity.failure(ResultCode.SERVER_ERROR);
    }

    /**
     * 方法描述: 提取、精炼错误信息，封装后返回前端
     *
     * @param fieldErrorList 错误字段信息集合
     * @return java.util.List<com.restful.common.core.ParameterInvalidItem>
     * @author LiShuLin
     * @date 2019/9/19
     */
    private List<ParameterInvalidItem> getErrorInfo(List<FieldError> fieldErrorList) {
        List<ParameterInvalidItem> parameterInvalidItemList = new ArrayList<>();
        for (FieldError fieldError : fieldErrorList) {
            ParameterInvalidItem parameterInvalidItem = new ParameterInvalidItem();
            parameterInvalidItem.setFieldName(fieldError.getField());
            parameterInvalidItem.setMessage(fieldError.getDefaultMessage());
            parameterInvalidItemList.add(parameterInvalidItem);
        }
        return parameterInvalidItemList;
    }

}

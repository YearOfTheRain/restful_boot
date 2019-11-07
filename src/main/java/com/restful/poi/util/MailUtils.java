package com.restful.poi.util;

import com.restful.poi.model.Mail;
import com.sun.mail.util.MailSSLSocketFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Properties;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description 发送邮件工具类
 * @date 2019-10-08 10:25
 */
@Slf4j
public class MailUtils {

    /*** 收件人电子邮箱*/
    public static final String TO = "asdsadas@qq.com";
    /*** 发件人电子邮箱*/
    public static final String FROM = "ysadsadsa@qq.com";
    /*** 指定发送邮件的主机为 smtp.qq.com QQ 邮件服务器*/
    private static final String HOST = "smtp.qq.com";
    /*** 获取配置*/
    private static Properties properties;
    static {
        // 获取系统属性
        properties = System.getProperties();

        // 设置邮件服务器
        properties.setProperty("mail.smtp.host", HOST);

        properties.put("mail.smtp.auth", "true");
        MailSSLSocketFactory sf;
        try {
            sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.ssl.socketFactory", sf);
        } catch (GeneralSecurityException e) {
            log.info("init sendMail fail, because the MailSSLSocketFactory has init fail and reason is :" + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Session getSession() {
        return Session.getDefaultInstance(properties,new Authenticator(){
            @Override
            public PasswordAuthentication getPasswordAuthentication()
            {     //qq邮箱服务器账户、第三方登录授权码
                return new PasswordAuthentication(FROM, "12312312"); //发件人邮件用户名、密码
            }
        });
    }

    /**
     * 方法描述:  发送邮件
     *
     * @param mail 邮件信息
     * @return boolean
     * @author LiShuLin
     * @date 2019/10/31
     */
    public static void buildMessage(Mail mail) {
        // 创建默认的 MimeMessage 对象
        MimeMessage message = new MimeMessage(getSession());
        try {
            // Set From: 头部头字段
            message.setFrom(new InternetAddress(FROM));
            // Set To: 头部头字段
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mail.getTo()));
            // Set Subject: 主题文字
            message.setSubject(mail.getSubject());
            // Set Content: 消息内容
            message.setContent(textAndFileMessages(mail));
            Transport.send(message);
        } catch (MessagingException|UnsupportedEncodingException e) {
            log.info("build MimeMessage fail and the reason is : ", e);
            e.printStackTrace();
        }
        log.info("Sent message successfully...");
    }

    /**
     *  创建多个消息内容(包括文字消息和附件信息)
     * @param mail mail 信息
     * @throws MessagingException MessagingException
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     */
    private static Multipart textAndFileMessages(Mail mail) throws MessagingException, UnsupportedEncodingException {
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(text(mail.getText()));
        if (!StringUtils.isEmpty(mail.getFileName())) {
            multipart.addBodyPart(file(mail.getFileName()));
        }
        return multipart;
    }

    /**
     *  设置文本值
     * @param text 文本
     * @throws MessagingException MessagingException
     */
    private static BodyPart text(String text) throws MessagingException {
        BodyPart textBodyPart = new MimeBodyPart();
        textBodyPart.setText(text);
        return textBodyPart;
    }

    /**
     *  设置附件
     * @param fileName 文件名
     * @throws MessagingException MessagingException
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     */
    private static BodyPart file(String fileName) throws MessagingException, UnsupportedEncodingException {
        // 附件部分
        BodyPart fileBodyPart = new MimeBodyPart();
        //设置要发送附件的文件路径
        DataSource source = new FileDataSource(fileName);
        fileBodyPart.setDataHandler(new DataHandler(source));
        //处理附件名称中文（附带文件路径）乱码问题
        fileBodyPart.setFileName(MimeUtility.encodeText(fileName));
        return fileBodyPart;
    }
}

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
    public static boolean buildMessage(Mail mail) {
        // 创建默认的 MimeMessage 对象
        MimeMessage message = new MimeMessage(getSession());

        try {
            // Set From: 头部头字段
            message.setFrom(new InternetAddress(FROM));

            // Set To: 头部头字段
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mail.getTo()));

            // Set Subject: 主题文字
            message.setSubject(mail.getSubject());

            // 创建消息部分
            BodyPart messageBodyPart = new MimeBodyPart();

            // 消息
            messageBodyPart.setText(mail.getText());

            // 创建多重消息
            Multipart multipart = new MimeMultipart();

            // 设置文本消息部分
            multipart.addBodyPart(messageBodyPart);

            if (!StringUtils.isEmpty(mail.getFileName())) {
                // 附件部分
                BodyPart fileBodyPart = new MimeBodyPart();
                //设置要发送附件的文件路径
                DataSource source = new FileDataSource(mail.getFileName());
                fileBodyPart.setDataHandler(new DataHandler(source));

                //处理附件名称中文（附带文件路径）乱码问题
                fileBodyPart.setFileName(MimeUtility.encodeText(mail.getFileName()));
                multipart.addBodyPart(fileBodyPart);
            }

            // 发送完整消息
            message.setContent(multipart);

            //   发送消息
            Transport.send(message);
        } catch (MessagingException e) {
            log.info("build MimeMessage fail and the reason is : ", e);
            e.printStackTrace();
            return false;
        } catch (UnsupportedEncodingException e) {
            log.info("fileName url encode error and the reason is :", e);
            e.printStackTrace();
            return false;
        }
        log.info("Sent message successfully...");
        return true;
    }
}

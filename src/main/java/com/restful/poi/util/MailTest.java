package com.restful.poi.util;

import com.restful.poi.model.Mail;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description 发邮件
 * @date 2019-09-30 17:07
 */
public class MailTest {

    /*public static void main(String [] args) throws GeneralSecurityException, UnsupportedEncodingException
    {
        // 收件人电子邮箱
        String to = "997150421@qq.com";

        // 发件人电子邮箱
        String from = "yearoftherain@qq.com";

        // 指定发送邮件的主机为 smtp.qq.com
        String host = "smtp.qq.com";  //QQ 邮件服务器

        // 获取系统属性
        Properties properties = System.getProperties();

        // 设置邮件服务器
        properties.setProperty("mail.smtp.host", host);

        properties.put("mail.smtp.auth", "true");
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", sf);
        // 获取默认session对象
        Session session = Session.getDefaultInstance(properties,new Authenticator(){
            @Override
            public PasswordAuthentication getPasswordAuthentication()
            {     //qq邮箱服务器账户、第三方登录授权码
                return new PasswordAuthentication("yearoftherain@qq.com", "jblwaulofalgdicg"); //发件人邮件用户名、密码
            }
        });

        try{
            // 创建默认的 MimeMessage 对象
            MimeMessage message = new MimeMessage(session);

            // Set From: 头部头字段
            message.setFrom(new InternetAddress(from));

            // Set To: 头部头字段
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: 主题文字
            message.setSubject("测试导出excel到邮箱");

            // 创建消息部分
            BodyPart messageBodyPart = new MimeBodyPart();

            // 消息
            messageBodyPart.setText("测试导出excel到邮箱，不用理会，请忽略。附件也请忽略");

            // 创建多重消息
            Multipart multipart = new MimeMultipart();

            // 设置文本消息部分
            multipart.addBodyPart(messageBodyPart);

            // 附件部分
            messageBodyPart = new MimeBodyPart();
            //设置要发送附件的文件路径
            String filename = "D:\\小程序ERP导入字段BBBig-nosame.xlsx";
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));

            //messageBodyPart.setFileName(filename);
            //处理附件名称中文（附带文件路径）乱码问题
            messageBodyPart.setFileName(MimeUtility.encodeText(filename));
            multipart.addBodyPart(messageBodyPart);

            // 发送完整消息
            message.setContent(multipart );

            //   发送消息
            Transport.send(message);
            System.out.println("Sent message successfully....");
        }catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }*/

    public static void main(String[] args) {
        Mail mail = new Mail();
        mail.setForm(MailUtils.FROM);
        mail.setTo("424040724@qq.com");
//        mail.setTo(MailUtils.TO);
        mail.setSubject("");
        mail.setText("测试导出excel到邮箱，不用理会，请忽略。附件也请忽略");
        mail.setText("");
        mail.setFileName("D:\\小程序ERP导入字段BBBig-nosame.xlsx");
        MailUtils.buildMessage(mail);
    }
}

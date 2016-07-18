package puzzle.fca.utils;

import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
/**
 * 本程序用java来实现Email的发送，所用到的协议为：SMTP，端口号为25；<br>
 * 方法：用Socket进行实现，打开客户端的Socket，并连接上服务器：<br>
 * 如：Socket sockClient = new Socket("smtp.qq.com",23);<br>
 * 这表示发件方连接的是QQ邮箱的服务器，端口号为23
 * @author cuiran
 * @version 1.0.0
 */
public class Email{
    /**
     * 整个MIME邮件对象
     */
    private MimeMessage	mimeMsg;
    /**
     * 专门用来发送邮件的Session会话
     */
    private Session		session;
    /**
     * 封装邮件发送时的一些配置信息的一个属性对象
     */
    private Properties	props;
    /**
     * 发件人的用户名
     */
    private String		username;
    /**
     * 发件人的密码
     */
    private String		password;
    /**
     * 用来实现附件添加的组件
     */
    private Multipart	mp;
    /**
     * 发送参数初始化,有的服务器不需要用户验证，所以这里对用户名和密码进行初始化""
     *
     * @param smtp
     *            SMTP服务器的地址，比如要用QQ邮箱，哪么应为："smtp.qq.com"，163为："smtp.163.com"
     */
    public Email(String smtp){
        username = "";
        password = "";
        // 设置邮件服务器
        setSmtpHost(smtp);
        // 创建邮件
        createMimeMessage();
    }
    /**
     * 设置发送邮件的主机(JavaMail需要Properties来创建一个session对象。
     * 它将寻找字符串"mail.smtp.host"，属性值就是发送邮件的主机);
     *
     * @param hostName
     */
    public void setSmtpHost(String hostName){
        System.out.println("设置系统属性：mail.smtp.host = " + hostName);
        if (props == null)
            props = System.getProperties();
        props.put("mail.smtp.host", hostName);
    }

    /**
     * (这个Session类代表JavaMail 中的一个邮件session. 每一个基于
     * JavaMail的应用程序至少有一个session但是可以有任意多的session。 在这个例子中,
     * Session对象需要知道用来处理邮件的SMTP 服务器。
     */
    public boolean createMimeMessage(){
        try{
            // 用props对象来创建并初始化session对象
            session = Session.getDefaultInstance(props, null);
        }
        catch (Exception e){
            System.err.println("获取邮件会话对象时发生错误！" + e);
            return false;
        }
        try{
            // 用session对象来创建并初始化邮件对象
            mimeMsg = new MimeMessage(session);
            // 生成附件组件的实例
            mp = new MimeMultipart();

        }
        catch (Exception e){
            return false;
        }
        return true;
    }
    /**
     * 设置SMTP的身份认证
     */
    public void setNeedAuth(boolean need){
        if (props == null){
            props = System.getProperties();
        }
        if (need)
            props.put("mail.smtp.auth", "true");
        else
            props.put("mail.smtp.auth", "false");
    }
    /**
     * 进行用户身份验证时，设置用户名和密码
     */
    public void setNamePass(String name, String pass){
        username = name;
        password = pass;
    }
    /**
     * 设置邮件主题
     *
     * @param mailSubject
     * @return
     */
    public boolean setSubject(String mailSubject){
        try{
            mimeMsg.setSubject(mailSubject);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    /**
     * 设置邮件内容,并设置其为文本格式或HTML文件格式，编码方式为UTF-8
     *
     * @param mailBody
     * @return
     */
    public boolean setBody(String mailBody) {
        try{
            BodyPart bp = new MimeBodyPart();
            bp.setContent("<meta http-equiv=Content-Type content=text/html; charset=UTF-8>" + mailBody,
                    "text/html;charset=UTF-8");
            // 在组件上添加邮件文本
            mp.addBodyPart(bp);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    /**
     * 增加发送附件
     *
     * @param fileName
     *            邮件附件的地址，只能是本机地址而不能是网络地址，否则抛出异常
     * @return
     */
    public boolean addAttach(String fileName){
        try{
            BodyPart bp = new MimeBodyPart();
            FileDataSource fileds = new FileDataSource(fileName);
            bp.setDataHandler(new DataHandler(fileds));
            // 发送的附件前加上一个用户名的前缀
            bp.setFileName(fileds.getName());
            // 添加附件
            mp.addBodyPart(bp);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }
    /**
     * 设置发件人地址
     *
     * @param from
     *            发件人地址
     * @return
     */
    public boolean setFrom(String from){
        try{
            mimeMsg.setFrom(new InternetAddress(from));
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }
    /**
     * 设置收件人地址
     *
     * @param to
     *            收件人的地址
     * @return
     */
    public boolean setTo(String to){
        if (to == null)
            return false;
        try{
            mimeMsg.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(to));
        }
        catch (Exception e){
            return false;
        }
        return true;
    }
    /**
     * 设置抄送人地址
     *
     * @param copyTo
     * @return
     */
    public boolean setCopyTo(String copyTo){
        System.out.println("发送附件到");
        if (copyTo == null)
            return false;
        try{
            mimeMsg.setRecipients(javax.mail.Message.RecipientType.CC, InternetAddress.parse(copyTo));
        }
        catch (Exception e){
            return false;
        }
        return true;
    }
    /**
     * 发送邮件
     *
     * @return
     */
    public boolean send(){
        try{
            mimeMsg.setContent(mp);
            mimeMsg.saveChanges();
            System.out.println("正在发送邮件....");
            Session mailSession = Session.getInstance(props, null);
            Transport transport = mailSession.getTransport("smtp");
            // 真正的连接邮件服务器并进行身份验证
            transport.connect((String) props.get("mail.smtp.host"), username, password);
            // 发送邮件
            transport.sendMessage(mimeMsg, mimeMsg.getRecipients(javax.mail.Message.RecipientType.TO));
            System.out.println("发送邮件成功！");
            transport.close();
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}

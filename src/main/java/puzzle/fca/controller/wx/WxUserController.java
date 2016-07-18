package puzzle.fca.controller.wx;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.fca.Constants;
import puzzle.fca.controller.BaseController;
import puzzle.fca.entity.FcaUser;
import puzzle.fca.service.IFcaUserService;
import puzzle.fca.utils.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller(value = "wxUserController")
@RequestMapping(value = "/wx/user")
public class WxUserController extends BaseController {

    @Autowired
    private IFcaUserService fcaUserService;

    /**
     * 进入微信登录页面
     * @return
     */
    @RequestMapping(value = "/index")
    public String index(){
        return Constants.UrlHelper.WX_USER_LOGIN;
    }

    /**
     * 验证登录
     * @param userName
     * @param password
     * @return
     */
    @RequestMapping(value = "/login")
    @ResponseBody
    public Result login(String userName,String password){
        Result result=new Result();
        try{
            if(StringUtil.isNullOrEmpty(userName) || StringUtil.isNullOrEmpty(password)){
                result.setCode(-1);
                result.setMsg("用户名或密码不能为空！");
                return result;
            }
            if(!userName.contains("@")){
                result.setCode(-1);
                result.setMsg("用户名不正确！");
                return result;
            }
            userName=userName.trim().replace('@', '|');
            password=password.trim();

            if(!StringUtil.isRangeLength(userName, 3, 20) || !StringUtil.isRangeLength(password, 6, 20)){
                result.setCode(-1);
                result.setMsg("用户名或密码不正确！");
                return result;
            }
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("email",userName);
            map.put("password", EncryptUtil.MD5(password));
            FcaUser user=fcaUserService.query(map);
            if(user == null){
                result.setCode(1);
                result.setMsg("用户名或密码不正确！");
                return result;
            }
            if(user.getStatus() == Constants.FCA_USER_STATUS_DISABLED){
                result.setCode(1);
                result.setMsg("该账户无效，不能登录！");
                return result;
            }
            this.setCurrentUser(user);
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("登录验证出错！");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/forget")
    public String login(){
        return Constants.UrlHelper.WX_USER_FORGET;
    }

    @RequestMapping(value = "/email")
    @ResponseBody
    public Result email(String email){
        Result result = new Result();
        try{
            if(StringUtil.isNullOrEmpty(email)){
                result.setCode(-1);
                result.setMsg("邮箱地址不能为空");
                return result;
            }
            if(!email.contains("@")){
                result.setCode(-1);
                result.setMsg("邮箱地址不正确");
                return result;
            }
            email = email.trim().replace('@', '|');
            FcaUser user = fcaUserService.query(null, email);
            if(user == null){
                result.setCode(-1);
                result.setMsg("邮箱地址不存在");
                return result;
            }
            String code = ConvertUtil.toString(RandomUtil.nextInt(6));
            user.setCode(code);
            fcaUserService.update(user);
            Email mail = new Email("smtp.163.com");
            String mailBody = "验证码:" + code + ",请尽快使用。";
            mail.setNeedAuth(true);
            mail.setSubject("FCA验证码通知邮件");//邮件主题
            mail.setBody(mailBody);//邮件正文
            mail.setTo(email.replace('|', '@'));//收件人地址
            mail.setFrom("fcagroup@163.com");//发件人地址
            mail.setNamePass("fcagroup@163.com", "fca123456");//发件人地址和密码 **改为相应邮箱密码
            mail.send();
        }
        catch (Exception e){
            e.printStackTrace();
            result.setCode(-1);
            result.setMsg("发送邮件出错");
        }
        return result;
    }

    @RequestMapping(value = "/password")
    @ResponseBody
    public Result password(FcaUser user){
        Result result = new Result();
        try{
            if(user == null){
                result.setCode(-1);
                result.setMsg("提交参数错误");
                return result;
            }
            if(StringUtil.isNullOrEmpty(user.getEmail())){
                result.setCode(-1);
                result.setMsg("邮箱地址不能为空");
                return result;
            }
            if(!user.getEmail().contains("@")){
                result.setCode(-1);
                result.setMsg("邮箱地址不正确");
                return result;
            }
            if(StringUtil.isNullOrEmpty(user.getCode())){
                result.setCode(-1);
                result.setMsg("验证码不能为空");
                return result;
            }
            if(StringUtil.isNullOrEmpty(user.getPassword())){
                result.setCode(-1);
                result.setMsg("密码不能为空");
                return result;
            }
            if(!StringUtil.isRangeLength(user.getPassword(), 6, 20)){
                result.setCode(-1);
                result.setMsg("密码长度必须在6-20之间");
                return result;
            }
            if(StringUtil.isNullOrEmpty(user.getRepassword())){
                result.setCode(-1);
                result.setMsg("重复密码不能为空");
                return result;
            }
            if(!user.getPassword().contentEquals(user.getRepassword())){
                result.setCode(-1);
                result.setMsg("两次密码不一致");
                return result;
            }
            String code = user.getCode();
            String password = user.getPassword();
            String email = user.getEmail().trim().replace('@', '|');
            user = fcaUserService.query(null, email);
            if(user == null){
                result.setCode(-1);
                result.setMsg("该用户不存在");
                return result;
            }
            if(!user.getCode().contentEquals(code)){
                result.setCode(-1);
                result.setMsg("验证码不正确");
                return result;
            }
            user.setPassword(EncryptUtil.MD5(password));
            if(!fcaUserService.update(user)){
                result.setCode(-1);
                result.setMsg("重置密码失败");
                return result;
            }
        }
        catch (Exception e){
            result.setCode(1);
            result.setMsg("重置密码出错");
            return result;
        }
        return result;
    }
}

package puzzle.fca.intercept;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import puzzle.fca.Constants;
import puzzle.fca.entity.FcaUser;
import puzzle.fca.init.InitConfig;
import puzzle.fca.service.IFcaUserService;
import puzzle.fca.service.impl.FcaUserServiceImpl;
import puzzle.fca.utils.HttpUtil;
import puzzle.fca.utils.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class WxLoginInterceptor extends HandlerInterceptor {

    @Autowired
    private IFcaUserService fcaUserService;

    @Autowired
    private InitConfig initConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI().replace(request.getContextPath(), "");
        if(isExcludePath(path)){
            return true;
        }
        HttpSession session = request.getSession();
        Object user = session.getAttribute(Constants.SESSION_USER);
        if(user == null){
            String openId = getOpenId(request, response);
            if(StringUtil.isNotNullOrEmpty(openId)) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("openId", openId);
                FcaUser find = fcaUserService.query(map);
                if (find == null) {
                    String url = request.getContextPath() + "/" + Constants.UrlHelper.WX_USER_LOGIN + "?" +
                                 Constants.UrlHelper.PARAM_RETURN_URL + "=" + URLEncoder.encode(path, "utf-8");
                    response.sendRedirect(url);
                } else {
                    request.getSession().setAttribute(Constants.SESSION_USER, find);
                }
            }
        }
        return true;
    }

    protected String getOpenId(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String proxy = initConfig.getProperty("wx.Proxy");
        if(StringUtil.isNullOrEmpty(proxy)){
            if(StringUtil.isNotNullOrEmpty(request.getParameter("code"))
                    && StringUtil.isNotNullOrEmpty(request.getParameter("state"))
                    && request.getParameter("state").contentEquals(initConfig.getProperty("wx.State"))){

                String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={APPID}&secret={APPSECRET}&code={CODE}&grant_type=authorization_code";
                url = url.replace("{APPID}", initConfig.getProperty("wx.AppID"));
                url = url.replace("{APPSECRET}", initConfig.getProperty("wx.AppSecret"));
                url = url.replace("{CODE}", request.getParameter("code"));
                HttpUtil http = new HttpUtil(url);
                if(http.request()){
                    String text = http.getResponse();
                    if(StringUtil.isNotNullOrEmpty(text)) {
                        JSONObject result = JSONObject.fromObject(text);
                        return result.get("openid").toString();
                    }
                }
            }
            else {
                String path = request.getScheme() + "://" + request.getServerName();
                if (request.getServerPort() != 80) {
                    path += ":" + request.getServerPort();
                }
                path += request.getRequestURI();
                String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid={APPID}&redirect_uri={REDIRECT_URI}&response_type=code&scope=SCOPE&state={STATE}#wechat_redirect";
                url = url.replace("{APPID}", initConfig.getProperty("wx.AppID"));
                url = url.replace("{REDIRECT_URI}", URLEncoder.encode(path, "utf-8"));
                url = url.replace("{STATE}", initConfig.getProperty("wx.State"));
                response.sendRedirect(url);
            }
        }else{
            if(StringUtil.isNotNullOrEmpty(request.getParameter("openid"))) {
                return request.getParameter("openid");
            }
            else{
                String path = request.getScheme() + "://" + request.getServerName();
                if (request.getServerPort() != 80) {
                    path += ":" + request.getServerPort();
                }
                path += request.getRequestURI();
                String url = (proxy.indexOf('?') > -1 ? "&" : "?") + "url=" + URLEncoder.encode(path, "utf-8");
                response.sendRedirect(url);
            }
        }
        return null;
    }
}

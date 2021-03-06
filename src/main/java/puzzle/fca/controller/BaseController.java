package puzzle.fca.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Cookie;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import puzzle.fca.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import puzzle.fca.init.InitConfig;
import puzzle.fca.utils.StringUtil;

import java.io.IOException;
import java.net.HttpCookie;

public class BaseController {

    protected static Logger logger;

    protected HttpServletRequest request;

    protected HttpSession session;

    protected HttpServletResponse response;

    protected ModelMap map;

    @Autowired
    protected InitConfig initConfig;


    public BaseController(){
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @ModelAttribute
    public void initialize(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap map){
        this.request = request;
        this.response = response;
        this.session = session;
        this.map = map;
        this.map.put(Constants.CONTEXT_PATH, session.getServletContext().getContextPath());
    }


    public String redirect(String url){
        String contextPath = session.getServletContext().getContextPath();
        if (StringUtil.isNullOrEmpty(contextPath) && !url.startsWith("/")) {
            url = "/" + url;
        }
        else if(StringUtil.isNotNullOrEmpty(contextPath) && !url.startsWith(contextPath)){
            url = contextPath + (url.startsWith("/") ? "" : "/") + url;
        }
        try {
            this.response.sendRedirect(url);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
//        return "redirect:" + url;
    }

    public String getParameter(String name){
        return request.getParameter(name);
    }

    public void setCookie(String name, String value){
       setCookie(name, value, 0, "/");
    }

    public void setCookie(String name, String value, int expiry){
        setCookie(name, value, expiry, "/");
    }

    public void setCookie(String name, String value, int expiry, String path){
        Cookie cookie = new Cookie(name, value);
        if(expiry > 0) {
            cookie.setMaxAge(expiry);
        }
        cookie.setPath(path);
        response.addCookie(cookie);
    }

    public void setCookie(Cookie cookie){
        response.addCookie(cookie);
    }

    public void removeCookie(String name){
        Cookie[] cookies = request.getCookies();
        if(cookies != null)
            for(Cookie cookie:cookies){
                if(cookie.getName().equals(name)){
                    cookie.setMaxAge(-1);
                    cookie.setValue("");
                    response.addCookie(cookie);
                    break;
                }
            }
    }

    public String getCookie(String name){
        Cookie[] cookies = request.getCookies();
        if(cookies == null)
            return null;
        for(Cookie cookie:cookies){
            if(cookie.getName().equals(name)){
                return cookie.getValue();
            }
        }
        return null;
    }

    public void setCurrentUser(Object user){
        if(session != null) {
            if (user != null) {
                this.session.setAttribute(Constants.SESSION_ADMIN, user);

            } else {
                this.session.removeAttribute(Constants.SESSION_ADMIN);
            }
        }
    }

    public Object getCurrentUser(){
        if(this.session != null && this.session.getAttribute(Constants.SESSION_ADMIN) != null){
            return this.session.getAttribute(Constants.SESSION_ADMIN);
        }
        return  null;
    }

    public void setModelAttribute(String name, Object value){
        if(this.map != null){
            this.map.addAttribute(name, value);
        }
    }

    public Object getRequestAttribute(String name){
        if(this.request != null){
            return this.request.getAttribute(name);
        }
        return null;
    }

    public void writeJson(Object object){
        write("application/json;charset=utf-8", JSONObject.fromObject(object).toString());
    }

    public void writeXml(String xml) throws IOException{
        write("text/xml;charset=utf-8", xml);
    }

    public void writeText(String text){
        write("text/plain;charset=utf-8", text);
    }

    protected void write(String contentType, String content){
        try {
            response.addHeader("Content-Type", contentType);
            response.getWriter().write(content);
        }
        catch (Exception e){
            logger.error("write error:" + e.getMessage());
        }
    }

    public String getHost(){
        String host = request.getScheme() + "://" + request.getServerName();
        if(request.getServerPort() != 80){
            host += ":" + request.getServerPort() + "/";
        }
        return host;
    }

    public String getRoot(){
        return session.getServletContext().getRealPath("");
    }
}

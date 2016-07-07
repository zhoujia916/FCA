package puzzle.fca.controller.admin;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import puzzle.fca.Constants;
import puzzle.fca.controller.ModuleController;
import puzzle.fca.controller.plugin.uploader.PathFormatter;
import puzzle.fca.entity.FcaArticle;
import puzzle.fca.entity.FcaArticleCat;
import puzzle.fca.entity.SystemMenuAction;
import puzzle.fca.entity.SystemUser;
import puzzle.fca.service.IFcaArticleCatService;
import puzzle.fca.service.IFcaArticleService;
import puzzle.fca.utils.ConvertUtil;
import puzzle.fca.utils.Page;
import puzzle.fca.utils.Result;
import puzzle.fca.utils.StringUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

@Controller (value = "adminFcaArticleController")
@RequestMapping (value = "/admin/fcaarticle")
public class FcaArticleController extends ModuleController {

    @Autowired
    private IFcaArticleService fcaArticleService;

    @Autowired
    private IFcaArticleCatService fcaArticleCatService;

    @RequestMapping (value = {"/","/index"})
    public String index(){

        List<SystemMenuAction> actions = getActions();
        this.setModelAttribute("actions", actions);

        List<FcaArticleCat> fcaArticleCatList=fcaArticleCatService.queryList(null);
        this.setModelAttribute("fcaArticleCatList",addSubFcaArticle(fcaArticleCatList, 0, "select"));
        return Constants.UrlHelper.ADMIN_FCA_ARTICLE;
    }



    @RequestMapping (value = "/add")
    public String add(){
        List<FcaArticleCat> cats=fcaArticleCatService.queryList(null);
        this.setModelAttribute("catList", addSubFcaArticle(cats, 0, "select"));
        this.setModelAttribute("action", Constants.PageHelper.PAGE_ACTION_CREATE);
        return Constants.UrlHelper.ADMIN_FCA_ARTICLE_SHOW;
    }

    @RequestMapping (value = "/edit/{articleId}")
    public String edit(@PathVariable("articleId") Integer articleId){
        if(articleId != null && articleId > 0){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("articleId", articleId);
            FcaArticle article = fcaArticleService.query(map);
            this.setModelAttribute("article", article);
        }
        List<FcaArticleCat> cats=fcaArticleCatService.queryList(null);
        this.setModelAttribute("catList", addSubFcaArticle(cats, 0, "select"));

        this.setModelAttribute("action", Constants.PageHelper.PAGE_ACTION_UPDATE);
        return Constants.UrlHelper.ADMIN_FCA_ARTICLE_SHOW;
    }

    @RequestMapping (value = "/view/{articleId}")
    public String view(@PathVariable("articleId") Integer articleId){
        if(articleId != null && articleId > 0){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("articleId", articleId);
            FcaArticle article = fcaArticleService.query(map);
            this.setModelAttribute("article", article);
            List<FcaArticleCat> cats=fcaArticleCatService.queryList(null);
            this.setModelAttribute("catList", addSubFcaArticle(cats, 0, "select"));
        }
        this.setModelAttribute("action", Constants.PageHelper.PAGE_ACTION_VIEW);
        return Constants.UrlHelper.ADMIN_FCA_ARTICLE_SHOW;
    }

    @RequestMapping (value = "/list.do")
    @ResponseBody
    public Result list(FcaArticle fcaArticle,Page page){
        Result result=new Result();
        try{
            Map<String, Object> map=new HashMap<String, Object>();
            if(fcaArticle!=null) {
                if(StringUtil.isNotNullOrEmpty(fcaArticle.getTitle())){
                    map.put("title", fcaArticle.getTitle());
                }
                if(StringUtil.isNotNullOrEmpty(fcaArticle.getUserName())){
                    map.put("userName",fcaArticle.getUserName());
                }
                if (StringUtil.isNotNullOrEmpty(fcaArticle.getStartDate())) {
                    map.put("beginTime", ConvertUtil.toLong(ConvertUtil.toDateTime(fcaArticle.getStartDate() + " 00:00:00")));
                }
                if (StringUtil.isNotNullOrEmpty(fcaArticle.getEndDate())) {
                    map.put("endTime", ConvertUtil.toLong(ConvertUtil.toDateTime(fcaArticle.getEndDate() + " 23:59:59")));
                }
                if (fcaArticle.getStatus() != null && fcaArticle.getStatus() >0) {
                    map.put("status", fcaArticle.getStatus());
                }
                if (fcaArticle.getCatId() != null && fcaArticle.getCatId() >0) {
                    map.put("catId", fcaArticle.getCatId());
                }
            }
            List<FcaArticle> list=fcaArticleService.queryList(map,page);
            if(list!=null && list.size()>0){
                JSONArray array=new JSONArray();
                for(FcaArticle article:list) {
                    JSONObject jsonObject = JSONObject.fromObject(article);
                    jsonObject.put("addTime", ConvertUtil.toString(ConvertUtil.toDate(article.getAddTime())));
                    jsonObject.put("status", Constants.MAP_AUTO_ARTICLE_STATUS.get(article.getStatus()));
                    array.add(jsonObject);
                }
                result.setData(array);
            }

            result.setTotal(page.getTotal());
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("获取文章信息出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    @RequestMapping (value = "/action.do")
    @ResponseBody
    public Result action(String action,FcaArticle fcaArticle){
        Result result=new Result();
        Map<String, Object> map=new HashMap<String, Object>();
        try{
            if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_CREATE)){
                int userId = 0;
                if(getCurrentUser() != null){
                    userId = ((SystemUser)getCurrentUser()).getUserId();
                }
                fcaArticle.setAddUserId(userId);
                fcaArticle.setStatus(2);
                fcaArticle.setAddTime(ConvertUtil.toLong(new Date()));
                String cover = saveCover();
                if(StringUtil.isNotNullOrEmpty(cover)){
                    fcaArticle.setCover(cover);
                }
                if(!fcaArticleService.insert(fcaArticle)){
                    result.setCode(1);
                    result.setMsg("添加文章时出错");
                }else{
                    //添加日志
                    insertLog(Constants.PageHelper.PAGE_ACTION_CREATE,"添加文章信息");
                }
            }else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_UPDATE)){
                String cover = saveCover();
                if(StringUtil.isNotNullOrEmpty(cover)){
                    fcaArticle.setCover(cover);
                }
                if(!fcaArticleService.update(fcaArticle)){
                    result.setCode(1);
                    result.setMsg("修改文章信息时出错");
                }else{
                    //添加日志
                    insertLog(Constants.PageHelper.PAGE_ACTION_UPDATE,"修改特定的文章信息");
                }
            }else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_DELETE)){
                //删除
                String id =request.getParameter("id");
                String ids=request.getParameter("ids");
                if(StringUtil.isNotNullOrEmpty(id)){
                    map.put("articleId",ConvertUtil.toInt(id));
                }else if(StringUtil.isNotNullOrEmpty(ids)){
                    map.put("articleIds",ids);
                }
                if(!fcaArticleService.delete(map)){
                    result.setCode(1);
                    result.setMsg("删除文章信息出错");
                }else{
                    //添加日志
                    insertLog(Constants.PageHelper.PAGE_ACTION_DELETE,"删除特定的文章信息");
                }
            }else{
                result.setCode(-1);
                result.setMsg("参数出错");
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("操作文章信息时出错!");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    //region
    public List<FcaArticleCat> addSubFcaArticle(List<FcaArticleCat> list, int   parentId, String type){
        List<FcaArticleCat> newList = new ArrayList<FcaArticleCat>();
        for (FcaArticleCat item : list) {
            if (item.getParentId() == parentId) {
                if(type.equalsIgnoreCase("select")){
                    int level = getLevel(list, item);
                    if(level == 0){
                        item.setCatName("|-" + item.getCatName());
                    }else{
                        item.setCatName("|-" + StringUtil.padLeft(level * 2, '-') + item.getCatName());
                    }
                    newList.add(item);
                    List<FcaArticleCat> children = addSubFcaArticle(list, item.getCatId(), type);
                    for(int i = 0; i < children.size(); i++){
                        newList.add(children.get(i));
                    }
                } else if (type.equalsIgnoreCase("list")) {
                    newList.add(item);
                    List<FcaArticleCat> children = addSubFcaArticle(list, item.getCatId(), type);
                    for(int i = 0; i < children.size(); i++){
                        newList.add(children.get(i));
                    }
                }
                else if (type.equalsIgnoreCase("tree")) {
                    List<FcaArticleCat> children = addSubFcaArticle(list, item.getCatId(), type);
                    item.setChildren(children);
                    newList.add(item);
                }

            }
        }
        return newList;
    }

    public int getLevel(List<FcaArticleCat> list, FcaArticleCat fcaArticleCat){
        if(fcaArticleCat.getParentId() == 0){
            return 0;
        }
        int level = 0;
        boolean hasParent = true;
        while(hasParent){
            hasParent = false;
            for (FcaArticleCat item : list) {
                if (fcaArticleCat.getParentId() == item.getCatId()) {
                    level++;
                    fcaArticleCat = item;
                    hasParent = true;
                    break;
                }
            }
        }
        return level;
    }

    public String saveCover(){
        try {
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(session.getServletContext());
            if (multipartResolver.isMultipart(request)) {
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                MultipartFile cover = multiRequest.getFile("file");

                String typePath = "article";
                String savePath = session.getServletContext().getRealPath("") + "/upload/" + typePath + "/";
                String relativeUrl = request.getContextPath() + "/upload/" + typePath + "/";
                String saveName = PathFormatter.format(cover.getOriginalFilename(), "{yy}{MM}{dd}/{hh}{mm}{rand:6}");
                String dirName = savePath + saveName.substring(0, saveName.lastIndexOf('/'));
                File dir = new File(dirName);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                FileOutputStream fos = new FileOutputStream(savePath + saveName);
                fos.write(cover.getBytes());
                fos.close();

                String url = request.getScheme() + "://" + request.getServerName();
                if (request.getServerPort() != 80) {
                    url += ":" + request.getServerPort();
                }
                url += relativeUrl + saveName;

                return url;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    //endregion
}

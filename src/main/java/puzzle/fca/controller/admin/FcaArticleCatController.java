package puzzle.fca.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.fca.Constants;
import puzzle.fca.controller.ModuleController;
import puzzle.fca.entity.FcaArticleCat;
import puzzle.fca.entity.SystemMenuAction;
import puzzle.fca.service.IFcaArticleCatService;
import puzzle.fca.utils.ConvertUtil;
import puzzle.fca.utils.Page;
import puzzle.fca.utils.Result;
import puzzle.fca.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller (value = "adminFcaArticleCatController")
@RequestMapping (value = "/admin/fcaarticlecat")
public class FcaArticleCatController extends ModuleController {

    @Autowired
    private IFcaArticleCatService fcaArticleCatService;

    @RequestMapping (value = {"/","/index"})
    public String index(){
        List<SystemMenuAction> actions = getActions();
        this.setModelAttribute("actions", actions);
        List<FcaArticleCat> fcaArticleCatList=fcaArticleCatService.queryList(null);
        this.setModelAttribute("fcaArticleCatList",addSubFcaArticle(fcaArticleCatList, 0, "select"));
        return Constants.UrlHelper.ADMIN_FCA_ARTICLE_CAT;
    }

    @RequestMapping (value = "/list.do")
    @ResponseBody
    public Result list(FcaArticleCat fcaArticleCat){
        Result result=new Result();
        Map map=new HashMap();
        try{
            if(fcaArticleCat!=null){
                if(StringUtil.isNotNullOrEmpty(fcaArticleCat.getCatName())){
                    map.put("catName",fcaArticleCat.getCatName());
                }
            }
            List<FcaArticleCat> list = fcaArticleCatService.queryList(map);
            result.setData(list);
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("获取文章类型信息出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    @RequestMapping (value = "/action.do")
    @ResponseBody
    public Result action(String action,FcaArticleCat fcaArticleCat){
        Result result=new Result();
        Map<String, Object> map=new HashMap<String, Object>();
        try{
            if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_CREATE)){
                if(!fcaArticleCatService.insert(fcaArticleCat)){
                    result.setCode(1);
                    result.setMsg("添加文章类型信息时出错");
                }else{
                    //添加日志
                    insertLog(Constants.PageHelper.PAGE_ACTION_CREATE,"添加文章类型信息");
                }
            }else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_UPDATE)){
                if(!fcaArticleCatService.update(fcaArticleCat)){
                    result.setCode(1);
                    result.setMsg("修改文章类型信息时出错");
                }else{
                    //添加日志
                    insertLog(Constants.PageHelper.PAGE_ACTION_UPDATE,"修改指定的文章类型信息");
                }
            }else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_DELETE)){
                String id=request.getParameter("id");
                String ids=request.getParameter("ids");
                if(StringUtil.isNotNullOrEmpty(id)){
                    map.put("catId", ConvertUtil.toInt(id));
                }else if(StringUtil.isNotNullOrEmpty(ids)){
                    map.put("catIds",ids);
                }
                if(!fcaArticleCatService.delete(map)){
                    result.setCode(1);
                    result.setMsg("删除文章类型信息时出错");
                }else{
                    //添加日志
                    insertLog(Constants.PageHelper.PAGE_ACTION_DELETE,"删除指定的文章类型信息");
                }
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("操作文章类型信息时出错!");
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

    public int getLevel(List<FcaArticleCat> list, FcaArticleCat menu){
        if(menu.getParentId() == 0){
            return 0;
        }
        int level = 0;
        boolean hasParent = true;
        while(hasParent){
            hasParent = false;
            for (FcaArticleCat item : list) {
                if (menu.getParentId() == item.getCatId()) {
                    level++;
                    menu = item;
                    hasParent = true;
                    break;
                }
            }
        }
        return level;
    }
    //endregion
}

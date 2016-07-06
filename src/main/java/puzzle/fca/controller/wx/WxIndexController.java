package puzzle.fca.controller.wx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.fca.Constants;
import puzzle.fca.controller.ModuleController;
import puzzle.fca.entity.FcaArticle;
import puzzle.fca.entity.FcaArticleCat;
import puzzle.fca.service.IFcaArticleCatService;
import puzzle.fca.service.IFcaArticleService;
import puzzle.fca.utils.ConvertUtil;
import puzzle.fca.utils.Page;
import puzzle.fca.utils.Result;
import puzzle.fca.utils.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller(value = "wxIndexController")
@RequestMapping(value = "/wx")
public class WxIndexController extends ModuleController{

    @Autowired
    private IFcaArticleCatService fcaArticleCatService;

    @Autowired
    private IFcaArticleService fcaArticleService;

    /**
     * 进入微信index页面加载的数据
     * @return
     */
    @RequestMapping(value = "/index")
    public String index(){
        Map map=new HashMap();
        String str=request.getParameter("catId");
        if(StringUtil.isNotNullOrEmpty(str)){
            map.put("catId",ConvertUtil.toInt(str));
            this.setModelAttribute("catId", ConvertUtil.toInt(str));
        }else{
            map.put("catId",1);
            this.setModelAttribute("catId", 1);
        }
        Page page=new Page();
        page.setPageSize(2);
        List<FcaArticle> articleList=fcaArticleService.queryList(map,page);
        for(int i=0;i<articleList.size();i++){
            articleList.get(i).setAddTimeString(ConvertUtil.toString(ConvertUtil.toDate(
                    articleList.get(i).getAddTime()
            ),"MM-dd"));
        }
        List<FcaArticleCat> articleCatList=fcaArticleCatService.queryList(null);
        this.setModelAttribute("articleList",articleList);
        this.setModelAttribute("articleCatList",articleCatList);
        return Constants.UrlHelper.WX_INDEX;
    }

    @RequestMapping(value = "/index/query.do")
    @ResponseBody
    public Result query(Integer catId,Page page){
        Result result=new Result();
        try{
            Map map=new HashMap();
            map.put("catId",catId);
            page.setPageSize(2);
            List<FcaArticle> articleList=fcaArticleService.queryList(map,page);
            for(int i=0;i<articleList.size();i++){
                articleList.get(i).setAddTimeString(ConvertUtil.toString(ConvertUtil.toDate(
                        articleList.get(i).getAddTime()
                ),"MM-dd"));
            }
            result.setData(articleList);
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("加载文章信息出错！");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }
}

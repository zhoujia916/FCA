package puzzle.fca.controller.wx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.fca.Constants;
import puzzle.fca.controller.BaseController;
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
@RequestMapping(value = "/wx/index")
public class WxIndexController extends BaseController {

    @Autowired
    private IFcaArticleCatService fcaArticleCatService;

    @Autowired
    private IFcaArticleService fcaArticleService;

    @RequestMapping(value = "/cs")
    public String cs(){
        return Constants.UrlHelper.WX_TEST;
    }

    /**
     * 进入微信index页面加载的数据
     * @return
     */
    @RequestMapping(value = "/into")
    public String index(){
        Map<String, Object> map=new HashMap<String, Object>();
        Page page=new Page();
        page.setPageSize(Constants.WX_DEFAULT_PAGESIZE);
        String str=request.getParameter("catId");
        if(StringUtil.isNotNullOrEmpty(str)){
            map.put("catId",ConvertUtil.toInt(str));
            this.setModelAttribute("catId", ConvertUtil.toInt(str));
        }
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

    /**
     * 微信index页面加载更多信息
     * @param catId
     * @param page
     * @return
     */
    @RequestMapping(value = "/query.do")
    @ResponseBody
    public Result query(Integer catId,Page page){
        Result result=new Result();
        try{
            if(catId == null || page == null) {
                result.setCode(-1);
                result.setMsg("查询参数出错！");
                return result;
            }
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("catId", catId);
            page.setPageSize(Constants.WX_DEFAULT_PAGESIZE);
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

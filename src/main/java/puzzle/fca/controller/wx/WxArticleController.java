package puzzle.fca.controller.wx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import puzzle.fca.Constants;
import puzzle.fca.controller.ModuleController;
import puzzle.fca.entity.FcaArticle;
import puzzle.fca.entity.FcaArticleCat;
import puzzle.fca.service.IFcaArticleCatService;
import puzzle.fca.service.IFcaArticleService;
import puzzle.fca.utils.ConvertUtil;
import puzzle.fca.utils.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller(value = "wxArticleController")
@RequestMapping(value = "/wx")
public class WxArticleController extends ModuleController{

    @Autowired
    private IFcaArticleCatService fcaArticleCatService;

    @Autowired
    private IFcaArticleService fcaArticleService;

    /**
     * 查询微信article.htrml页面的文章全文
     * @param articleId
     * @return
     */
    @RequestMapping(value = "/article/{articleId}")
    public String article(@PathVariable Integer articleId){
        Map map=new HashMap();
        map.put("articleId",articleId);
        FcaArticle article=fcaArticleService.query(map);
        article.setAddTimeString(ConvertUtil.toString(ConvertUtil.toDate(
                article.getAddTime()
        ),Constants.DATE_FORMAT));
        List<FcaArticleCat> articleCatList=fcaArticleCatService.queryList(null);
        this.setModelAttribute("article",article);
        this.setModelAttribute("articleCatList",articleCatList);
        return Constants.UrlHelper.WX_ARTICLE;
    }
}

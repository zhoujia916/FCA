package puzzle.fca.controller.admin;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.fca.Constants;
import puzzle.fca.controller.ModuleController;
import puzzle.fca.entity.FcaAd;
import puzzle.fca.entity.FcaAdPosition;
import puzzle.fca.entity.SystemMenuAction;
import puzzle.fca.service.IFcaAdPositionService;
import puzzle.fca.service.IFcaAdService;
import puzzle.fca.utils.ConvertUtil;
import puzzle.fca.utils.Page;
import puzzle.fca.utils.Result;
import puzzle.fca.utils.StringUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller (value = "adminFcaAdController")
@RequestMapping (value = "/admin/fcaad")
public class FcaAdController extends ModuleController {

    @Autowired
    private IFcaAdService autoAdService;

    @Autowired
    private IFcaAdPositionService autoAdPositionService;

    @RequestMapping(value = "/add")
    public String add(){
        List<FcaAdPosition> list=autoAdPositionService.queryList(null);
        this.setModelAttribute("list",list);
        this.setModelAttribute("action",Constants.PageHelper.PAGE_ACTION_CREATE);
        return Constants.UrlHelper.ADMIN_FCA_AD_ADD;
    }

    @RequestMapping (value = {"/","/index"})
    public String index(){
        List<SystemMenuAction> actions = getActions();
        this.setModelAttribute("actions", actions);
        List<FcaAdPosition> list=autoAdPositionService.queryList(null);
        this.setModelAttribute("list",list);
        return Constants.UrlHelper.ADMIN_FCA_AD;
    }

    @RequestMapping (value = "/list.do")
    @ResponseBody
    public Result list(FcaAd autoAd,Page page){
        Result result=new Result();
        try{
            Map<String, Object> map=new HashMap<String, Object>();
            if(autoAd!=null) {
                if (autoAd.getAdPositionId() != null && autoAd.getAdPositionId() > 0) {
                    map.put("adPositionId", autoAd.getAdPositionId());
                }
                if (StringUtil.isNotNullOrEmpty(autoAd.getBeginTimeString())) {
                    map.put("startDate", ConvertUtil.toLong(ConvertUtil.toDateTime(autoAd.getBeginTimeString() + " 00:00:00")));
                }
                if (StringUtil.isNotNullOrEmpty(autoAd.getEndTimeString())) {
                    map.put("endDate", ConvertUtil.toLong(ConvertUtil.toDateTime(autoAd.getEndTimeString() + " 23:59:59")));
                }
            }
            List<FcaAd> list=autoAdService.queryList(map,page);
            if(list!=null && list.size()>0){
                JSONArray array=new JSONArray();
                for(FcaAd ad:list){
                    JSONObject jsonObject=JSONObject.fromObject(ad);
                    jsonObject.put("startDate",ConvertUtil.toString(ConvertUtil.toDate(ad.getStartDate())));
                    jsonObject.put("endDate",ConvertUtil.toString(ConvertUtil.toDate(ad.getEndDate())));
                    jsonObject.put("beginTimeString",ConvertUtil.toString(ConvertUtil.toDate(ad.getStartDate()),"yyyy-MM-dd"));
                    jsonObject.put("endTimeString",ConvertUtil.toString(ConvertUtil.toDate(ad.getEndDate()),"yyyy-MM-dd"));
                    jsonObject.put("addTime",ConvertUtil.toString(ConvertUtil.toDate(ad.getAddTime())));
                    array.add(jsonObject);
                }
                result.setData(array);
                result.setTotal(page.getTotal());
            }
        }catch (Exception e){
            result.setCode(1);
            result.setMsg("获取广告信息出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    @RequestMapping (value = "/action.do")
    @ResponseBody
    public Result action(String action,FcaAd fcaAd){
        Result result=new Result();
        try{
            if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_CREATE)){
                fcaAd.setAddTime(ConvertUtil.toLong(new Date()));
                fcaAd.setStartDate(ConvertUtil.toLong(ConvertUtil.toDateTime(fcaAd.getBeginTimeString()+" 00:00:00")));
                fcaAd.setEndDate(ConvertUtil.toLong(ConvertUtil.toDateTime(fcaAd.getEndTimeString()+" 23:59:59")));
                fcaAd.setStatus(1);
                if(!autoAdService.insert(fcaAd)){
                    result.setCode(1);
                    result.setMsg("添加广告信息时出错");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_CREATE,"添加广告信息");
                }
            }else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_UPDATE)){
                fcaAd.setStartDate(ConvertUtil.toLong(ConvertUtil.toDateTime(fcaAd.getBeginTimeString()+" 00:00:00")));
                fcaAd.setEndDate(ConvertUtil.toLong(ConvertUtil.toDateTime(fcaAd.getEndTimeString()+" 23:59:59")));
                if(!autoAdService.update(fcaAd)){
                    result.setCode(1);
                    result.setMsg("修改广告信息时出错");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_UPDATE,"修改广告信息");
                }
            }else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_DELETE)){
                Map<String, Object> map=new HashMap<String, Object>();
                String id=request.getParameter("id");
                String ids=request.getParameter("ids");
                if(StringUtil.isNotNullOrEmpty(id)){
                    map.put("adId",ConvertUtil.toInt(id));
                }else if(StringUtil.isNotNullOrEmpty(ids)){
                    String[] adIds=ids.split(",");
                    map.put("adIds",adIds);
                }
                if(!autoAdService.delete(map)){
                    result.setCode(1);
                    result.setMsg("删除广告信息时出错");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_DELETE,"删除特定的广告信息");
                }
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("操作广告信息时出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }
}

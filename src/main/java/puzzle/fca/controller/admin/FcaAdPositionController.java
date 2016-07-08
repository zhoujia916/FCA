package puzzle.fca.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import puzzle.fca.Constants;
import puzzle.fca.controller.ModuleController;
import puzzle.fca.entity.FcaAdPosition;
import puzzle.fca.entity.SystemMenuAction;
import puzzle.fca.service.IFcaAdPositionService;
import puzzle.fca.utils.ConvertUtil;
import puzzle.fca.utils.Page;
import puzzle.fca.utils.Result;
import puzzle.fca.utils.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller(value = "adminFcaAdPosition")
@RequestMapping (value = "/admin/fcaadposition")
public class FcaAdPositionController extends ModuleController {
    @Autowired
    private IFcaAdPositionService fcaAdPositionService;

    @RequestMapping (value = {"/","/index"})
    public String index(){
        List<SystemMenuAction> actions=getActions();
        this.setModelAttribute("actions", actions);
        return Constants.UrlHelper.ADMIN_FCA_AD_POSITION;
    }

    @RequestMapping(value = "/list.do")
    @ResponseBody
    public Result list(FcaAdPosition fcaAdPosition,Page page){
        Result result=new Result();
        try{
            Map<String, Object> map=new HashMap<String, Object>();
            if(fcaAdPosition!=null) {
                if(StringUtil.isNotNullOrEmpty(fcaAdPosition.getPositionName()))
                map.put("positionName", fcaAdPosition.getPositionName());
            }
            List<FcaAdPosition> list=fcaAdPositionService.queryList(map,page);
            if(list!=null && list.size()>0){
                result.setData(list);
                result.setTotal(page.getTotal());
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("获取广告位置信息出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }

    @RequestMapping (value = "/action.do")
    @ResponseBody
    public Result action(String action,FcaAdPosition autoAdPosition){
        Result result=new Result();
        try{
            if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_CREATE)){
                if(!fcaAdPositionService.insert(autoAdPosition)){
                    result.setCode(1);
                    result.setMsg("插入广告位置信息时出错");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_CREATE,"��ӹ��λ����Ϣ");
                }
            }else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_UPDATE)){
                if(!fcaAdPositionService.update(autoAdPosition)){
                    result.setCode(1);
                    result.setMsg("修改广告位置信息时出错");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_UPDATE,"�޸�ָ���Ĺ��λ����Ϣ");
                }
            }else if(action.equalsIgnoreCase(Constants.PageHelper.PAGE_ACTION_DELETE)){
                Map<String, Object> map=new HashMap<String, Object>();
                String id=request.getParameter("id");
                String ids=request.getParameter("ids");
                if(StringUtil.isNotNullOrEmpty(id)){
                    map.put("positionId", ConvertUtil.toInt(id));
                }else if(StringUtil.isNotNullOrEmpty(ids)){
                    String[] positionIds=ids.split(",");
                    map.put("positionIds",positionIds);
                }
                if(!fcaAdPositionService.delete(map)){
                    result.setCode(1);
                    result.setMsg("删除广告位置信息时出错");
                }else{
                    insertLog(Constants.PageHelper.PAGE_ACTION_DELETE,"ɾ���ض��Ĺ��λ����Ϣ");
                }
            }
        }catch(Exception e){
            result.setCode(1);
            result.setMsg("操作广告位置信息时出错");
            logger.error(result.getMsg()+e.getMessage());
        }
        return result;
    }
}

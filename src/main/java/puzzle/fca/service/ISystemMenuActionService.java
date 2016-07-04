package puzzle.fca.service;

import java.util.Date;
import java.util.Map;
import java.util.List;
import puzzle.fca.utils.Page;
import puzzle.fca.entity.SystemMenuAction;

public interface ISystemMenuActionService{ 

	public boolean insert(SystemMenuAction entity);

    public boolean insertBatch(List<SystemMenuAction> list);

    public boolean update(SystemMenuAction entity);

    public boolean delete(Map<String, Object> map);

    public SystemMenuAction query(Map<String, Object> map);
    
    public List<SystemMenuAction> queryList(Object param);

    public List<SystemMenuAction> queryList(Map<String, Object> map, Page page);
    
}

package puzzle.fca.service;

import java.util.Date;
import java.util.Map;
import java.util.List;
import puzzle.fca.utils.Page;
import puzzle.fca.entity.SystemUserMap;

public interface ISystemUserMapService{ 

	public boolean insert(SystemUserMap entity);

    public boolean insertBatch(List<SystemUserMap> list);

    public boolean update(SystemUserMap entity);

    public boolean delete(Map<String, Object> map);

    public SystemUserMap query(Map<String, Object> map);
    
    public List<SystemUserMap> queryList(Object param);

    public List<SystemUserMap> queryList(Map<String, Object> map, Page page);
    
}

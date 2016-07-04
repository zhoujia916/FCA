package puzzle.fca.service;

import java.util.Date;
import java.util.Map;
import java.util.List;
import puzzle.fca.utils.Page;
import puzzle.fca.entity.SystemDept;

public interface ISystemDeptService{ 

	public boolean insert(SystemDept entity);

    public boolean update(SystemDept entity);

    public boolean delete(Map<String, Object> map);

    public SystemDept query(Map<String, Object> map);
    
    public List<SystemDept> queryList(Object param);

    public List<SystemDept> queryList(Map<String, Object> map, Page page);
    
}

package puzzle.fca.service;

import java.util.Date;
import java.util.Map;
import java.util.List;
import puzzle.fca.utils.Page;
import puzzle.fca.entity.SystemUserGroup;

public interface ISystemUserGroupService{ 

	public boolean insert(SystemUserGroup entity);

    public boolean update(SystemUserGroup entity);

    public boolean delete(Map<String, Object> map);

    public SystemUserGroup query(Map<String, Object> map);
    
    public List<SystemUserGroup> queryList(Object param);

    public List<SystemUserGroup> queryList(Map<String, Object> map, Page page);
    
}

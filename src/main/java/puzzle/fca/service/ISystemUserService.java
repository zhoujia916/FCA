package puzzle.fca.service;

import java.util.Date;
import java.util.Map;
import java.util.List;

import puzzle.fca.entity.SystemAuthority;
import puzzle.fca.utils.Page;
import puzzle.fca.entity.SystemUser;

public interface ISystemUserService{ 

	public boolean insert(SystemUser entity);

    public boolean update(SystemUser entity);

    public boolean delete(Map<String, Object> map);

    public SystemUser query(Map<String, Object> map);
    
    public List<SystemUser> queryList(Object param);

    public List<SystemUser> queryList(Map<String, Object> map, Page page);

    public List<SystemAuthority> queryAuthority(int userId);
}

package puzzle.fca.service.impl;

import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import puzzle.fca.entity.FcaAdPosition;
import puzzle.fca.mapper.SqlMapper;
import puzzle.fca.service.IFcaAdPositionService;
import puzzle.fca.utils.Page;

@Service("FcaAdPositionService")
public class FcaAdPositionServiceImpl implements IFcaAdPositionService {
	
	@Autowired
	private SqlMapper sqlMapper;

	/**
	* 插入单条记录
	*/
	@Override
	public boolean insert(FcaAdPosition entity){
		return sqlMapper.insert("FcaAdPositionMapper.insert", entity);
	}

	/**
	* 更新单条记录
	*/
	@Override
    public boolean update(FcaAdPosition entity){
    	return sqlMapper.update("FcaAdPositionMapper.update", entity);
    }

	/**
	* 删除单条记录
	*/
	@Override
    public boolean delete(Map<String, Object> map){
    	return sqlMapper.delete("FcaAdPositionMapper.delete", map);
    }

	/**
	* 查询单条记录
	*/
	@Override
    public FcaAdPosition query(Map<String, Object> map){
    	return sqlMapper.query("FcaAdPositionMapper.query", map);
    }

	/**
	* 查询多条记录
	*/
	@Override
    public List<FcaAdPosition> queryList(Object param){
    	return sqlMapper.queryList("FcaAdPositionMapper.queryList", param);
    }
    
    
    /**
	* 查询分页记录
	*/
	@Override
    public List<FcaAdPosition> queryList(Map<String, Object> map, Page page){
    	return sqlMapper.queryList("FcaAdPositionMapper.queryList", map, page);
    }
}

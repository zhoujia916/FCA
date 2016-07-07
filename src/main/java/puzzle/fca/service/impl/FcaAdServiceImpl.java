package puzzle.fca.service.impl;

import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import puzzle.fca.entity.FcaAd;
import puzzle.fca.mapper.SqlMapper;
import puzzle.fca.service.IFcaAdService;
import puzzle.fca.utils.Page;

@Service("FcaAdService")
public class FcaAdServiceImpl implements IFcaAdService {
	
	@Autowired
	private SqlMapper sqlMapper;
	
	/**
	* 插入单条记录
	*/
	@Override
	public boolean insert(FcaAd entity){
		return sqlMapper.insert("FcaAdMapper.insert", entity);
	}

	/**
	* 更新单条记录
	*/
	@Override
    public boolean update(FcaAd entity){
    	return sqlMapper.update("FcaAdMapper.update", entity);
    }

	/**
	* 删除单条记录
	*/
	@Override
    public boolean delete(Map<String, Object> map){
    	return sqlMapper.delete("FcaAdMapper.delete", map);
    }

	/**
	* 查询单条记录
	*/
	@Override
    public FcaAd query(Map<String, Object> map){
    	return sqlMapper.query("FcaAdMapper.query", map);
    }

	/**
	* 查询多条记录
	*/
	@Override
    public List<FcaAd> queryList(Object param){
    	return sqlMapper.queryList("FcaAdMapper.queryList", param);
    }
    
    
    /**
	* 查询分页记录
	*/
	@Override
    public List<FcaAd> queryList(Map<String, Object> map, Page page){
    	return sqlMapper.queryList("FcaAdMapper.queryList", map, page);
    }
}

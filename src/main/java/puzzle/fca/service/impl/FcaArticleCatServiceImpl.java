package puzzle.fca.service.impl;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import puzzle.fca.entity.FcaArticleCat;
import puzzle.fca.service.IFcaArticleCatService;
import puzzle.fca.utils.Page;
import puzzle.fca.mapper.SqlMapper;

@Service("fcaArticleCatService")
public class FcaArticleCatServiceImpl implements IFcaArticleCatService {
	
	@Autowired
	private SqlMapper sqlMapper;
	
	/**
	* 插入单条记录
	*/
	@Override
	public boolean insert(FcaArticleCat entity){
		return sqlMapper.insert("FcaArticleCatMapper.insert", entity);
	}

	/**
	* 更新单条记录
	*/
	@Override
    public boolean update(FcaArticleCat entity){
    	return sqlMapper.update("FcaArticleCatMapper.update", entity);
    }

	/**
	* 删除单条记录
	*/
	@Override
    public boolean delete(Map<String, Object> map){
    	return sqlMapper.delete("FcaArticleCatMapper.delete", map);
    }

	/**
	* 查询单条记录
	*/
	@Override
    public FcaArticleCat query(Map<String, Object> map){
    	return sqlMapper.query("FcaArticleCatMapper.query", map);
    }

	/**
	* 查询多条记录
	*/
	@Override
    public List<FcaArticleCat> queryList(Object param){
    	return sqlMapper.queryList("FcaArticleCatMapper.queryList", param);
    }
    
    
    /**
	* 查询分页记录
	*/
	@Override
    public List<FcaArticleCat> queryList(Map<String, Object> map, Page page){
    	return sqlMapper.queryList("FcaArticleCatMapper.queryList", map, page);
    }
    
}

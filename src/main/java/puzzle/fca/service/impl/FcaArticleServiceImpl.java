package puzzle.fca.service.impl;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import puzzle.fca.entity.FcaArticle;
import puzzle.fca.service.IFcaArticleService;
import puzzle.fca.utils.Page;
import puzzle.fca.mapper.SqlMapper;

@Service("fcaArticleService")
public class FcaArticleServiceImpl implements IFcaArticleService {
	
	@Autowired
	private SqlMapper sqlMapper;
	
	/**
	* 插入单条记录
	*/
	@Override
	public boolean insert(FcaArticle entity){
		return sqlMapper.insert("FcaArticleMapper.insert", entity);
	}

	/**
	* 更新单条记录
	*/
	@Override
    public boolean update(FcaArticle entity){
    	return sqlMapper.update("FcaArticleMapper.update", entity);
    }

	/**
	* 删除单条记录
	*/
	@Override
    public boolean delete(Map<String, Object> map){
    	return sqlMapper.delete("FcaArticleMapper.delete", map);
    }

	/**
	* 查询单条记录
	*/
	@Override
    public FcaArticle query(Map<String, Object> map){
    	return sqlMapper.query("FcaArticleMapper.query", map);
    }

	/**
	* 查询多条记录
	*/
	@Override
    public List<FcaArticle> queryList(Object param){
    	return sqlMapper.queryList("FcaArticleMapper.queryList", param);
    }
    
    
    /**
	* 查询分页记录
	*/
	@Override
    public List<FcaArticle> queryList(Map<String, Object> map, Page page){
    	return sqlMapper.queryList("FcaArticleMapper.queryList", map, page);
    }
    
}

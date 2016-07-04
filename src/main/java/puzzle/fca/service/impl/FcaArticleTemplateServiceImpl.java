package puzzle.fca.service.impl;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import puzzle.fca.entity.FcaArticleTemplate;
import puzzle.fca.service.IFcaArticleTemplateService;
import puzzle.fca.utils.Page;
import puzzle.fca.mapper.SqlMapper;

@Service("fcaArticleTemplateService")
public class FcaArticleTemplateServiceImpl implements IFcaArticleTemplateService {
	
	@Autowired
	private SqlMapper sqlMapper;
	
	/**
	* 插入单条记录
	*/
	@Override
	public boolean insert(FcaArticleTemplate entity){
		return sqlMapper.insert("FcaArticleTemplateMapper.insert", entity);
	}

	/**
	* 更新单条记录
	*/
	@Override
    public boolean update(FcaArticleTemplate entity){
    	return sqlMapper.update("FcaArticleTemplateMapper.update", entity);
    }

	/**
	* 删除单条记录
	*/
	@Override
    public boolean delete(Map<String, Object> map){
    	return sqlMapper.delete("FcaArticleTemplateMapper.delete", map);
    }

	/**
	* 查询单条记录
	*/
	@Override
    public FcaArticleTemplate query(Map<String, Object> map){
    	return sqlMapper.query("FcaArticleTemplateMapper.query", map);
    }

	/**
	* 查询多条记录
	*/
	@Override
    public List<FcaArticleTemplate> queryList(Object param){
    	return sqlMapper.queryList("FcaArticleTemplateMapper.queryList", param);
    }
    
    
    /**
	* 查询分页记录
	*/
	@Override
    public List<FcaArticleTemplate> queryList(Map<String, Object> map, Page page){
    	return sqlMapper.queryList("FcaArticleTemplateMapper.queryList", map, page);
    }
    
}

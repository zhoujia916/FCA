package puzzle.fca.service;

import java.util.Date;
import java.util.Map;
import java.util.List;
import puzzle.fca.utils.Page;
import puzzle.fca.entity.FcaArticle;

public interface IFcaArticleService{ 

	public boolean insert(FcaArticle entity);

    public boolean update(FcaArticle entity);

    public boolean delete(Map<String, Object> map);

    public FcaArticle query(Map<String, Object> map);
    
    public List<FcaArticle> queryList(Object param);

    public List<FcaArticle> queryList(Map<String, Object> map, Page page);
    
}

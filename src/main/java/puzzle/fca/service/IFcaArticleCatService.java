package puzzle.fca.service;

import java.util.Date;
import java.util.Map;
import java.util.List;
import puzzle.fca.utils.Page;
import puzzle.fca.entity.FcaArticleCat;

public interface IFcaArticleCatService{ 

	public boolean insert(FcaArticleCat entity);

    public boolean update(FcaArticleCat entity);

    public boolean delete(Map<String, Object> map);

    public FcaArticleCat query(Map<String, Object> map);
    
    public List<FcaArticleCat> queryList(Object param);

    public List<FcaArticleCat> queryList(Map<String, Object> map, Page page);
    
}

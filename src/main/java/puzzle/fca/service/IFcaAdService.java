package puzzle.fca.service;

import java.util.Map;
import java.util.List;

import puzzle.fca.entity.FcaAd;
import puzzle.fca.utils.Page;

public interface IFcaAdService{ 

	public boolean insert(FcaAd entity);

    public boolean update(FcaAd entity);

    public boolean delete(Map<String, Object> map);

    public FcaAd query(Map<String, Object> map);
    
    public List<FcaAd> queryList(Object param);

    public List<FcaAd> queryList(Map<String, Object> map, Page page);
}

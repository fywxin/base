package org.whale.ext.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.whale.ext.dao.AttrDao;
import org.whale.ext.dao.DomainDao;
import org.whale.ext.domain.Attr;
import org.whale.ext.domain.Domain;
import org.whale.system.base.BaseDao;
import org.whale.system.base.Query;
import org.whale.system.common.util.Strings;
import org.whale.system.service.BaseService;

@Service
public class DomainService extends BaseService<Domain, Long> {
	
	@Autowired
	private DomainDao domainDao;
	@Autowired
	private AttrDao attrDao;

	@Override
	public void save(Domain domain) {
		this.domainDao.save(domain);
		List<Attr> attrs = domain.getAttrs();
		if(attrs != null && attrs.size() > 0){
			for(Attr attr : attrs){
				attr.setDomainId(domain.getId());
				attr.setAttrId(null);
				if(Strings.isBlank(attr.getSqlName())){
					attr.setSqlName(attr.getName());
				}
				this.attrDao.save(attr);
			}
		}
	}

	@Override
	public void update(Domain domain) {
		this.domainDao.update(domain);
		this.attrDao.deleteBy(Query.newQuery(Attr.class).addEq("domainId", domain.getId()));
		
		List<Attr> attrs = domain.getAttrs();
		if(attrs != null && attrs.size() > 0){
			for(Attr attr : attrs){
				attr.setDomainId(domain.getId());
				this.attrDao.save(attr);
			}
		}
	}

	@Override
	public void delete(Long id) {
		this.domainDao.delete(id);
		this.attrDao.deleteBy(Query.newQuery(Attr.class).addEq("domainId", id));
	}

	@Override
	public Domain get(Long id) {
		Domain domain = this.domainDao.get(id);
		if(domain == null)
			return null;
		List<Attr> attrs = this.attrDao.queryByDomainId(id);
		domain.setAttrs(attrs);
		
		return domain;
	}
	
	public Domain getBySqlName(String dbName){
		if(Strings.isBlank(dbName))
			return null;
		Domain domain = this.domainDao.getBySqlName(dbName.trim());
		if(domain == null)
			return null;
		List<Attr> attrs = this.attrDao.queryByDomainId(domain.getId());
		domain.setAttrs(attrs);
		
		return domain;
	}
	
	/**
	 * 获取数据库所有表
	 * @return
	 */
	public List<Map<String, Object>> queryAllTable(){
		return this.domainDao.queryAllTable();
	}
	
	/**
	 * 根据表名获取其所有的字段列表
	 * @param table
	 * @return
	 */
	public List<Map<String, Object>> queryColsByTable(String table){
		if(Strings.isBlank(table))
			return null;
		return this.attrDao.queryColsByTable(table);
	}

	@Override
	public BaseDao<Domain, Long> getDao() {
		return domainDao;
	}

}

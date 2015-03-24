package org.whale.ext.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.whale.ext.dao.AttrDao;
import org.whale.ext.dao.DomainDao;
import org.whale.ext.domain.Attr;
import org.whale.ext.domain.Domain;
import org.whale.system.base.BaseDao;
import org.whale.system.base.BaseService;
import org.whale.system.common.util.Strings;

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
				attr.setId(null);
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
		Attr ar = new Attr();
		ar.setDomainId(domain.getId());
		this.attrDao.deleteBy(ar);
		
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
		Attr attr = new Attr();
		attr.setDomainId(id);
		this.attrDao.deleteBy(attr);
	}

	@Override
	public Domain get(Long id) {
		Domain domain = this.domainDao.get(id);
		if(domain == null)
			return null;
		List<Attr> attrs = this.attrDao.getByDomainId(id);
		domain.setAttrs(attrs);
		
		return domain;
	}
	
	public Domain getByName(String name){
		if(Strings.isBlank(name))
			return null;
		return this.domainDao.getByName(name.trim());
	}
	
	public Domain getByClazzName(String clazzName){
		if(Strings.isBlank(clazzName))
			return null;
		Domain domain = this.domainDao.getByClazzName(clazzName.trim());
		if(domain == null)
			return null;
		List<Attr> attrs = this.attrDao.getByDomainId(domain.getId());
		domain.setAttrs(attrs);
		
		return domain;
	}

	@Override
	public BaseDao<Domain, Long> getDao() {
		return domainDao;
	}

}

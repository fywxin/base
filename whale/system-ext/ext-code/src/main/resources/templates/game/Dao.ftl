package ${domain.pkgName!"com.cyou.fz.trade"}.${domain.domainName?uncap_first}.dao;

import org.springframework.stereotype.Repository;

import com.cyou.fz.commons.mybatis.selecterplus.mybatis.dao.BaseDAO;
import ${domain.pkgName!"org.whale.system"}.${domain.domainName?uncap_first}.bean.${domain.domainName};

/**
* Created by wjs on ${.now?date}.
*/
@Repository
public interface ${domain.domainName}Dao extends BaseDAO<${domain.domainName}> {
	
}
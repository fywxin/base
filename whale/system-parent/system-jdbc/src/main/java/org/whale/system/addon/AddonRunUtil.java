package org.whale.system.addon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.OrderComparator;
import org.springframework.stereotype.Component;
import org.whale.system.base.BaseDao;

@SuppressWarnings("all")
@Component
public class AddonRunUtil {
	
	private static final Logger log = LoggerFactory.getLogger(AddonRunUtil.class);

	@Autowired(required = false)
	private List<IBaseDaoAddon> addons;
	
	private boolean isSort = false;
	
	public void runBeforeSave(Object obj, BaseDao baseDao){
		for(IBaseDaoAddon addon : addons){
			if(addon.access(obj, baseDao)){
				addon.beforeSave(obj, baseDao);
			}
		}
	}
	
	public void runAfterSave(Object obj, BaseDao baseDao){
		for(IBaseDaoAddon addon : addons){
			if(addon.access(obj, baseDao)){
				addon.afterSave(obj, baseDao);
			}
		}
	}
	
	public void runBeforeUpdate(Object obj, BaseDao baseDao){
		for(IBaseDaoAddon addon : addons){
			if(addon.access(obj, baseDao)){
				addon.beforeUpdate(obj, baseDao);
			}
		}
	}
	
	public void runAfterUpdate(Object obj, BaseDao baseDao){
		for(IBaseDaoAddon addon : addons){
			if(addon.access(obj, baseDao)){
				addon.afterUpdate(obj, baseDao);
			}
		}
	}
	
	public void runBeforeDelete(Object obj, BaseDao baseDao){
		for(IBaseDaoAddon addon : addons){
			if(addon.access(obj, baseDao)){
				addon.beforeDelete(obj, baseDao);
			}
		}
	}
	
	public void runAfterDelete(Object obj, BaseDao baseDao){
		for(IBaseDaoAddon addon : addons){
			if(addon.access(obj, baseDao)){
				addon.afterDelete(obj, baseDao);
			}
		}
	}
	
	public void runBeforeSaveBatch(List objs, BaseDao baseDao){
		for(IBaseDaoAddon addon : addons){
			if(addon.access(objs, baseDao)){
				addon.beforeSaveBatch(objs, baseDao);
			}
		}
	}
	
	public void runAfterSaveBatch(List objs, BaseDao baseDao){
		for(IBaseDaoAddon addon : addons){
			if(addon.access(objs, baseDao)){
				addon.afterSaveBatch(objs, baseDao);
			}
		}
	}
	
	public void runBeforeUpdateBatch(List objs, BaseDao baseDao){
		for(IBaseDaoAddon addon : addons){
			if(addon.access(objs, baseDao)){
				addon.beforeUpdateBatch(objs, baseDao);
			}
		}
	}
	
	public void runAfterUpdateBatch(List objs, BaseDao baseDao){
		for(IBaseDaoAddon addon : addons){
			if(addon.access(objs, baseDao)){
				addon.afterUpdateBatch(objs, baseDao);
			}
		}
	}
	
	public void runBeforeDeleteBatch(List objs, BaseDao baseDao){
		for(IBaseDaoAddon addon : addons){
			if(addon.access(objs, baseDao)){
				addon.beforeDeleteBatch(objs, baseDao);
			}
		}
	}
	
	public void runAfterDeleteBatch(List objs, BaseDao baseDao){
		for(IBaseDaoAddon addon : addons){
			if(addon.access(objs, baseDao)){
				addon.afterDeleteBatch(objs, baseDao);
			}
		}
	}
	
	
	public List<IBaseDaoAddon> getAddons(){
		if(addons == null || addons.size() < 1){
			log.warn("ORM: 容器中没有插件存在");
			return new ArrayList<IBaseDaoAddon>(0);
		}else{
			if(!isSort){
				synchronized (this) {
					if(!isSort){
						Collections.sort(addons, new OrderComparator());
						isSort = true;
					}
				}
			}
			return addons;
		}
	}
}

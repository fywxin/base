package org.whale.system.jdbc.orm.table.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.whale.system.jdbc.orm.entry.Acolumn;
import org.whale.system.jdbc.orm.entry.Atable;

/**
 * 获取类反射对象信息
 *
 * @author wjs
 * 2014年9月6日-下午1:48:33
 */
@Component
public class TableBuilder {
	
	private static Logger logger = LoggerFactory.getLogger(TableBuilder.class);

	public Atable parse(Class<?> clazz){
		Atable table = new Atable();
		table.setClazz(clazz);
		table.setEntityName(clazz.getName().substring(clazz.getName().lastIndexOf(".")+1));
		table.setSuperClazz(clazz.getSuperclass());
		table.setCols(this.getColumns(clazz));
		
		//递归获取父类信息
		if(!(clazz.getSuperclass() instanceof Object)){
			table.setParent(parse(clazz.getSuperclass()));
		}
		logger.info("ORM：通过反射获取clazz={} 实体信息完成：{}", clazz, table);
		return table;
	}
	
	private List<Acolumn> getColumns(Class<?> clazz){
		Field[] fields = clazz.getDeclaredFields();
		List<Acolumn> list = new ArrayList<Acolumn>(fields.length);
		Acolumn column = null;
		for(Field field : fields) {
			//排除 static 与 final 字段
			if ((field.getModifiers()>>3) % 2 == 1 || (field.getModifiers()>>4) % 2 == 1){
				continue;
			}
			field.setAccessible(true);
			column = new Acolumn();
			column.setAttrName(field.getName());
			column.setAttrType(field.getType());
			column.setField(field);
			list.add(column);
		}
		return list;
	}
	
}

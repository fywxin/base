package org.whale.ext.domain;

import java.util.ArrayList;
import java.util.List;

import org.whale.system.base.BaseEntry;
import org.whale.system.common.exception.SysException;
import org.whale.system.jdbc.annotation.Column;
import org.whale.system.jdbc.annotation.Id;
import org.whale.system.jdbc.annotation.Table;
import org.whale.system.jdbc.annotation.Validate;

/**
 * 实体对象
 *
 * @author 王金绍
 * 2014年9月10日-上午10:12:48
 */
@Table(value="sys_domian", cnName="实体对象")
public class Domain extends BaseEntry{

	private static final long serialVersionUID = -23042834921L;

	@Id
	@Column(cnName="id")
	private Long id;
	
	@Validate(required=true)
	@Column(cnName="实体名", unique=true)
	private String name;
	
	@Validate(required=true)
	@Column(cnName="中文名", unique=true)
	private String cnName;
	
	@Validate(required=true)
	@Column(cnName="数据库", unique=true)
	private String dbName;
	
	@Validate(required=true)
	@Column(cnName="类名", unique=true)
	private String clazzName;
	
	@Column(cnName="是否树")
	private boolean isTree;
	
	@Column(cnName="包名称")
	private String pkgName;
	
	@Column(cnName="表单列数")
	private int formColNum;
	
	//主键
	private Attr idAttr;
	
	private List<Attr> attrs;
	
	private List<Attr> listAttrs = new ArrayList<Attr>();
	
	private List<Attr> formAttrs = new ArrayList<Attr>();
	
	private List<Attr> queryAttrs = new ArrayList<Attr>();
	
	
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCnName() {
		return cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public boolean getIsTree() {
		return isTree;
	}

	public void setIsTree(boolean isTree) {
		this.isTree = isTree;
	}

	public int getFormColNum() {
		return formColNum;
	}

	public void setFormColNum(int formColNum) {
		this.formColNum = formColNum;
	}

	public Attr getIdAttr() {
		return idAttr;
	}

	public void setIdAttr(Attr idAttr) {
		this.idAttr = idAttr;
	}

	public List<Attr> getAttrs() {
		return attrs;
	}

	public void setAttrs(List<Attr> attrs) {
		this.attrs = attrs;
		if(attrs != null && attrs.size() > 0){
			this.queryAttrs.clear();
			this.listAttrs.clear();
			this.formAttrs.clear();
			for(Attr attr : attrs){
				if(attr.getIsId()) {
					this.idAttr = attr;
				}
				if(attr.getInQuery()){
					this.queryAttrs.add(attr);
				}
				if(attr.getInList()){
					this.listAttrs.add(attr);
				}
				if(attr.getInForm()){
					this.formAttrs.add(attr);
				}
			}
		}else{
			throw new SysException("字段集合为空");
		}
	}

	public List<Attr> getListAttrs() {
		return listAttrs;
	}

	public void setListAttrs(List<Attr> listAttrs) {
		this.listAttrs = listAttrs;
	}

	public List<Attr> getFormAttrs() {
		return formAttrs;
	}

	public void setFormAttrs(List<Attr> formAttrs) {
		this.formAttrs = formAttrs;
	}

	public List<Attr> getQueryAttrs() {
		return queryAttrs;
	}

	public void setQueryAttrs(List<Attr> queryAttrs) {
		this.queryAttrs = queryAttrs;
	}

	public String getClazzName() {
		return clazzName;
	}

	public void setClazzName(String clazzName) {
		this.clazzName = clazzName;
	}

	public String getPkgName() {
		return pkgName;
	}

	public void setPkgName(String pkgName) {
		this.pkgName = pkgName;
	}
	
}

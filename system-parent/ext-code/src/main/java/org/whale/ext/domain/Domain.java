package org.whale.ext.domain;

import java.util.ArrayList;
import java.util.List;

import org.whale.system.annotation.jdbc.Column;
import org.whale.system.annotation.jdbc.Id;
import org.whale.system.annotation.jdbc.Table;
import org.whale.system.annotation.jdbc.Validate;
import org.whale.system.base.BaseEntry;
import org.whale.system.common.util.PropertiesUtil;

/**
 * 实体对象
 *
 * @author wjs
 * 2014年9月10日-上午10:12:48
 */
@Table(value="sys_domian", cnName="实体对象")
public class Domain extends BaseEntry{

	private static final long serialVersionUID = -23042834921L;

	@Id
	@Column(cnName="id")
	private Long id;
	
	@Validate(required=true)
	@Column(cnName="实体名")
	private String domainName;
	
	@Validate(required=true)
	@Column(cnName="中文名")
	private String domainCnName;
	
	@Validate(required=true)
	@Column(cnName="数据库", unique=true)
	private String domainSqlName;
	
	@Column(cnName="基础包路径")
	private String pkgName = "org.whale.system";
	
	//树模型
	private Integer treeModel;
	
	private String treeId;
	
	private String treePid;
	
	private String treeName;
	
	//模板类型
	private Integer ftlType;
	
	//代码路径
	private String codePath;
	
	private String author = PropertiesUtil.getValue("author", "wjs");
	
	//主键
	private Attr idAttr;
	
	private List<Attr> attrs;
	
	private List<Attr> listAttrs = new ArrayList<Attr>();
	
	private List<Attr> formAttrs = new ArrayList<Attr>();
	
	private List<Attr> queryAttrs = new ArrayList<Attr>();

	public Long getId() {
		return id;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getDomainCnName() {
		return domainCnName;
	}

	public void setDomainCnName(String domainCnName) {
		this.domainCnName = domainCnName;
	}

	public String getDomainSqlName() {
		return domainSqlName;
	}

	public void setDomainSqlName(String domainSqlName) {
		this.domainSqlName = domainSqlName;
	}

	public String getPkgName() {
		return pkgName;
	}

	public void setPkgName(String pkgName) {
		this.pkgName = pkgName;
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

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getFtlType() {
		return ftlType;
	}

	public void setFtlType(Integer ftlType) {
		this.ftlType = ftlType;
	}

	public String getCodePath() {
		return codePath;
	}

	public void setCodePath(String codePath) {
		this.codePath = codePath;
	}

	public Integer getTreeModel() {
		return treeModel;
	}

	public void setTreeModel(Integer treeModel) {
		this.treeModel = treeModel;
	}

	public String getTreeId() {
		return treeId;
	}

	public void setTreeId(String treeId) {
		this.treeId = treeId;
	}

	public String getTreePid() {
		return treePid;
	}

	public void setTreePid(String treePid) {
		this.treePid = treePid;
	}

	public String getTreeName() {
		return treeName;
	}

	public void setTreeName(String treeName) {
		this.treeName = treeName;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
}

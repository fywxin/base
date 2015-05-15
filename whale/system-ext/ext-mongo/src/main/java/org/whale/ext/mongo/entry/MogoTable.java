package org.whale.ext.mongo.entry;

import java.util.List;

import org.whale.system.common.reflect.Atable;


public class MogoTable {

	private Atable table;
	
	private String collectionName;
	
	private String cnName;
	
	private List<MogoColumn> cols;
	
	private MogoColumn idCol;

	public Atable getTable() {
		return table;
	}

	public void setTable(Atable table) {
		this.table = table;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	public String getCnName() {
		return cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	public List<MogoColumn> getCols() {
		return cols;
	}

	public void setCols(List<MogoColumn> cols) {
		this.cols = cols;
	}

	public MogoColumn getIdCol() {
		return idCol;
	}

	public void setIdCol(MogoColumn idCol) {
		this.idCol = idCol;
	}
}

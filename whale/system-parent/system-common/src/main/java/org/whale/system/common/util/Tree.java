package org.whale.system.common.util;

import java.util.List;

public class Tree<T extends ITreeNode> {
	
	private boolean isRoot;
	
	private boolean isLeaf;
	
	private int level;
	
	private T node;
	
	private T parentNode;
	
	private List<T> subNodes;

	public boolean isRoot() {
		return isRoot;
	}

	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}

	public boolean isLeaf() {
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public T getNode() {
		return node;
	}

	public void setNode(T node) {
		this.node = node;
	}

	public List<T> getSubNodes() {
		return subNodes;
	}

	public void setSubNodes(List<T> subNodes) {
		this.subNodes = subNodes;
	}

	public T getParentNode() {
		return parentNode;
	}

	public void setParentNode(T parentNode) {
		this.parentNode = parentNode;
	}
	
}

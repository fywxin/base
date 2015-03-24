package org.whale.system.common.util;

import java.util.ArrayList;
import java.util.List;

public class TreeUtil {

	/**
	 * 返回子树结构
	 * @param list
	 * @param rootId
	 * @return
	 */
	public static <T extends ITreeNode> Tree<T> parseTree(List<T> list, Long rootId){
		if(list == null || list.size() < 1)
			return null;
		T root = getNode(list, rootId);
		if(root == null)
			root = getNode(list, null);
		if(root == null)
			root = getNode(list, 0L);
		if(root == null)
			throw new RuntimeException("找不到rootId["+rootId+"]的对象");
		
		return parseTree(list, root);
	}
	
	/**
	 * 创建树结构
	 * @param list
	 * @param root
	 * @return
	 */
	public static <T extends ITreeNode> Tree<T> parseTree(List<T> list, T root){
		
		return parseTree(list, root, null);
	}
	
	/**
	 * 创建树结构
	 * @param list
	 * @param node
	 * @param parent
	 * @return
	 */
	public static <T extends ITreeNode> Tree<T> parseTree(List<T> list, T node, Tree<T> parent){
		Tree<T> tree = new Tree<T>();
		tree.setLeaf(false);
		if(parent == null){
			tree.setRoot(true);
			tree.setLevel(1);
			tree.setParentNode(null);
			tree.setNode(node);
		}else{
			tree.setRoot(false);
			tree.setLevel(parent.getLevel()+1);
			tree.setParentNode(parent.getNode());
			tree.setNode(node);
		}
		
		List<T> subNodes = getSubNodes(list, node.id());
		if(subNodes != null && subNodes.size() > 0){
			for(T t : subNodes){
				parseTree(list, t, tree);
			}
		}else{
			tree.setLeaf(true);
		}
		tree.setSubNodes(subNodes);
		return tree;
	}
	
	/**
	 * 获取次级节点
	 * @param list
	 * @param id
	 * @return
	 */
	public static <T extends ITreeNode> List<T> getSubNodes(List<T> list, Long id){
		List<T> rs = new ArrayList<T>();
		
		if(list != null && list.size() > 0){
			for(T t : list){
				if(t.pid() == id){
					rs.add(t);
				}
			}
		}
		return rs;
	}
	
	/**
	 * 获取所有次级几点
	 * @param list
	 * @param id
	 * @return
	 */
	public static <T extends ITreeNode> List<T> getAllSubNodes(List<T> list, Long id){
		if(id == null || id == 0L)
			return list;
		
		List<T> rs = new ArrayList<T>();
		List<T> sub = getSubNodes(list, id);
		rs.addAll(sub);
		if(sub != null && sub.size() > 0){
			for(T t : sub){
				rs.addAll(getAllSubNodes(list, t.id()));
			}
		}
		return rs;
	}
	
	/**
	 * 节点是否是叶子节点
	 * @param list
	 * @param id
	 * @return
	 */
	public static <T extends ITreeNode> boolean isLeaf(List<T> list, Long id){
		if(list != null && list.size() > 0){
			for(T t : list){
				if(t.pid() == id){
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 获取节点
	 * @param list
	 * @param id
	 * @return
	 */
	public static <T extends ITreeNode>T getNode(List<T> list, Long id){
		if(list == null || list.size() < 1)
			return null;
		for(T t : list){
			if(t.id() == id){
				return t;
			}
		}
		return null;
	}
	
	/**
	 * 获取所有叶子节点
	 * @param list
	 * @return
	 */
	public static <T extends ITreeNode>List<T> getAllLeaf(List<T> list){
		List<T> rs = new ArrayList<T>();
		for(T t : list){
			if(isLeaf(list, t.id())){
				rs.add(t);
			}
		}
		return rs;
	}
	
	/**
	 * 递归获取所有子节点
	 * @param list
	 * @param id
	 * @return
	 */
	public static <T extends ITreeNode>List<T> getAllSubLeaf(List<T> list, Long id){
		if(id == null || id == 0L){
			return getAllLeaf(list);
		}
		return getAllLeaf(getAllSubNodes(list, id));
	}
	
//	public static <T extends ITreeNode>List<T> getAllSubLeaf(List<T> list, List<T> leafList){
//		if(leafList == null || leafList.size() < 1){
//			return null;
//		}
//		return getAllLeaf(getAllSubNodes(list, id));
//	}
}

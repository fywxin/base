package org.whale.system.common.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.core.OrderComparator;
import org.springframework.core.Ordered;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class TreeUtil {
	
	/**
	 * jqGrid树列表转换方法
	 * @param list
	 * @param rootId
	 * @return
	 */
	public static <T extends TreeNode> JSONObject jqGridTree(List<T> list, Object rootId){
		JSONObject obj = new JSONObject();
		if(list == null || list.size() < 1){
			obj.put("rows", new JSONArray());
		}else{
			obj.put("rows", parseJqGridTreeNodes(list, rootId));
		}
		return obj;
	}
	
	private static <T extends TreeNode> List<Map<String, Object>> parseJqGridTreeNodes(List<T> list, Object rootId){
		List<Map<String, Object>> rs = new ArrayList<Map<String,Object>>(list.size());
		//父Id，子列表
		Map<Object, List<Map<String, Object>>> pMap = new HashMap<Object, List<Map<String,Object>>>();
		//Id, 节点树对象
		Map<Object, Map<String, Object>> idMap = new HashMap<Object, Map<String, Object>>();
		
		//转换Temp
		List<Map<String, Object>> tmpList = null;
		Map<String, Object> tmp = null;
		Boolean hasOrderCol = null;
		for(int i=0; i<list.size(); i++){//pMap、idMap构造
			if(hasOrderCol == null){
				if(list.get(0) instanceof Ordered){
					hasOrderCol = true;
				}else{
					hasOrderCol = false;
				}
			}
			T obj = list.get(i);
			tmp = obj.asMap();
			tmp.put("name", obj.name());
			tmp.put("id", obj.id());
			tmp.put("pid", obj.pid());
			if(hasOrderCol.booleanValue()){
				Ordered ordered = (Ordered)list.get(i);
				tmp.put("order", ordered.getOrder());
			}
			
			idMap.put(obj.id(), tmp);
			
			tmpList = pMap.get(obj.pid());
			if(tmpList == null){
				tmpList = new ArrayList<Map<String,Object>>();
				pMap.put(obj.pid(), tmpList);
			}
			tmpList.add(tmp);
		}
		
		if(hasOrderCol.booleanValue()){
			for(List<Map<String, Object>> chls : pMap.values()){
				Collections.sort(chls, new OrderComparator());
			}
		}
		
		if(rootId == null){
			rootId = 0L;
		}
		//获取根节点集合
		List<Map<String, Object>> rootList = pMap.get(rootId);
		if(rootList == null){
			//获取所有的子节点集合
			Set<Object> childIds = new HashSet<Object>();
			for(List<Map<String, Object>> chls : pMap.values()){
				for(Map<String, Object> map : chls){
					childIds.add(map.get("id"));
				}
			}
			//不在子节点集合里面的都是根节点
			rootList = new ArrayList<Map<String,Object>>(list.size()-childIds.size());
			for(T obj : list){
				if(!childIds.contains(obj.id())){
					rootList.add(idMap.get(obj.id()));
				}
			}
		}
		
		//按根节点循环获取所有的树结构
		int num=0;
		for(Map<String, Object> root : rootList){//从根节点循环获取树结构对象类别
			num = loop(rs, root, pMap, idMap, num, 0);
		}
		
		return rs;
	}
	
	/**
	 * 构造树结构 lft rgt level
	 * @param rs  返回结果集合
	 * @param node 当前节点
	 * @param pMap 父Id，子列表
	 * @param idMap Id, 节点树对象
	 * @param index 当前index值
	 * @param level 
	 * @return
	 */
	private static Integer loop(List<Map<String, Object>> rs, Map<String, Object> node, Map<Object, List<Map<String, Object>>> pMap, Map<Object, Map<String, Object>> idMap, Integer index, Integer level){
		//进入节点前，先+1，做为上个节点的次节点
		int lft = index+1;
		rs.add(node);
		node.put("level", level);
		node.put("lft", lft);
		
		List<Map<String, Object>> subNodes = pMap.get(node.get("id"));
		if(subNodes != null && subNodes.size() > 0){
			node.put("uiicon", "ui-icon-image");
			node.put("expanded", true);
			level++;
			int num = lft;
			for(Map<String, Object> sub : subNodes){
				num = loop(rs, sub, pMap, idMap, num, level);
			}
			lft = num+1;
		}else{
			node.put("expanded", false);
			node.put("uiicon", "ui-icon-document");
			lft = lft+1;
		}
		node.put("rgt", lft);
		return lft;
	}
	

	/**
	 * 返回子树结构
	 * @param list
	 * @param rootId
	 * @return
	 */
	public static <T extends TreeNode> Tree<T> parseTree(List<T> list, Object rootId){
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
	public static <T extends TreeNode> Tree<T> parseTree(List<T> list, T root){
		
		return parseTree(list, root, null);
	}
	
	/**
	 * 创建树结构
	 * @param list
	 * @param node
	 * @param parent
	 * @return
	 */
	public static <T extends TreeNode> Tree<T> parseTree(List<T> list, T node, Tree<T> parent){
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
	public static <T extends TreeNode> List<T> getSubNodes(List<T> list, Object id){
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
	public static <T extends TreeNode> List<T> getAllSubNodes(List<T> list, Object id){
		if(id == null)
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
	public static <T extends TreeNode> boolean isLeaf(List<T> list, Object id){
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
	public static <T extends TreeNode>T getNode(List<T> list, Object id){
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
	public static <T extends TreeNode>List<T> getAllLeaf(List<T> list){
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
	public static <T extends TreeNode>List<T> getAllSubLeaf(List<T> list, Long id){
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

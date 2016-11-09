package org.whale.system.jdbc.readwrite.router;

import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.sql.DataSource;

import org.whale.system.spring.SpringContextHolder;

/**
 * 随机，按权重设置随机概率。
 * 在一个截面上碰撞的概率高，但调用量越大分布越均匀，而且按概率使用权重后也比较均匀，有利于动态调整提供者权重。
 * 
 * @author 王金绍
 *
 */
public class WeightRouter extends AbstractRouter {
	
	private Map<String, Integer> weightMap;
	
	private RoundRobinRouter pollRouter;
	
	private Object lock = new Object();
	
	private final Random random = new Random();
	
	private boolean same = false;

	public Map<String, Integer> getWeightMap() {
		return weightMap;
	}

	public void setWeightMap(Map<String, Integer> weightMap) {
		this.weightMap = weightMap;
	}

	@Override
	public String doSelect(List<DataSource> readDsList, List<String> readDsKeyList) {
		if(weightMap == null || weightMap.size() < 1 || same){
			return this.pollSelect(readDsList, readDsKeyList);
		}
		
		int length = readDsList.size(); // 总个数
        int totalWeight = 0; // 总权重
        boolean sameWeight = true; // 权重是否都一样
        for (int i = 0; i < length; i++) {
            int weight = weightMap.get(readDsKeyList.get(i));
            totalWeight += weight; // 累计总权重
            if (sameWeight && i > 0 && weight !=  weightMap.get(readDsKeyList.get(i-1))) {
                sameWeight = false; // 计算所有权重是否一样
            }
        }
        same = sameWeight;
        
        if (totalWeight > 0 && ! sameWeight) {
            // 如果权重不相同且权重大于0则按总权重数随机
            int offset = random.nextInt(totalWeight);
            // 并确定随机值落在哪个片断上
            for (int i = 0; i < length; i++) {
                offset -= weightMap.get(readDsKeyList.get(i));
                if (offset < 0) {
                    return readDsKeyList.get(i);
                }
            }
        }
		
        //相同，使用轮询
        return this.pollSelect(readDsList, readDsKeyList);
	}
	
	private String pollSelect(List<DataSource> readDsList, List<String> readDsKeyList){
		if(pollRouter == null){
			synchronized (lock) {
				if(pollRouter == null){
					try{
						pollRouter = SpringContextHolder.getBean(RoundRobinRouter.class);
					}catch(Exception e){
						pollRouter = new RoundRobinRouter();
					}
				}
			}
		}
		return pollRouter.route(readDsList, readDsKeyList);
	}
}

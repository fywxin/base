package org.whale.ext.mongo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.whale.system.base.Page;
import org.whale.system.common.util.Strings;
import org.whale.system.common.util.TimeUtil;
import org.whale.system.domain.Log;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;

/**
 * Morphia ORM封装
 * 
 * MongoDB会消耗太多的磁盘空间了。当然了，这与它的编码方式有关，因为MongoDB会通过预分配大文件空间来避免磁盘碎片问题。
 * 它的工作方式是这样的：在创建数据库时，系统会创建一个名为[db name].0的文件，当该文件有一半以上被使用时，系统会再次创建一个名为[db name].1的文件，该文件的大小是方才的两倍。
 * 这个情况会持续不断的发生，因此256、512、1024、2048大小的文件会被写到磁盘上。最后，再次创建文件时大小都将为2048Mb。
 * 如果存储空间是项目的一个限制，那么你必须要考虑这个情况。该问题有个商业解决方案，名字叫做TokuMX，使用后存储消耗将会减少90%。
 * 此外，从长远来看，repairDatabase与compact命令也会在一定程度上帮到你。
 * 
 * @author 王金绍
 * @date 2015年5月13日 下午1:52:59
 */
@Repository
public class LogDao {

	@Autowired
	private MongoClient mongoClient;
	
	public void save(List<Log> logs){
		if(logs == null || logs.size() < 1)
			return ;
		
		MongoDatabase database = mongoClient.getDatabase("whale");
		
		List<Document> docs = new ArrayList<Document>();
		Document doc = null;
		for(Log log : logs){
			doc = new Document();
			doc.append("_id", log.getId())
				.append("appId", log.getAppId())
				.append("opt", log.getOpt())
				.append("cnName", log.getCnName())
				.append("tableName", log.getTableName())
				.append("uri", log.getUri())
				.append("sqlStr", log.getSqlStr())
				.append("argStr", log.getArgStr())
				.append("rsStr", log.getRsStr())
				.append("ip", log.getIp())
				.append("createTime", log.getCreateTime())
				.append("userName", log.getUserName())
				.append("callOrder", log.getCallOrder())
				.append("methodCostTime", log.getMethodCostTime())
				.append("costTime", log.getCostTime())
				.append("rsType", log.getRsType());
			
			docs.add(doc);
		}
		
		
		database.getCollection("log").insertMany(docs);
	}
	
	
	public void queryPage(Page page){
		Document param = new Document();
		Pattern pattern = null;
		if(Strings.isNotBlank(page.getParamStr("opt"))){
			String opt = page.getParamStr("opt");
			if("dll".equals(opt)){
				param.put("$or", Arrays.asList(new Document[]{new Document("opt", Pattern.compile("^save")),new Document("opt",  Pattern.compile("^update")), new Document("opt",  Pattern.compile("^delete"))}));
			}else if("find".equals(opt)){
				param.put("$or", Arrays.asList(new Document[]{new Document("opt", Pattern.compile("^get")), new Document("opt", Pattern.compile("^query"))}));
			}else{
				param.put("opt", opt);
			}
		}
		if(Strings.isNotBlank(page.getParamStr("tableName"))){
			pattern = Pattern.compile(page.getParamStr("tableName"), Pattern.CASE_INSENSITIVE);  
			param.put("tableName", pattern);
		}
		if(Strings.isNotBlank(page.getParamStr("uri"))){
			pattern = Pattern.compile(page.getParamStr("uri"), Pattern.CASE_INSENSITIVE);  
			param.put("uri", pattern);
		}
		if(Strings.isNotBlank(page.getParamStr("userName"))){
			pattern = Pattern.compile(page.getParamStr("userName"), Pattern.CASE_INSENSITIVE);  
			param.put("userName", pattern);
		}
		if(page.getParamInteger("rsType") != null){
			param.put("rsType", page.getParamInteger("rsType"));
		}
		if(Strings.isNotBlank(page.getParamStr("startTime"))){
			long stime = TimeUtil.parseTime(page.getParamStr("startTime"), TimeUtil.COMMON_FORMAT).getTime();
			param.put("createTime", new Document("$gte", stime));
		}
		
		if(Strings.isNotBlank(page.getParamStr("endTime"))){
			long etime = TimeUtil.parseTime(page.getParamStr("endTime"), TimeUtil.COMMON_FORMAT).getTime();
			param.put("createTime", new Document("$lte", etime));
		}
		if(Strings.isNotBlank(page.getParamStr("appId"))){
			param.put("appId", Integer.parseInt(page.getParamStr("appId")));
		}
		if(Strings.isNotBlank(page.getParamInteger("methodCostTime"))){
			param.put("methodCostTime", new Document("$gte", page.getParamInteger("methodCostTime")));
		}
		if(Strings.isNotBlank(page.getParamInteger("costTime"))){
			param.put("costTime", new Document("$gte", page.getParamInteger("costTime")));
		}
		
		MongoDatabase database = mongoClient.getDatabase("whale");
		page.setTotal(database.getCollection("log").count(param));
		
		FindIterable<Document> iterable = database.getCollection("log").find(param).sort(new Document("createTime",1)).skip((page.getPageNo()-1)*page.getPageSize()).limit(page.getPageSize());
		
		final List<Log> list = new ArrayList<Log>(page.getPageSize());
		
		iterable.forEach(new Block<Document>() {
		    @Override
		    public void apply(final Document doc) {
		    	Log log = new Log();
		    	log.setId(doc.getString("_id"));
		    	log.setAppId(doc.getString("appId"));
		    	log.setCallOrder(doc.getInteger("callOrder"));
		    	log.setCnName(doc.getString("cnName"));
		    	log.setCostTime(doc.getInteger("costTime"));
		    	log.setCreateTime(doc.getLong("createTime"));
		    	log.setArgStr(doc.getString("argStr"));
		    	log.setRsStr(doc.getString("rsStr"));
		    	log.setIp(doc.getString("ip"));
		    	log.setMethodCostTime(doc.getInteger("methodCostTime"));
		    	log.setOpt(doc.getString("opt"));
		    	log.setRsType(doc.getInteger("rsType"));
		    	log.setTableName(doc.getString("tableName"));
		    	log.setUri(doc.getString("uri"));
		    	log.setUserName(doc.getString("userName"));
		    	log.setSqlStr(doc.getString("sqlStr"));
		    	
		    	list.add(log);
		    }
		});
		
		page.setDatas(list);
	}
	
	public Log get(String objId){
		MongoDatabase database = mongoClient.getDatabase("whale");
		Document doc = database.getCollection("log").find(new Document("_id", objId)).first();
		Log log = null;
		if(doc != null){
			log = new Log();
			log.setId(doc.getString("_id"));
	    	log.setAppId(doc.getString("appId"));
	    	log.setCallOrder(doc.getInteger("callOrder"));
	    	log.setCnName(doc.getString("cnName"));
	    	log.setCostTime(doc.getInteger("costTime"));
	    	log.setCreateTime(doc.getLong("createTime"));
	    	log.setArgStr(doc.getString("argStr"));
	    	log.setRsStr(doc.getString("rsStr"));
	    	log.setIp(doc.getString("ip"));
	    	log.setMethodCostTime(doc.getInteger("methodCostTime"));
	    	log.setOpt(doc.getString("opt"));
	    	log.setRsType(doc.getInteger("rsType"));
	    	log.setTableName(doc.getString("tableName"));
	    	log.setUri(doc.getString("uri"));
	    	log.setUserName(doc.getString("userName"));
	    	log.setSqlStr(doc.getString("sqlStr"));
		}
		
		return log;
	}
}

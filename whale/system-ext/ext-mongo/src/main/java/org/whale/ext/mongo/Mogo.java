package org.whale.ext.mongo;

import java.util.List;

import org.whale.system.common.util.LangUtil;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

public class Mogo {

	private MongoClient mongoClient;

	public static ServerAddress NEW_SA(String host, int port) {
		
		return new ServerAddress(host, port <= 0 ? ServerAddress.defaultPort(): port);
	}

	public static Mogo me() {
		return me("localhost", ServerAddress.defaultPort());
	}

	public static Mogo me(String host) {
		return me(host, ServerAddress.defaultPort());
	}

	public static Mogo me(String host, int port) {
		return me(NEW_SA(host, port), null);
	}

	public static Mogo me(ServerAddress sa, MongoCredential cred) {
		return me(sa, cred, null);
	}

	public static Mogo me(ServerAddress sa, MongoCredential cred,
			MongoClientOptions mopt) {
		return new Mogo(sa == null ? null : LangUtil.list(sa),
				cred == null ? null : LangUtil.list(cred), mopt);
	}

	public static Mogo me(List<ServerAddress> sas, List<MongoCredential> creds,
			MongoClientOptions mopt) {
		return new Mogo(sas, creds, mopt);
	}

	public static Mogo me(MongoClient mc) {
		return new Mogo(mc);
	}

	public static Mogo me(String userName, String password, String host, int port) {
		return me(userName, password, host, port, null);
	}

	public static Mogo me(String userName, String password, String host, int port, String database) {
		return me(NEW_SA(host, port), MongoCredential.createMongoCRCredential(userName, database, password.toCharArray()));
	}

	public Mogo(MongoClient mongoClient) {
		this.mongoClient = mongoClient;
	}

	private Mogo(List<ServerAddress> sas, List<MongoCredential> creds,
			MongoClientOptions mopt) {
		// 确保默认配置
		if (null == mopt)
			mopt = new MongoClientOptions.Builder().build();
		this.mongoClient = new MongoClient(sas, creds, mopt);
	}

	/**
	 * 获取数据库访问对象
	 * 
	 * @param dbname
	 *            数据库名称
	 * @return 数据库封装对象
	 */
	public MongoDatabase getDb(String dbname) {
		return mongoClient.getDatabase(dbname);
	}

	/**
	 * @return 当前服务器的数据库名称列表
	 */
	public MongoIterable<String> dbnames() {
		return mongoClient.listDatabaseNames();
	}
	

}

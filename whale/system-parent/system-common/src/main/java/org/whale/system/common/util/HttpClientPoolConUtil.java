package org.whale.system.common.util;

import org.apache.http.client.HttpClient;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientPoolConUtil {

	private static final Logger logger = LoggerFactory.getLogger("httpClient");

	private PoolingClientConnectionManager cm;

	private DefaultHttpClient httpclient;

	//连接超时
	private int connectionTimeout = 60000*60;

	//等待数据超时时间
	private int readTimeout = 60000*60;

	//设置整个连接池最大连接数 
	private int threadMaxTotal = 16;

	//每个路由最大连接数 ： 即连接到每个域名的最大连接数  ： 因为只有一个路由，所以让他等于最大值
	private int maxPerRoute = 16;
	
	//连接不够用的时候等待超时时间
	private Long connManagerTimeout = 1000L;

	/** Whether or not methods that have successfully sent their request will be retried */
	private boolean requestSentRetryEnabled = true;

	// 失败重复次数
	private int retryCount = 2;

	public HttpClientPoolConUtil() {
		//参数设置工具类：@HttpConnectionParams
		HttpParams httpParams = new BasicHttpParams();
		httpParams.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, connectionTimeout);
		httpParams.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, readTimeout);
		httpParams.setLongParameter(ClientPNames.CONN_MANAGER_TIMEOUT, connManagerTimeout);

		cm = new PoolingClientConnectionManager();
		cm.setMaxTotal(threadMaxTotal);
		cm.setDefaultMaxPerRoute(maxPerRoute);//route最大连接数

		httpclient = new DefaultHttpClient(cm, httpParams);
		httpclient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(retryCount, requestSentRetryEnabled));
		logger.info("httpClient 启动设置成功： "+this);
	}

	public void destroy() {
		if (httpclient != null) {
			httpclient.getConnectionManager().shutdown();
		}
	}


	public HttpClient getHttpclient() {
		return httpclient;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public void setThreadMaxTotal(int threadMaxTotal) {
		this.threadMaxTotal = threadMaxTotal;
	}

	public void setMaxPerRoute(int maxPerRoute) {
		this.maxPerRoute = maxPerRoute;
	}

	public void setRequestSentRetryEnabled(boolean requestSentRetryEnabled) {
		this.requestSentRetryEnabled = requestSentRetryEnabled;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public void setConnManagerTimeout(Long connManagerTimeout) {
		this.connManagerTimeout = connManagerTimeout;
	}

	@Override
	public String toString() {
		return "ClientMultiThreadedExecution [connectionTimeout="
				+ connectionTimeout + ", readTimeout=" + readTimeout
				+ ", threadMaxTotal=" + threadMaxTotal + ", maxPerRoute="
				+ maxPerRoute + ", connManagerTimeout=" + connManagerTimeout
				+ ", requestSentRetryEnabled=" + requestSentRetryEnabled
				+ ", retryCount=" + retryCount + "]";
	}

	
}

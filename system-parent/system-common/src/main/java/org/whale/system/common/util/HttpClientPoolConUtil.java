package org.whale.system.common.util;

import org.apache.http.client.HttpClient;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.CloseableHttpClient;
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

	private CloseableHttpClient httpclient;
	// 连接超时
	private int connectionTimeout = 60 * 1000;
	// 读超时
	private int readTimeout = 60 * 1000 * 2;
	// 连接池最大连接数
	private int threadMaxTotal = 64;
	// 每个路由最大连接数
	private int maxPerRoute = 64;
	//连接不够用时等待超时时间，如未设置此参数，则气质默认等于 connectionTimeout， 此值一定要设置，且不能太大
	private long connManagerTimeout = 1000L;

	// 关闭重试
	private boolean requestSentRetryEnabled = false;
	private int retryCount = 0;

	public HttpClientPoolConUtil() {
		//参数设置工具类：@HttpConnectionParams
		HttpParams httpParams = new BasicHttpParams();
		httpParams.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, connectionTimeout);
		httpParams.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, readTimeout);
		httpParams.setLongParameter(ClientPNames.CONN_MANAGER_TIMEOUT, connManagerTimeout);

		cm = new PoolingClientConnectionManager();
		cm.setMaxTotal(threadMaxTotal);
		cm.setDefaultMaxPerRoute(maxPerRoute);//route最大连接数

		httpclient = new DefaultHttpClient(cm, httpParams);;
		logger.info("httpClient 启动设置成功： {}", this);
	}

	public void destroy() {
		if (httpclient != null) {
			httpclient.getConnectionManager().shutdown();
		}
	}

	public CloseableHttpClient getHttpclient() {
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

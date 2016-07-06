package org.whale.system.common.util;

import org.apache.http.client.HttpClient;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * Created by 王金绍 on 2016/7/6.
 */
public class HttpPoolUtil {

    private final PoolingHttpClientConnectionManager mgr;
    private final CloseableHttpClient httpclient;

    public HttpPoolUtil() {
        super();
        final ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setBufferSize(8 * 1024)
                .setFragmentSizeHint(8 * 1024)
                .build();
        final SocketConfig socketConfig = SocketConfig.custom()
                .setSoTimeout(15000)
                .build();
        this.mgr = new PoolingHttpClientConnectionManager();
        this.mgr.setDefaultSocketConfig(socketConfig);
        this.mgr.setDefaultConnectionConfig(connectionConfig);
        this.httpclient = HttpClients.createMinimal(this.mgr);
    }

    public CloseableHttpClient getHttpclient() {
        return httpclient;
    }
}

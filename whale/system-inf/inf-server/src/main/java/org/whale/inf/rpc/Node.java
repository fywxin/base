package org.whale.inf.rpc;

import java.util.Map;

/**
 * 
 * @author Administrator
 *
 */
public interface Node {

    Map<String, Object> getConf();
    
    boolean isAvailable();

    void destroy();
}

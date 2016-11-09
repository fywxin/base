package org.whale.system.jdbc.readwrite;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.whale.system.common.util.ThreadContext;

/**
 * 遇到事务，事务内无论什么方法，都强制走主库
 *
 *
 * Created by 王金绍 on 2016/11/8.
 */
public class FocusWriteTransaction {
    private static Logger logger = LoggerFactory.getLogger(FocusWriteTransaction.class);

    /**
     * 设置走主库标志
     * @param pjp
     * @throws Throwable
     */
    public void focusWrite(ProceedingJoinPoint pjp) throws Throwable {
        if (logger.isDebugEnabled()){
            logger.debug("dynamic ds for transaction write mode!");
        }
        ThreadContext.getContext().put(ThreadContext.DYNAMIC_DS_TRANS_FOCUS_WRITE_FLAG, true);
        try {
            pjp.proceed();
        }finally {
            if (logger.isDebugEnabled()){
                logger.debug("dynamic ds for transaction REMOVE write Context!");
            }
            ThreadContext.getContext().remove(ThreadContext.DYNAMIC_DS_TRANS_FOCUS_WRITE_FLAG);
        }
    }
}
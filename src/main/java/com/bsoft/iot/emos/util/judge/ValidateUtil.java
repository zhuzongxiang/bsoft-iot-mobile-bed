package com.bsoft.iot.emos.util.judge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * @author zzx
 */
public class ValidateUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateUtil.class);

    /**
     * 验证传入的对象
     *
     * @param o       obj
     * @param message 异常信息
     * @param a       assert
     * @throws IllegalAccessException 异常
     */
    public static void execute(Object o, StringBuffer message, Assert a) throws IllegalAccessException {
        LOGGER.info("执行 ValidateUtil 的 execute");
        if (a == null) {
            throw new IllegalAccessException("assert is null");
        }
        if (o == null) {
            throw new IllegalAccessException("bean is null");
        }
        if (o instanceof Collection<?>) {
            if (CollectionUtils.isEmpty((Collection<?>) o)) {
                throw new IllegalAccessException("collection is null");
            }
        }
        try {
            LOGGER.info("执行 ValidateUtil 的 execute, o is {}", o);
            a.validate(o, message);
        } catch (AssertException ae) {
            throw new IllegalAccessException(ae.getMessage());
        }
    }
}

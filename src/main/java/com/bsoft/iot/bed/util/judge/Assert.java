package com.bsoft.iot.bed.util.judge;

/**
 * @author zzx
 */
public interface Assert {

    /**
     * 验证字段
     *
     * @param o       bean
     * @param message 异常信息
     * @throws AssertException 异常
     */
    void validate(Object o, StringBuffer message) throws AssertException;
}

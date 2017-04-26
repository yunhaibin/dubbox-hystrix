package com.alibaba.dubbo.circuitbreak;

import com.alibaba.dubbo.rpc.Result;

/**
 * Dubbo circuit breaker
 *
 * @author Jerry nickid@qq.com
 * @create 2017-04-25 11:55
 **/
public interface CircuitBreaker {
    /**
     * execute circuit breaker
     * @return com.alibaba.dubbo.rpc.Result
     */
    Result circuitBreak();
}

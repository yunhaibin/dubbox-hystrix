package com.alibaba.dubbo.circuitbreak;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Adaptive;
import com.alibaba.dubbo.common.extension.SPI;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;

/**
 * Circuit breaker factory
 *
 * @author Jerry nickid@qq.com
 * @create 2017-04-25 11:57
 **/
@SPI("hystrix")
public interface CircuitBreakerFactory {
    /**
     * CircuitBreaker factory
     * @return com.alibaba.dubbo.hystrix.CircuitBreaker
     */
    @Adaptive(Constants.CIRCUIT_BREAKER_KEY)
    CircuitBreaker getCircuitBreaker(Invoker<?> invoker, Invocation invocation);
}

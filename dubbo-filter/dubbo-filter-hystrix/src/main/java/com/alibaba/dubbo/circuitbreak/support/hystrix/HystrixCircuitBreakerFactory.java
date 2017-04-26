package com.alibaba.dubbo.circuitbreak.support.hystrix;

import com.alibaba.dubbo.circuitbreak.CircuitBreaker;
import com.alibaba.dubbo.circuitbreak.support.AbstractCircuitBreakerFactory;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;

/**
 * @author Jerry nickid@qq.com
 * @create 2017-04-25 13:25
 **/
public class HystrixCircuitBreakerFactory extends AbstractCircuitBreakerFactory {
    /**
     * @param invoker
     * @param invocation
     * @return com.alibaba.dubbo.circuitbreak.CircuitBreaker
     */
    @Override
    protected CircuitBreaker createCircuitBreaker(Invoker<?> invoker, Invocation invocation) {
        return new HystrixCircuitBreaker(invoker, invocation);
    }
}

package com.alibaba.dubbo.circuitbreak.support;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.circuitbreak.CircuitBreaker;
import com.alibaba.dubbo.circuitbreak.CircuitBreakerFactory;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Abstract circuit breaker factory
 *
 * @author Jerry nickid@qq.com
 * @create 2017-04-25 12:04
 **/
public abstract class AbstractCircuitBreakerFactory implements CircuitBreakerFactory {
    /**
     * Hystrix 断路器由于自身有对象缓存机制，因此不使用以下注释的对象缓存机制（Hystrix会报错提示需要创建新的对象），
     * 如果使用其他断路方案可以使用以下缓存机制提高效率
     */
    //    private final ConcurrentMap<String, CircuitBreaker> circuitBreakers = new ConcurrentHashMap<String, CircuitBreaker>();
//
//    public CircuitBreaker getCircuitBreaker(Invoker<?> invoker, Invocation invocation) {
//        URL url = invoker.getUrl().addParameter(Constants.METHOD_KEY, invocation.getMethodName());
//        String key = url.toFullString();
//        CircuitBreaker circuitBreaker = circuitBreakers.get(key);
//        if (circuitBreaker == null) {
//            circuitBreakers.put(key, createCircuitBreaker(invoker, invocation));
//            circuitBreaker = circuitBreakers.get(key);
//        }
//        return circuitBreaker;
//    }
    @Override
    public CircuitBreaker getCircuitBreaker(Invoker<?> invoker, Invocation invocation) {
        return createCircuitBreaker(invoker, invocation);
    }

    protected abstract CircuitBreaker createCircuitBreaker(Invoker<?> invoker, Invocation invocation);
}

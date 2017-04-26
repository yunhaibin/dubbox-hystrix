package com.alibaba.dubbo.circuitbreak.support.hystrix;

import com.alibaba.dubbo.circuitbreak.CircuitBreaker;
import com.alibaba.dubbo.rpc.*;
import com.netflix.hystrix.*;

/**
 * @author Jerry nickid@qq.com
 * @create 2017-04-25 13:26
 **/
public class HystrixCircuitBreaker extends HystrixCommand<Result> implements CircuitBreaker {

    private static final int DEFAULT_THREADPOOL_CORE_SIZE = 10;
    private Invoker<?> invoker;
    private Invocation invocation;

    /**
     * 断路器配置未来可优化为动态配置，策略不写死在代码中
     * @param invoker
     * @param invocation
     */
    public HystrixCircuitBreaker(Invoker<?> invoker, Invocation invocation) {
        // 用Dubbo服务提供者接口名来定义断路器分组key
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(invoker.getInterface().getName()))
                // 用Dubbo服务提供者接口的方法名+方法入参个数定义相同依赖的key
                .andCommandKey(HystrixCommandKey.Factory.asKey(String.format("%s_%d", invocation.getMethodName(),
                        invocation.getArguments() == null ? 0 : invocation.getArguments().length)))
                // 重写断路器基本策略属性
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        // 断路器是否开启，默认：true
                        .withCircuitBreakerEnabled(true)
                        // 统计时间滚动窗口，以毫秒为单位，默认：10秒
                        .withMetricsRollingStatisticalWindowInMilliseconds(10000)
                        // 断路器在整个统计时间内是否开启的阀值，也就是10秒钟内至少请求10次，断路器才发挥起作用
                        .withCircuitBreakerRequestVolumeThreshold(10)
                        // 断路器默认工作时间，默认:5秒，断路器中断请求5秒后会进入半打开状态，放部分流量过去重试
                        .withCircuitBreakerSleepWindowInMilliseconds(5000)
                        // 当出错率超过30%后断路器启动，默认:50%
                        .withCircuitBreakerErrorThresholdPercentage(30)
                        // 是否开启Hystrix超时机制，这里禁用Hystrix的超时，使用dubbo的超时
                        .withExecutionTimeoutEnabled(false))
                // 线程池配置，可考虑获取Dubbo提供者的线程数来配置断路器的线程数，若使用Hystrix的线程数则应大于Dubbo服务提供者的线程数，保证管道匹配
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(DEFAULT_THREADPOOL_CORE_SIZE)));

        this.invoker = invoker;
        this.invocation = invocation;
    }

    /**
     * 根据需要重写失败回退逻辑（降级逻辑）
     * @return
     */
    @Override
    protected Result getFallback() {
        Throwable throwable = new RpcException("Hystrix fallback");
        Result result = new RpcResult(throwable);
        return result;
    }

    @Override
    public Result circuitBreak() {
        return super.execute();
    }

    @Override
    protected Result run() throws Exception {
        return invoker.invoke(invocation);
    }
}

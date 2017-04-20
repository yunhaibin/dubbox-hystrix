package com.alibaba.dubbo.hystrix.support;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.netflix.hystrix.*;

/**
 * dubbo hystrix simple controller
 *
 * @author Jerry
 * @create 2017-04-19 14:49
 **/
public class DubboHystrix extends HystrixCommand<Result> {

    private static Logger logger                            = LoggerFactory.getLogger(DubboHystrix.class);
    private static final int DEFAULT_THREADPOOL_CORE_SIZE   = 100;
    private Invoker<?> invoker;
    private Invocation invocation;

    /**
     *
     */
    public DubboHystrix(Invoker<?> invoker, Invocation invocation) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(invoker.getInterface().getName()))
                .andCommandKey(HystrixCommandKey.Factory.asKey(String.format("%s_%d", invocation.getMethodName(),
                        invocation.getArguments() == null ? 0 : invocation.getArguments().length)))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                //10秒钟内至少30此请求失败，熔断器才发挥起作用
                .withCircuitBreakerRequestVolumeThreshold(30)
                //熔断器中断请求30秒后会进入半打开状态,放部分流量过去重试
                .withCircuitBreakerSleepWindowInMilliseconds(30000)
                //错误率达到60开启熔断保护
                .withCircuitBreakerErrorThresholdPercentage(60)
                //使用dubbo的超时，禁用Hystrix的超时
                .withExecutionTimeoutEnabled(false))
                //线程池配置
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(getThreadPoolCoreSize(invoker.getUrl()))));

        this.invoker = invoker;
        this.invocation = invocation;
    }

    private static int getThreadPoolCoreSize(URL url) {
        if (url != null) {
            int size = url.getParameter("ThreadPoolCoreSize", DEFAULT_THREADPOOL_CORE_SIZE);
            if (logger.isDebugEnabled()) {
                logger.debug("ThreadPoolCoreSize:" + size);
            }
            return size;
        }
        return DEFAULT_THREADPOOL_CORE_SIZE;
    }

    /**
     * @return com.alibaba.dubbo.rpc.Result
     * @throws Exception
     */
    protected Result run() throws Exception {
        return invoker.invoke(invocation);
    }
}

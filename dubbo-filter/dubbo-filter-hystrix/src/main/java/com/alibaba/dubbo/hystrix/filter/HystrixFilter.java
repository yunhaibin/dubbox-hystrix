package com.alibaba.dubbo.hystrix.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.hystrix.support.DubboHystrix;
import com.alibaba.dubbo.rpc.*;

@Activate(group = Constants.CONSUMER)
public class HystrixFilter implements Filter {

    /**
     * @param invoker    service
     * @param invocation invocation.
     * @return com.alibaba.dubbo.rpc.Result
     * @throws RpcException
     */
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        DubboHystrix hystrix = new DubboHystrix(invoker, invocation);
        return hystrix.execute();
    }

}

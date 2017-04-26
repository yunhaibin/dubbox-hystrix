Add hystrix circuit breaker for dubbo.

Consumer reference add circuitbreak label and value is hystrix.

Example:

<dubbo:reference id="userInfoService" interface="cn.nickid.dbapi.leancloud.service.UserInfoService" circuitbreak="hystrix" />
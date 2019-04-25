package cn.harmonycloud.datacenter.aspect;

import cn.harmonycloud.datacenter.dao.daoImpl.NodeDataDao;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(2)
public class LoggingAspect {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(public * cn.harmonycloud.datacenter.dao.*.*(..))")
    private void packageDaoPointcut(){};
    @Pointcut("execution(public * cn.harmonycloud.datacenter.repository.*.*(..))")
    private void packageRepositoryPointcut(){};

    @Around(value = "packageDaoPointcut()")
    public Object aroundPackageDaoMethod(ProceedingJoinPoint pjp){
        String methodName = pjp.getSignature().getName();
        try{
            LOGGER.info("Running method '" + methodName + "'");
            long startTime = System.currentTimeMillis();
            Object result = pjp.proceed();
            long endTime = System.currentTimeMillis();
            LOGGER.info("Completed running method '" + methodName + "' " + "in " + (endTime - startTime)+ " ms");
            return result;
        }catch (Throwable throwable) {
            LOGGER.error("Method '" + methodName + "'" + " occurs Exception " + throwable);
            throwable.printStackTrace();
        } finally {

        }
        return null;
    }

    @Around(value = "packageRepositoryPointcut()")
    public Object aroundPackageRepositoryMethod(ProceedingJoinPoint pjp) {
        String methodName = pjp.getSignature().getName();
        try{
            LOGGER.info("Running method '" + methodName + "'");
            long startTime = System.currentTimeMillis();
            Object result = pjp.proceed();
            long endTime = System.currentTimeMillis();
            LOGGER.info("Completed running method '" + methodName + "' " + "in " + (endTime - startTime)+ " ms");
            return result;
        }catch (Throwable throwable) {
            LOGGER.error("Method '" + methodName + "'" + " occurs Exception " + throwable);
            throwable.printStackTrace();
        } finally {

        }
        return null;
    }
}

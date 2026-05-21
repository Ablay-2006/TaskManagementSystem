package kz.ablaysharimov.taskmanagementsystem.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class AblaySharimovLoggingAspect {

    @Around("execution(* kz.ablaysharimov.taskmanagementsystem.controller..*(..))")
    public Object logControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();

        log.info("Entering method: {}.{} with arguments: {}", className, methodName, Arrays.toString(args));
        long startTime = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            log.info("Exiting method: {}.{} - Execution time: {} ms", className, methodName, (endTime - startTime));
            return result;
        } catch (Exception ex) {
            long endTime = System.currentTimeMillis();
            log.error("Error in method: {}.{} - Execution time: {} ms - Error: {}",
                    className, methodName, (endTime - startTime), ex.getMessage());
            throw ex;
        }
    }

    @Before("execution(* kz.ablaysharimov.taskmanagementsystem.service..*(..))")
    public void logServiceMethods(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();

        log.debug("Service call: {}.{} with params: {}", className, methodName, Arrays.toString(args));
    }

    @AfterThrowing(pointcut = "execution(* kz.ablaysharimov.taskmanagementsystem..*..*(..))", throwing = "ex")
    public void logExceptions(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        log.error("Exception in: {}.{} - Exception type: {} - Message: {}",
                className, methodName, ex.getClass().getSimpleName(), ex.getMessage(), ex);
    }
}


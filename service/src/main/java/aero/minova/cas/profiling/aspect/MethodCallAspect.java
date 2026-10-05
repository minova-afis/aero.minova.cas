package aero.minova.cas.profiling.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/*
 * --- ACTIVATE --- 
 *  
 * To activate MethodCallAspect both properties must be set:
 * 
 * - application.properties -> cas.aspect.MethodCallAspect=true
 * - application.properties -> logging.level.aero.minova.cas.profiling.aspect.MethodCallAspect=DEBUG
 * 
 */

@Slf4j
@Aspect
@Component
@ConditionalOnProperty(name = "cas.aspect.MethodCallAspect", havingValue = "true")
public class MethodCallAspect {

    @Before("execution(* aero.minova.cas.controller.*.*(..)) " +
            //" || execution(* aero.minova.cas.model.*.*(..)) " +
            //" || execution(public aero.minova.cas.service.*.*(..)) " +
            " || execution(* aero.minova.cas.sql.*.*(..)) ")
    public void methodCall(JoinPoint joinPoint) throws Throwable {
    	
    	//System.out.println("MethodCallAspect " + joinPoint + " - " + joinPoint.getArgs());
    	long threadId = Thread.currentThread().threadId();
        log.debug("MethodCallAspect --- {} --- {}", threadId, joinPoint);
    }

    /*
    @Before("    execution(* aero.minova.cas.api.domain.ValueJacksonSerializer.*(..)) " +
            " || execution(* aero.minova.cas.api.domain.ValueSerializer.*(..)) " +
            " || execution(* aero.minova.cas.api.domain.ValueDeserializer.*(..)) " 
    		)
    public void apiMethodCall(JoinPoint joinPoint) throws Throwable {
    	
    	//System.out.println("MethodCallAspect " + joinPoint + " - " + joinPoint.getArgs());
        log.debug("ApiMethodCallAspect {}", joinPoint);
    }
    */
}
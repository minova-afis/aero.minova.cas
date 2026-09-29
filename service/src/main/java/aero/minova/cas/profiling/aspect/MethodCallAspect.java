package aero.minova.cas.profiling.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
//@Aspect
//@Component
public class MethodCallAspect {

    @Before("execution(* aero.minova.cas.controller.*.*(..)) " +
            //" || execution(* aero.minova.cas.model.*.*(..)) " +
            //" || execution(public aero.minova.cas.service.*.*(..)) " +
            " || execution(* aero.minova.cas.sql.*.*(..)) ")
    public void methodCall(JoinPoint joinPoint) throws Throwable {
    	
    	//System.out.println("MethodCallAspect " + joinPoint + " - " + joinPoint.getArgs());
        log.debug("MethodCallAspect {}", joinPoint);
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
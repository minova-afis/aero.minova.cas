package aero.minova.cas.profiling.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import aero.minova.cas.profiling.model.TimingRequestCollector;
import lombok.RequiredArgsConstructor;

/*
Spring injects the request-scoped proxy here even though TimingAspect itself is a singleton
— that's exactly the point of @RequestScope + proxyMode
(which @RequestScope sets to TARGET_CLASS by default).
 */

@Aspect
@Component
@RequiredArgsConstructor
public class TimingRequestAspect {

    private final TimingRequestCollector timingRequestCollector;

    @Around("   execution(* aero.minova.cas.api.domain.ValueSerializer.*(..)) " +
    		"|| execution(* aero.minova.cas.api.domain.ValueJacksonSerializer.*(..)) " +
    		"|| execution(* aero.minova.cas.api.domain.ValueGsonSerializer.*(..)) ")
    public Object timeSerialization(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.nanoTime();
        try {
            return pjp.proceed();
        } finally {
        	double durationInMs = (System.nanoTime() - start) / 1_000_000.;
            timingRequestCollector.addSerializerForValueTiming(durationInMs);
        }
    }

    @Around("   execution(* aero.minova.cas.api.domain.ValueDeserializer.*(..)) " +
    		"|| execution(* aero.minova.cas.api.domain.ValueJacksonDeserializer.*(..)) " + 
    		"|| execution(* aero.minova.cas.api.domain.ValueGsonDeserializer.*(..)) ")
    public Object timeDeserialization(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.nanoTime();
        try {
            return pjp.proceed();
        } finally {
        	double durationInMs = (System.nanoTime() - start) / 1_000_000.;
            timingRequestCollector.addDeserializerForValueTiming(durationInMs);
        }
    }

}

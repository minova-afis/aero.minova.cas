package aero.minova.cas.profiling.filter;

import java.io.IOException;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/*
 * Zum aktivieren des PerformanceLoggingFilter, den entsprechenden Logger in den application.properties 
 * auf DEBUG stellen. 
 * 
 * Beispiel: logging.level.aero.minova.cas.profiling.filter.PerformanceLoggingFilter=DEBUG
 */
@Slf4j
@Component
@Order(-1100)
public class PerformanceLoggingFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
    	//System.out.println("PerformanceLoggingFilter is on: " + log.isDebugEnabled());
        return !log.isDebugEnabled();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain) throws ServletException, IOException {

    	long startTimeInNanos = System.nanoTime();
        long threadId = Thread.currentThread().threadId();

		log.debug("--- PerformanceLoggingFilter Start --- {} ---", threadId);
    	
    	try {
    		filterChain.doFilter(request, response);
    	} finally {    	
	    	long durationTimeInNanos =  System.nanoTime() - startTimeInNanos;
	    	long durationTimeInMs =  durationTimeInNanos/1_000_000;

	    	String headerProfiling = response.getHeader("X-Profiling-Time");
	    	String headerContentEncoding = response.getHeader("Content-Encoding") != null ? response.getHeader("Content-Encoding") : "";

	    	log.debug("--- PerformanceLoggingFilter End   --- {} --- (Duration Filter / Duration Controller): ({}ms / {}) {}", threadId, durationTimeInMs, headerProfiling, headerContentEncoding);
	    	
	    }
    }

}

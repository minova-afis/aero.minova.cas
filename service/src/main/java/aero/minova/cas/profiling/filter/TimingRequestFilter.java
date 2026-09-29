package aero.minova.cas.profiling.filter;

import java.io.IOException;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import aero.minova.cas.profiling.model.TimingRequestCollector;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
The subtlety worth knowing

Since a Filter is invoked by the servlet container before Spring's
RequestContextListener/DispatcherServlet fully establishes request scope in some setups,
directly injecting the request-scoped proxy into a Filter bean can occasionally throw
Scope 'request' is not active — depending on filter order and whether you're using
Spring's RequestContextFilter. Two ways to avoid that:

Simplest fix: make sure org.springframework.web.filter.RequestContextFilter
(or RequestContextListener in web.xml/ServletContextInitializer) runs before
your MetricsLoggingFilter in the filter chain. Since request scope is backed
by request attributes, and your own filter obviously is running inside an active
request, this is almost always already satisfied automatically for filters registered
as Spring beans — worth calling out but rarely bites you in practice.

Defensive alternative: inject ObjectProvider<RequestMetricsCollector> instead
of the bean directly, and resolve it with .getObject() inside doFilterInternal.
This defers the scope lookup until you're definitely inside the request,
sidestepping any bean-creation-order issues entirely.

I'd drop the direct injection in the example above and just keep the
 ObjectProvider variant — it's marginally more code but removes an entire class of startup-order bugs:
*/
@Slf4j
@Component
@RequiredArgsConstructor
//@Order(-1010) // Wrapped by: org.springframework.beans.factory.support.ScopeNotActiveException: Error creating bean with name 'scopedTarget.timingRequestCollector': Scope 'request' is not active for the current thread; consider defining a scoped proxy for this bean if you intend to refer to it from a singleton
public class TimingRequestFilter extends OncePerRequestFilter {

    private final ObjectProvider<TimingRequestCollector> timingRequestCollectorProvider;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
    	//System.out.println("TimingRequestFilter is on: " + log.isDebugEnabled());
        return !log.isDebugEnabled();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
    	
        TimingRequestCollector timingRequestCollector = timingRequestCollectorProvider.getObject();

        long startTimeInNanos = System.nanoTime();
        long threadId = Thread.currentThread().threadId();
		log.debug("--- TimingRequestFilter Start      --- {} --- ", threadId);

        try {
            filterChain.doFilter(request, response);
        } finally {
	    	long durationTimeInNanos =  System.nanoTime() - startTimeInNanos;
	    	long durationTimeInMs =  durationTimeInNanos/1_000_000;

            var serializerCount = timingRequestCollector.getSerializerForValueCount();
            var serializerDuration = timingRequestCollector.getSerializerForValueDuration();
            var deserializerCount = timingRequestCollector.getDeserializerForValueCount();
            var deserializerDuration = timingRequestCollector.getDeserializerForValueDuration();

	    	String headerProfiling = response.getHeader("X-Profiling-Time");
	    	String headerContentEncoding = response.getHeader("Content-Encoding") != null ? response.getHeader("Content-Encoding") : "";

            log.debug("--- TimingRequestFilter End        --- {} --- (Duration Filter / Duration Controller): ({}ms / {}) {} - [{} {}]: Serializer ({} / {}ms) - Deserializer ({} / {}ms)",
            		threadId,
            		durationTimeInMs,
            		headerProfiling,
            		headerContentEncoding,
            		request.getMethod(), 
                    request.getRequestURI(), 
                    serializerCount, 
                    serializerDuration,
                    deserializerCount,
                    deserializerDuration);
                        
        }
    }
}
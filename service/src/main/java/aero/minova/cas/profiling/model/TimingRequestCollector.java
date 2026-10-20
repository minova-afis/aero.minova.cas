package aero.minova.cas.profiling.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import lombok.Getter;

/*
For accumulating data across multiple aspects and reading it later in the filter,
the cleanest fit is a request-scoped bean. It gets a fresh instance per HTTP request,
Spring injects a proxy into your aspects and the filter so you never need to touch
the HttpServletRequest directly, and it's automatically cleaned up when the request ends
— no manual ThreadLocal management, no leak risk on exceptions.
*/

@Component
@RequestScope
public class TimingRequestCollector {
	
	@Getter
    private final List<TimingRecord> timingRecordList = new ArrayList<>();

    @Getter
    private int serializerForValueCount = 0;
    
    @Getter
    private double serializerForValueDuration = 0.0;

    @Getter
    private int deserializerForValueCount = 0;
    
    @Getter
    private double deserializerForValueDuration = 0.0;

    public void addTiming(String label, double durationInMs) {
        timingRecordList.add(new TimingRecord(label, durationInMs));
    }

    public void addSerializerForValueTiming(double duration) {
    	serializerForValueCount += 1;
    	serializerForValueDuration += duration;
    }

    public void addDeserializerForValueTiming(double duration) {
    	deserializerForValueCount += 1;
    	deserializerForValueDuration += duration;
    }

    // Record's 
    public record TimingRecord(String label, double durationInMs) {}
}

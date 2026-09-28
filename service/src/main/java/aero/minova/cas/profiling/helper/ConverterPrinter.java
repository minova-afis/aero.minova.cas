
package aero.minova.cas.profiling.helper;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ConverterPrinter implements ApplicationListener<ContextRefreshedEvent> {

    private final RequestMappingHandlerAdapter handlerAdapter;

    public ConverterPrinter(RequestMappingHandlerAdapter handlerAdapter) {
        this.handlerAdapter = handlerAdapter;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        handlerAdapter.getMessageConverters().forEach(c ->
        	log.debug("ConverterPrinter " +c.getClass().getSimpleName()
                + " → " + c.getSupportedMediaTypes())
        );
    }
}
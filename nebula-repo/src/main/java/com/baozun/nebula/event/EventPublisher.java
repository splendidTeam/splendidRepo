package com.baozun.nebula.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
@Service
@Scope("singleton")
public class EventPublisher {
    @Autowired
    private ApplicationContext context;
    
    public void publish(ApplicationEvent event) {
    	context.publishEvent(event);
    }
}

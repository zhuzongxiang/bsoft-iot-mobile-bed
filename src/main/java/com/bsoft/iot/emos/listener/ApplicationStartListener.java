package com.bsoft.iot.emos.listener;

import com.bsoft.iot.emos.dxiot.DxApi;
import com.bsoft.iot.emos.util.AccessTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author zzx
 */
@Component
public class ApplicationStartListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationStartListener.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        LOGGER.info("启动");
        AccessTokenUtil.init();
        DxApi.init();
    }
}

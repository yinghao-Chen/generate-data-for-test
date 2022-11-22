package cn.cyh.generatedata.factory;

import cn.cyh.generatedata.api.enums.DataSource;
import cn.cyh.generatedata.api.enums.DataSourceFunc;
import cn.cyh.generatedata.service.DataSourceStrategy;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.Map;

/**
 * @author cyh
 * @date 2022/11/22
 */
@Component
public class DataSourceStrategyFactory implements ApplicationContextAware {

    private ApplicationContext applicationContext;
    private static EnumMap<DataSource, DataSourceStrategy> HANDLER_MAP = new EnumMap<>(DataSource.class);


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void init() {
        Map<String, Object> map = applicationContext.getBeansWithAnnotation(DataSourceFunc.class);
        map.forEach((key, value) -> {
            DataSourceFunc func = value.getClass().getAnnotation(DataSourceFunc.class);
            HANDLER_MAP.put(func.dataSource(), (DataSourceStrategy) value);
        });
    }

    public static DataSourceStrategy getStrategy(DataSource type) {
        return HANDLER_MAP.get(type);
    }

}

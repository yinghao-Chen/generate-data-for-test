package cn.cyh.generatedata.strategy;

import cn.cyh.generatedata.api.enums.Rule;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.Map;

/**
 * @author cyh
 * @date 2022/11/15
 */
@Component
public class StrategyFactory implements ApplicationContextAware {

    private ApplicationContext applicationContext;
    private static EnumMap<Rule, Strategy> HANDLER_MAP = new EnumMap<>(Rule.class);


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void init() {
        Map<String, Object> map = applicationContext.getBeansWithAnnotation(RuleFunc.class);
        map.forEach((key, value) -> {
            RuleFunc ruleFunc = value.getClass().getAnnotation(RuleFunc.class);
            HANDLER_MAP.put(ruleFunc.rule(), (Strategy) value);
        });
    }

    public static Strategy getStrategy(Rule type) {
        return HANDLER_MAP.get(type);
    }

}

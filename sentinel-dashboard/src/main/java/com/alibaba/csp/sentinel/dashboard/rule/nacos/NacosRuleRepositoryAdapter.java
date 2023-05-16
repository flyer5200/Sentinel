package com.alibaba.csp.sentinel.dashboard.rule.nacos;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.RuleEntity;
import com.alibaba.csp.sentinel.dashboard.discovery.MachineInfo;
import com.alibaba.csp.sentinel.dashboard.repository.rule.RuleRepository;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public abstract class NacosRuleRepositoryAdapter<T extends RuleEntity> implements RuleRepository<T, Long> {
    private ConfigService configService;
    private Converter<String, List<T>> converter;

    @Override
    public T save(T entity) {
        if (entity.getId() == null) {
            entity.setId(nextId());
        }
        T processedEntity = preProcess(entity);
        if (processedEntity != null) {
            try {
                String ruleJson = configService.getConfig(entity.getApp() + NacosConfigUtil.DEGRADE_DATA_ID_POSTFIX, NacosConfigUtil.GROUP_ID, 3000);
                if (StringUtil.isNotBlank(ruleJson)){
                    List<T> rules = converter.convert(ruleJson);
                    rules.add(processedEntity);
                    configService.publishConfig(entity.getApp() + NacosConfigUtil.DEGRADE_DATA_ID_POSTFIX, NacosConfigUtil.GROUP_ID, JSON.toJSONString(rules));
                }
            }catch (Exception e){
                e.getMessage();
            }
        }
        return null;
    }

    @Override
    public List<T> saveAll(List<T> rules) {
        return null;
    }

    @Override
    public T delete(Long aLong) {
        return null;
    }

    @Override
    public T findById(Long aLong) {
        return null;
    }

    @Override
    public List<T> findAllByMachine(MachineInfo machineInfo) {
        return null;
    }

    @Override
    public List<T> findAllByApp(String appName) {
        return null;
    }

    protected T preProcess(T entity) {
        return entity;
    }

    /**
     * Get next unused id.
     *
     * @return next unused id
     */
    abstract protected long nextId();
}

package cn.cyh.generatedata.controller;

import cn.cyh.generatedata.api.common.Result;
import cn.cyh.generatedata.api.common.ResultCode;
import cn.cyh.generatedata.api.enums.DataSource;
import cn.cyh.generatedata.api.vo.GenToDataSourceSaveVO;
import cn.cyh.generatedata.api.vo.GenToDataSourceVO;
import cn.cyh.generatedata.factory.DataSourceStrategyFactory;
import cn.cyh.generatedata.service.DataSourceStrategy;
import cn.cyh.generatedata.utils.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cyh
 * @date 2022/11/22
 */
@Slf4j
@RestController
@RequestMapping("/toData")
public class GenToDataController {

    @PostMapping("/test")
    public Result test(@RequestBody GenToDataSourceVO vo) {
        if(!StringUtils.hasText(vo.getUrl()) || !StringUtils.hasText(vo.getUsername())) {
            return Result.fail(ResultCode.FAILURE, "缺少必要参数");
        }
        DataSourceStrategy strategy = getStrategy(vo.getUrl());
        if(strategy != null) {
            boolean test = strategy.test(vo);
            return Result.status(test);
        }
        return Result.fail("操作失败");
    }

    @PostMapping("/save")
    public Result save(@RequestBody GenToDataSourceSaveVO vo) {
        if(vo.getMeta() == null || vo.getRuleData() == null
                || !StringUtils.hasText(vo.getMeta().getUrl())
                || !StringUtils.hasText(vo.getMeta().getUsername())
                || !StringUtils.hasText(vo.getMeta().getPassword())
                || !StringUtils.hasText(vo.getMeta().getTable())
                || vo.getMeta().getCount() <= 0) {
            return Result.fail(ResultCode.FAILURE, "缺少必要参数");
        }
        DataSourceStrategy strategy = getStrategy(vo.getMeta().getUrl());
        if(strategy != null) {
            int cnt = strategy.saveData(vo);
            return Result.success(String.format("生成%s条数据", cnt));
        }
        return Result.fail("操作失败");
    }

    @PostMapping("/es/test")
    public Result esTest(@RequestBody GenToDataSourceVO vo) {
        if(!StringUtils.hasText(vo.getUrl())) {
            return Result.fail(ResultCode.FAILURE, "缺少必要参数");
        }
        DataSourceStrategy strategy = getStrategy(vo.getUrl());
        if(strategy != null) {
            boolean test = strategy.test(vo);
            return Result.status(test);
        }
        return Result.fail("操作失败");
    }

    @PostMapping("/es/save")
    public Result esSave(@RequestBody GenToDataSourceSaveVO vo) {
        if(vo.getMeta() == null || vo.getRuleData() == null
                || !StringUtils.hasText(vo.getMeta().getUrl())
                || !StringUtils.hasText(vo.getMeta().getTable())
                || !StringUtils.hasText(vo.getMeta().getType())
                || vo.getMeta().getCount() <= 0) {
            return Result.fail(ResultCode.FAILURE, "缺少必要参数");
        }
        DataSourceStrategy strategy = getStrategy(vo.getMeta().getUrl());
        if(strategy != null) {
            int cnt = strategy.saveData(vo);
            return Result.success(String.format("生成%s条数据", cnt));
        }
        return Result.fail("操作失败");
    }

    private DataSourceStrategy getStrategy(String url) {
        DataSourceStrategy strategy = null;
        if(url.contains(DataSource.MYSQL.getValue())) {
            strategy = DataSourceStrategyFactory.getStrategy(DataSource.MYSQL);
        } else if(IpUtil.validIp(url)) {
            strategy = DataSourceStrategyFactory.getStrategy(DataSource.ELASTIC_SEARCH);
        }
        return strategy;
    }

}

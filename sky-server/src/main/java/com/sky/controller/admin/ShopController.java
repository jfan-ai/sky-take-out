package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;



import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "店铺相关接口")
public class ShopController {
    public static final String KEY = "SHOP_STATUS";
    @Autowired
    @Qualifier("secondRedisTemplate")
    RedisTemplate redisTemplate;

    @PutMapping("/{status}")
    @ApiOperation("设置店铺的营业状态")
    public Result setStatus(@PathVariable Integer status) {
        log.info("设置店铺营业状态：{}", status == 1 ? "营业中" : "打烊中");
        redisTemplate.opsForValue().set(KEY, status);
        log.info("【写后】立即再读一次: {}", redisTemplate.opsForValue().get(KEY));
        return Result.success();
    }
    /**
     * 获取营业状态
     */
    @ApiOperation("获取营业状态")
    @GetMapping("/status")
    public Result<Integer> getStatus() {

        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("获取营业状态", status);
        return Result.success(status);
    }
}

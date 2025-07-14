package com.sky.controller.admin;
import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/admin/employee")
@Api(tags = "员工管理")
public class EmployeeController {
    /**
     * 要用到EmployeeService,jwtProperties
     */
    @Autowired
    EmployeeService employeeService;
    @Autowired
    JwtProperties jwtProperties;
    /**
     * 登录
     * @paramemp loginEmployeeLoginDTO
     * @return
     */
    @ApiOperation("员工登录")
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO){
        log.info("员工登录：{}",employeeLoginDTO);
        /**
         * 通过数据库查询拿到employee的信息
         */
        Employee employee = employeeService.login(employeeLoginDTO);
        /**
         * 登录成功后，生成jwt令牌
         * 需要用到的claims包含empid:id
         */
        Map<String,Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID,employee.getId());
        /**
         * 利用JwtUtil工具类生成jwt令牌
         */
        String token = JwtUtil.createJWT(jwtProperties.getAdminSecretKey(), jwtProperties.getAdminTtl(), claims);
        /**
         * 构造EmployeeLoginVO对象
         */
        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token( token)
                .build();
        return Result.success(employeeLoginVO);
    }
    @ApiOperation("员工注销")
    @PostMapping("/logout")
    public Result<String> logout(){
        return Result.success();
    }


    /**
     * 新增员工
     */
    @ApiOperation("新增员工")
    @PostMapping
    public Result save(@RequestBody EmployeeDTO employeeDTO){
        log.info("新增员工，员工数据：{}",employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }
    /**
     * 员工分页查询
     */
    @ApiOperation("员工分页查询")
    @GetMapping ("/page")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("员工分页查询，参数为：{}",employeePageQueryDTO);
        PageResult pageResult = employeeService.page(employeePageQueryDTO);
        return Result.success(pageResult);
    }
}
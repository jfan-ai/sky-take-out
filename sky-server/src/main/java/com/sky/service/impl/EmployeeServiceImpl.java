package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.EmployeeUpdatePasswordDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.LoginFailedException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {
    /**
     * 要用到employeeMapper
     */

    @Autowired
    EmployeeMapper employeeMapper;
    @Override
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {

        /**
         * 拿到username和 password
         */
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();
        /**
         * 根据username查询数据库返回employee实体
         */
        Employee employee = employeeMapper.getByUsername(username);
        /**
         * 若用户不存在 抛出异常messageConstant.ACCOUNT_NOT_FOUND
         */
        if(employee == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        /**
         * md5加密password
         * 密码比对
         * 若不对则抛出异常MessageConstant.PASSWORD_ERROR
         */
         password = DigestUtils.md5Hex(password.getBytes());
         log.info("加密后的密码为：{}",password);
        if(!password.equals(employee.getPassword())){
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }
        /**
         * 账号被锁定
         * 若被锁定则抛出异常MessageConstant.ACCOUNT_LOCKED
         */
        if(employee.getStatus() == 0){
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }
        return employee;
    }

    @Override
    public void save(EmployeeDTO employeeDTO) {
        /**
         * 创建一个employee实体 用来拷贝数据
         */
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);
        /**
         * 补充dto没有的属性 状态 密码 创建时间 修改时间 创建人id 更改人id
         */
        employee.setStatus(1);
        employee.setPassword(DigestUtils.md5Hex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setCreateUser(BaseContext.getCurrentId());
//        employee.setUpdateUser(BaseContext.getCurrentId());
        /**
         * 调用mapper插入
         */
        employeeMapper.insert(employee);
    }

    @Override
    public PageResult page(EmployeePageQueryDTO employeePageQueryDTO) {
        /**
         * 使用pageHelper分页插件 设置当前页码和每页显示条数
         */
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        /**
         * 调用mapper分页查询 返回Page<Employee>对象
         */
        Page<Employee> page = employeeMapper.page(employeePageQueryDTO);
        /**
         * 获取page的结果
         */
        long total = page.getTotal();
        List<Employee> list = page.getResult();
        return new PageResult(total, list);
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .build();
        employeeMapper.update(employee  );
    }

    @Override
    public void update(EmployeeDTO employeeDTO) {
        Long id = BaseContext.getCurrentId();
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);
//        employee.setUpdateUser( id);
//        employee.setUpdateTime(LocalDateTime.now());
        employeeMapper.update(employee);
    }

    @Override
    public Employee getById(Long id) {
        return employeeMapper.getById(id);
    }

    @Override
    public void editPassword(EmployeeUpdatePasswordDTO employeeUpdatePasswordDTO) {
        /**
         * 根据id查询员工信息
         * 判断原密码是否正确
         * 不正确抛出自定义异常
         * 修改密码
         */
        Employee employee = employeeMapper.getById(Long.valueOf(employeeUpdatePasswordDTO.getEmpId()));
        /**
         * 密码进行md5加密
         */
        String oldPassword = DigestUtils.md5Hex(employeeUpdatePasswordDTO.getOldPassword().getBytes());
        String newPassword = DigestUtils.md5Hex(employeeUpdatePasswordDTO.getNewPassword().getBytes());
        log.info("员工修改密码：{}", employeeUpdatePasswordDTO);
        log.info("员工修改旧密码：{}", oldPassword);
        if (!employee.getPassword().equals(oldPassword) ){
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }else{
            employee.setPassword(newPassword);
            employeeMapper.update(employee);
        }
    }


}
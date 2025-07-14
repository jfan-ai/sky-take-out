package com.sky.service.impl;

//import com.sky.constant.MessageConstant;
//import com.sky.constant.StatusConstant;
//import com.sky.dto.EmployeeLoginDTO;
//import com.sky.entity.Employee;
//import com.sky.exception.AccountLockedException;
//import com.sky.exception.AccountNotFoundException;
//import com.sky.exception.PasswordErrorException;
//import com.sky.mapper.EmployeeMapper;
//import com.sky.service.EmployeeService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.util.DigestUtils;
//
//@Service
//public class EmployeeServiceImpl implements EmployeeService {
//
//    @Autowired
//    private EmployeeMapper employeeMapper;
//
//    /**
//     * 员工登录
//     *
//     * @param employeeLoginDTO
//     * @return
//     */
//    @Override
//    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
//        String username = employeeLoginDTO.getUsername();
//        String password = employeeLoginDTO.getPassword();
//
//        //1、根据用户名查询数据库中的数据
//        Employee employee = employeeMapper.getByUsername(username);
//
//        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
//        if (employee == null) {
//            //账号不存在
//            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
//        }
//
//        //密码比对
//        // TODO 后期需要进行md5加密，然后再进行比对
//        if (!password.equals(employee.getPassword())) {
//            //密码错误
//            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
//        }
//
//        if (employee.getStatus() == StatusConstant.DISABLE) {
//            //账号被锁定
//            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
//        }
//
//        //3、返回实体对象
//        return employee;
//    }
//
//}

import com.sky.constant.MessageConstant;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.service.EmployeeService;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



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
}
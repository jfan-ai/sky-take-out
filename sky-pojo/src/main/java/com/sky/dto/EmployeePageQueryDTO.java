package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@ApiModel(description = "员工分页查询传递的数据模型")
@Data
public class EmployeePageQueryDTO implements Serializable {

    //员工姓名
    @ApiModelProperty("员工姓名")
    private String name;

    //页码
    @ApiModelProperty("页码")
    private int page;

    @ApiModelProperty("每页显示记录数")
    //每页显示记录数
    private int pageSize;

}

package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "员工密码修改传递的数据模型")
public class EmployeeUpdatePasswordDTO {
    @ApiModelProperty("员工id")
    private  Integer empId;
    @ApiModelProperty("原密码")
    private String oldPassword;
    @ApiModelProperty("新密码")
    private String newPassword;

}

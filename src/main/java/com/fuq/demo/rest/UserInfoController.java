package com.fuq.demo.rest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fuq.demo.service.system.UserInfoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Controller
@RequestMapping("/userInfo")
@Api(value = "用户信息操作接口Controller", tags = "用户信息操作接口")
public class UserInfoController {
	
	@Autowired
	private UserInfoService userInfoService;
    /**
     * 	用户查询.
     * @return
     */
    @RequiresPermissions("userInfo:view")//权限管理;
    @ApiOperation(value = "查询用户")
    @GetMapping("/userList")
    public String userInfo(){
        return "userInfo";
    }

    /**
     * 用户添加;
     * @return
     */
    @RequiresPermissions("userInfo:add")//权限管理;
    @ApiOperation(value = "添加用户")
    @PutMapping("/userAdd")
    public String userInfoAdd(){
        return "userInfoAdd";
    }

    /**
     * 用户删除;
     * @return
     */
    @RequiresPermissions("userInfo:del")//权限管理;
    @ApiOperation(value = "删除用户")
    @DeleteMapping("/userDel")
    public String userDel(){
        return "userInfoDel";
    }
}
package com.fuq.demo.mapper.system;

import org.springframework.data.repository.CrudRepository;

import com.fuq.demo.tool.dto.UserInfo;


public interface UserInfoDao extends CrudRepository<UserInfo,Long> {
    /**通过username查找用户信息;*/
    public UserInfo findByUsername(String username);
}
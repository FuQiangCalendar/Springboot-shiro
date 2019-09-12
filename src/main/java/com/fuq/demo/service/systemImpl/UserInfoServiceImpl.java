package com.fuq.demo.service.systemImpl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fuq.demo.dto.UserInfo;
import com.fuq.demo.mapper.system.UserInfoDao;
import com.fuq.demo.service.system.UserInfoService;

@Service
public class UserInfoServiceImpl implements UserInfoService {

	 @Resource
     private UserInfoDao userInfoDao;
     @Override
     public UserInfo findByUsername(String username) {
    	 System.out.println("UserInfoServiceImpl.findByUsername()");
    	 return userInfoDao.findByUsername(username);
     }

}

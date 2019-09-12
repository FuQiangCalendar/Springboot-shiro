package com.fuq.demo.service.system;

import com.fuq.demo.dto.UserInfo;

public interface UserInfoService {
	
	UserInfo findByUsername (String userName);
}

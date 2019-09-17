package com.fuq.demo.service.system;

import com.fuq.demo.tool.dto.UserInfo;

public interface UserInfoService {
	
	UserInfo findByUsername (String userName);
}

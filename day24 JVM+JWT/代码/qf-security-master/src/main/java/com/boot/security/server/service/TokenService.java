package com.boot.security.server.service;

import com.boot.security.server.dto.LoginUser;
import com.boot.security.server.dto.Token;

/**
 * Token管理器
 * 可存储到redis或者数据库
 */
public interface TokenService {

	Token saveToken(LoginUser loginUser);

	void refresh(LoginUser loginUser);

	LoginUser getLoginUser(String token);

	boolean deleteToken(String token);

}

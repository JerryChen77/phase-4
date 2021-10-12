package cn.exrick.service;


import cn.exrick.pojo.front.Member;

public interface LoginService {

    Member userLogin(String username, String password);

    Member getUserByToken(String token);

    int logout(String token);
}

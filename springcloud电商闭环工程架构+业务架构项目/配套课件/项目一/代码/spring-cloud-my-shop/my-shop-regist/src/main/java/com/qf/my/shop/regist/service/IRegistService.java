package com.qf.my.shop.regist.service;

import com.qf.common.dto.ResultBean;

public interface IRegistService {

    ResultBean getSmsCode(String phone);

}

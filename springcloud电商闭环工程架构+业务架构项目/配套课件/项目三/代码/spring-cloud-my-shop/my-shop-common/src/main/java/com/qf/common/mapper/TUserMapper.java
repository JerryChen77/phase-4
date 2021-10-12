package com.qf.common.mapper;

import com.qf.common.base.IBaseDAO;
import com.qf.common.entity.TUser;

public interface TUserMapper extends IBaseDAO<TUser> {


    TUser selectByPhone(String phone);
}

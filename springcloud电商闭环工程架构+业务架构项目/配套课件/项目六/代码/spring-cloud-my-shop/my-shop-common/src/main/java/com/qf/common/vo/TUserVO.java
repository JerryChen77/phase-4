package com.qf.common.vo;

import com.qf.common.entity.TUser;
import lombok.Data;

@Data
public class TUserVO extends TUser {

    private String phone;

    private String password;

    private String code;

}

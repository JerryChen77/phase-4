package com.qf.data.view.core.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 封装前端请求过来的数据
 */
@Data
public class AddViewRequest implements Serializable {

    private String token;//已经登陆后获取的login_token，或者是未登陆状态下的view_token

    private Long pid;

    private int count;

}

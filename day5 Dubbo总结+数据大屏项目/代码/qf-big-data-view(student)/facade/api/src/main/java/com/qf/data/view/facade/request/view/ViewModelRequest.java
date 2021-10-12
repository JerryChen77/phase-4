package com.qf.data.view.facade.request.view;

import lombok.Data;

import java.io.Serializable;

@Data
public class ViewModelRequest implements Serializable {

    private String token;//已经登陆后获取的login_token，或者是未登陆状态下的view_token

    private Long pid;

    private int count;
}

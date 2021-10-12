package com.qf.data.view.facade.request.base;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BaseRequest implements Serializable {
	private static final long serialVersionUID = -2404260760940395071L;

	/**
	 * 请求ID
	 */
	protected String requestId;

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}

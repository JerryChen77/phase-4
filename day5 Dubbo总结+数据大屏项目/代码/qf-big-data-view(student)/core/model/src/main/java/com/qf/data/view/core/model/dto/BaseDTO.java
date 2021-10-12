package com.qf.data.view.core.model.dto;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO Base
 *
 */
@Getter
@Setter
public class BaseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Date gmtCreate;

	protected Date gmtModified;

	protected Byte isDeleted;

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}

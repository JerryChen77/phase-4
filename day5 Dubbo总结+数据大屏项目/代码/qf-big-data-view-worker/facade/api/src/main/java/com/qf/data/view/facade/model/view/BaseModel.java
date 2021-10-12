package com.qf.data.view.facade.model.view;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Facade api base model
 */
@Getter
@Setter
public class BaseModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private Date gmtCreate;

	private Date gmtModified;

	private Byte isDeleted;
}

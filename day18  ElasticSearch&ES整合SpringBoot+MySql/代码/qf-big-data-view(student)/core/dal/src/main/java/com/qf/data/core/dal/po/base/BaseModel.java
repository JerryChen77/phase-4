
package com.qf.data.core.dal.po.base;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.Date;

public class BaseModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer uid = 0;

	private Long startId = 0L;

	private Long endId = 0L;

	private Integer did = 0;

	private Byte status = 0;

	private Short type = 0;

	private Integer offset = 0;

	private Integer limit = 100;

	private Date gmtCreate;

	private Date gmtModified;

	private Byte isDeleted;

	/**
	 * @return uid
	 */
	public Integer getUid() {
		return uid;
	}

	/**
	 * @param uid
	 */
	public void setUid(Integer uid) {
		this.uid = uid;
	}

	/**
	 * @return startId
	 */
	public Long getStartId() {
		return startId;
	}

	/**
	 * @param startId
	 */
	public void setStartId(Long startId) {
		this.startId = startId;
	}

	/**
	 * @return endId
	 */
	public Long getEndId() {
		return endId;
	}

	/**
	 * @param endId
	 */
	public void setEndId(Long endId) {
		this.endId = endId;
	}

	/**
	 * @return did
	 */
	public Integer getDid() {
		return did;
	}

	/**
	 * @param did
	 */
	public void setDid(Integer did) {
		this.did = did;
	}

	/**
	 * @return type
	 */
	public Short getType() {
		return type;
	}

	/**
	 * @param type
	 */
	public void setType(Short type) {
		this.type = type;
	}

	/**
	 * @return status
	 */
	public Byte getStatus() {
		return status;
	}

	/**
	 * @param status
	 */
	public void setStatus(Byte status) {
		this.status = status;
	}

	/**
	 * @return offset
	 */
	public Integer getOffset() {
		return offset;
	}

	/**
	 * @param offset
	 */
	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	/**
	 * @return limit
	 */
	public Integer getLimit() {
		if (limit > 200)
			return 200;
		return limit;
	}

	/**
	 * @param limit
	 */
	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Date getGmtCreate() {
		return this.gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Date getGmtModified() {
		return this.gmtModified;
	}

	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}

	public Byte getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Byte isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}

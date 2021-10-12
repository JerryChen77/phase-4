package com.qf.data.view.core.service.base;


import com.qf.data.view.core.model.dto.BaseDTO;

/**
 * Core Service Base
 */
public interface BaseService<T extends BaseDTO> {

	/**
	 * 覆盖式插入记录
	 *
	 * @param record
	 * @return
	 */
	int insert(T record);

	/**
	 * 通过主键ID查询记录
	 *
	 * @param id
	 * @return
	 */
	T selectById(Long id);

	/**
	 * 通过主键ID删除
	 */
	default int deleteById(Long id) {
		return 0;
	}

	/**
	 * 有选择性的插入记录
	 *
	 * @param record
	 * @return
	 */
	default int insertSelective(T record) {
		return 0;
	}

	/**
	 * 有选择性的更新
	 *
	 * @param record
	 * @return
	 */
	default int update(T record) {
		return 0;
	}



	/**
	 * 插入记录，返回主键ID
	 * 
	 * @author taojiagui(星意)
	 */
	default Long insertWithReturnId(T record) {
		return 0L;
	}
}

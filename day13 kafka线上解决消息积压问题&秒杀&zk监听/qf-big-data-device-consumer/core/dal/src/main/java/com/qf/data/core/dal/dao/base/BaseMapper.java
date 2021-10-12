
package com.qf.data.core.dal.dao.base;

/**
 * Mapper基类
 */
public interface BaseMapper<T> {

	/**
	 * 通过主键ID删除
	 */
	int deleteById(Long id);

	/**
	 * 覆盖式插入记录
	 */
	int insert(T record);

	/**
	 * 有选择性的插入记录
	 */
	int insertSelective(T record);

	/**
	 * 通过主键ID查询记录
	 */
	T selectById(Long id);

	/**
	 * 有选择性的更新
	 */
	int updateByIdSelective(T record);

	/**
	 * 覆盖式更新
	 */
	int updateById(T record);

}

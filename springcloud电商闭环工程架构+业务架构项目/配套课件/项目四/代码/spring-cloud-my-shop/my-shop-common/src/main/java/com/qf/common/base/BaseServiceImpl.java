package com.qf.common.base;

import java.util.List;

public abstract class BaseServiceImpl<T> implements IBaseService<T> {


    public abstract IBaseDAO<T> getMapper();

    public int deleteByPrimaryKey(Long id) {
        return getMapper().deleteByPrimaryKey(id);
    }

    public int insert(T record) {
        return getMapper().insert(record);
    }

    public int insertSelective(T record) {
        return getMapper().insertSelective(record);
    }

    public T selectByPrimaryKey(Long id) {
        return getMapper().selectByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(T record) {
        return getMapper().updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(T record) {
        return getMapper().updateByPrimaryKey(record);
    }

    public List<T> selectAll() {
        return getMapper().selectAll();
    }
}

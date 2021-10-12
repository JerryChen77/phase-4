package com.qf.myshardingjdbcdemo.mapper;


import com.qf.myshardingjdbcdemo.entity.TbOrder;

public interface TbOrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TbOrder record);

    int insertSelective(TbOrder record);

    TbOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TbOrder record);

    int updateByPrimaryKey(TbOrder record);
}

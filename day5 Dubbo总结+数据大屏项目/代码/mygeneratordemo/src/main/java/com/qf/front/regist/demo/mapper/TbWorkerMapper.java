package com.qf.front.regist.demo.mapper;

import com.qf.front.regist.demo.entity.po.TbWorker;

public interface TbWorkerMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TbWorker record);

    int insertSelective(TbWorker record);

    TbWorker selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TbWorker record);

    int updateByPrimaryKey(TbWorker record);
}
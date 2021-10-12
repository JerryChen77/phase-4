package com.qf.data.core.dal.dao.worker;


import com.qf.data.core.dal.po.WorkerPO;

public interface TbWorkerMapper {
    int deleteByPrimaryKey(Long id);

    int insert(WorkerPO record);

    int insertSelective(WorkerPO record);

    WorkerPO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(WorkerPO record);

    int updateByPrimaryKey(WorkerPO record);
}
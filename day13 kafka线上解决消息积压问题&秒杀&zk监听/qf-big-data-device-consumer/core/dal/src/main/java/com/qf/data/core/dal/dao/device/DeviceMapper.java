package com.qf.data.core.dal.dao.device;


import com.qf.data.core.dal.po.device.DevicePO;

public interface DeviceMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DevicePO record);

    int insertSelective(DevicePO record);

    DevicePO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DevicePO record);

    int updateByPrimaryKey(DevicePO record);
}
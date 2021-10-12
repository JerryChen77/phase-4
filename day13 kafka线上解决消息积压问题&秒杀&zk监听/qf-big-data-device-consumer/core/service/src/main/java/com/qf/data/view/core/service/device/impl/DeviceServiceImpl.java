package com.qf.data.view.core.service.device.impl;

import com.qf.data.core.dal.dao.device.DeviceMapper;
import com.qf.data.core.dal.po.device.DevicePO;
import com.qf.data.view.core.service.device.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceMapper deviceMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return 0;
    }

    @Override
    public int insert(DevicePO record) {
        return deviceMapper.insert(record);
    }

    @Override
    public int insertSelective(DevicePO record) {
        return 0;
    }

    @Override
    public DevicePO selectByPrimaryKey(Long id) {
        return null;
    }

    @Override
    public int updateByPrimaryKeySelective(DevicePO record) {
        return 0;
    }

    @Override
    public int updateByPrimaryKey(DevicePO record) {
        return 0;
    }
}

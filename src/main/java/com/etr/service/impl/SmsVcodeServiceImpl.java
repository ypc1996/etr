package com.etr.service.impl;

import com.etr.mapper.SmsVcodeMapper;
import com.etr.model.SmsVCode;
import com.etr.service.SmsVcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: ypc
 * @Date: 2019/5/25 11:42
 * @Description:
 */
@Service
public class SmsVcodeServiceImpl implements SmsVcodeService {
    @Autowired
    private SmsVcodeMapper smsVcodeMapper;
    @Override
    public Integer addSmsVCode(SmsVCode smsVCode) {
        return smsVcodeMapper.addSmsVCode(smsVCode);
    }

    @Override
    public SmsVCode findByMobileVCode(String mobile, String vCode, Integer used) {
        return smsVcodeMapper.findByMobileVCode(mobile,vCode,used);
    }

    @Override
    public Integer updateSmsVCode(SmsVCode smsVCode) {
        return smsVcodeMapper.updateSmsVCode(smsVCode);
    }
}

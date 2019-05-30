package com.etr.service;

import com.etr.model.SmsVCode;

/**
 * @Author: ypc
 * @Date: 2019/5/25 11:42
 * @Description:
 */
public interface SmsVcodeService {
    Integer addSmsVCode(SmsVCode smsVCode);
    SmsVCode findByMobileVCode(String mobile, String vCode, Integer used);
    Integer updateSmsVCode(SmsVCode smsVCode);

}

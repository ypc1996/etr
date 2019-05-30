package com.etr.mapper;

import com.etr.model.SmsVCode;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: ypc
 * @Date: 2019/5/25 11:39
 * @Description:
 */
public interface SmsVcodeMapper {
    Integer addSmsVCode(SmsVCode smsVCode);
    SmsVCode findByMobileVCode(@Param("mobile") String mobile, @Param("vCode") String vCode, @Param("used") Integer used);
    Integer updateSmsVCode(SmsVCode smsVCode);
}

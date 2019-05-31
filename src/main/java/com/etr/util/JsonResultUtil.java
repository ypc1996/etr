package com.etr.util;

import com.etr.common.JsonResult;
import com.etr.em.GlobalEnum;

/**
 * Created by LJW on 2019/5/22 - 23:15
 */
public class JsonResultUtil {
    public static JsonResult createSucess(Object object){
        JsonResult jsonResult=new JsonResult();
        jsonResult.setIsSuccess(true);
        jsonResult.setData(object);
        jsonResult.makeStatusAndMsg(GlobalEnum.SUCESS);
        return  jsonResult;
    }
    public static JsonResult createError(GlobalEnum statusEnum){
        JsonResult jsonResult=new JsonResult();
        jsonResult.setIsSuccess(false);
        jsonResult.makeStatusAndMsg(statusEnum);
        return jsonResult;
    }

    public static JsonResult createError(GlobalEnum statusEnum,Object object){
        JsonResult jsonResult=new JsonResult();
        jsonResult.setIsSuccess(false);
        jsonResult.setData(object);
        jsonResult.makeStatusAndMsg(statusEnum);
        return jsonResult;
    }

}

package com.growatt.grohome.http.parser;

import com.growatt.grohome.base.BaseBean;
import com.growatt.grohome.base.BaseException;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class StringResponseBodyConverter implements Converter<ResponseBody, String> {

    @Override
    public String convert(ResponseBody value) throws IOException {
        String jsonString = value.string();
        try {
            JSONObject object = new JSONObject(jsonString);
         /*   int code = object.optInt("result");
            if (0 == code) {//失败
                String data;
                //错误信息
                data = object.getString("errorMsg");
                //异常处理
                throw new BaseException(code, data);
            }*/
            return jsonString;
        } catch (Exception e){
            //数据解析异常即json格式有变动
            throw new BaseException(BaseException.PARSE_ERROR_MSG);
        }finally {
            value.close();
        }
    }

}

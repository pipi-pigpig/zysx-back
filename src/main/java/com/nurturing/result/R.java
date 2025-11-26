package com.nurturing.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class R {
    private Integer code;//响应码，1 代表成功; 0 代表失败
    private String message;  //响应信息 描述字符串
    private Object data; //返回的数据

    //增删改 成功响应
    public static R success(){
        return new R(200,"success",null);
    }
    //查询 成功响应
    public static R success(Object data){
        return new R(200,"success",data);
    }

    public static R success(String msg){return new R(200,msg,null);}

    //失败响应
    public static R error(Integer code,String msg){
        return new R(code,msg,null);
    }
}

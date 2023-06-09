package org.example.entity.R;

import lombok.Data;

/**
 * @author xin麒
 * @date 2023/2/17 15:17
 */
@Data
public class R<T> {
    private int code;
    private T data;

    public R(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public R(int code) {
        this.code = code;
    }

    public static R success(){
     return new R(200);
    }

    public static <T> R<T> success(T data){
     return new R<T>(200,data);
    }


    public static R fail(){
     return new R(500);
    }

    public static <T> R<T> fail(T data){
     return new R<T>(500,data);
    }


}

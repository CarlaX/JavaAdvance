package com.fzw.dubbocommon.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fzw
 * @description
 * @date 2021-07-06
 **/
@NoArgsConstructor
@Data
public class ResultVO {
    private Integer code;
    private String msg;

    public ResultVO(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

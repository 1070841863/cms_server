package com.xuecheng.framework.domain.learning.response;

import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author study
 * @create 2020-04-22 16:30
 */
@Data
@NoArgsConstructor
@ToString
public class GetMediaResult extends ResponseResult {
    private String fileurl;

    public GetMediaResult(ResultCode resultCode, String fileurl) {
        super(resultCode);
        this.fileurl = fileurl;
    }
}

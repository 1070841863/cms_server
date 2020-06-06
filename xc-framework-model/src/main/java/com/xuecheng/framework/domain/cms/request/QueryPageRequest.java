package com.xuecheng.framework.domain.cms.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author study
 * @create 2020-03-31 16:58
 */
@Data
public class QueryPageRequest {
    //接收页面查询的查询条件
    //站点id
    @ApiModelProperty("站点id")
     private String siteId;
    // 页面ID
     private String pageId;
    // 页面名称
     private String pageName;
    // 别名
     private String pageAliase;
    // 模版id
     private String templateId;
     //类型
     private String pageType;

}

package com.xuecheng.search.service;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author study
 * @create 2020-04-16 17:37
 */
@Service
public class SearchService {

    private final static Logger logger=LoggerFactory.getLogger(SearchService.class);

    @Value("${xuecheng.elasticsearch.course.index}")
    private String es_index;
    @Value("${xuecheng.elasticsearch.course.type}")
    private String es_type;
    @Value("${xuecheng.elasticsearch.course.source_field}")
    private String es_source_field;
    @Value("${xuecheng.elasticsearch.course_media.index}")
    private String media_index;
    @Value("${xuecheng.elasticsearch.course_media.type}")
    private String media_type;
    @Value("${xuecheng.elasticsearch.course_media.source_field}")
    private String media_source_field;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    //按关键字查询
    public QueryResponseResult<CoursePub> list(int page, int size, CourseSearchParam courseSearchParam) {
        //设置索引
        SearchRequest searchRequest=new SearchRequest(es_index);
        //设置类型
        searchRequest.types(es_type);
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder= QueryBuilders.boolQuery();
        //sourse源字段过滤
        String[] source_fields = es_source_field.split(",");
        searchSourceBuilder.fetchSource(source_fields,new String[]{});
        //关键字
        if(StringUtils.isNotEmpty(courseSearchParam.getKeyword())){
            //匹配关键字
            MultiMatchQueryBuilder multiMatchQueryBuilder=QueryBuilders.multiMatchQuery(courseSearchParam.getKeyword(),"name"
            ,"teachplan","description"
            );
            //设置占比
            multiMatchQueryBuilder.minimumShouldMatch("70%");
            //设置booast
            multiMatchQueryBuilder.field("name",10);
            boolQueryBuilder.must(multiMatchQueryBuilder);
        }
        /********************根据分类查询**************************/
        //根据分类查询
        if(StringUtils.isNotEmpty(courseSearchParam.getMt())){
            //根据一级分类
            boolQueryBuilder.filter(QueryBuilders.termQuery("mt",courseSearchParam.getMt()));
        }
        if(StringUtils.isNotEmpty(courseSearchParam.getSt())){
            //根据二级分类
            boolQueryBuilder.filter(QueryBuilders.termQuery("st",courseSearchParam.getSt()));
        }
        if(StringUtils.isNotEmpty(courseSearchParam.getGrade())){
            //根据难度等级
            boolQueryBuilder.filter(QueryBuilders.termQuery("grade",courseSearchParam.getGrade()));
        }
        /********************根据分类查询结束**************************/
        /********************分页**************************/
        //分页
        if(page<=0){
            page=1;
        }
        if(size<=0){
            size=20;
        }
        int start=(page-1)*size;
        searchSourceBuilder.from(start);
        searchSourceBuilder.size(size);
        /********************分页结束************************/
        //布尔查询
        searchSourceBuilder.query(boolQueryBuilder);
        /********************高亮设置**************************/
        HighlightBuilder highlightBuilder=new HighlightBuilder();
        highlightBuilder.preTags("<font class='eslight'>");
        highlightBuilder.postTags("</font>");
        //设置高亮字段
        highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
        searchSourceBuilder.highlighter(highlightBuilder);
        /********************高亮设置结束**************************/

        //请求搜索
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse=null;
        try {
            searchResponse=restHighLevelClient.search(searchRequest);

        } catch (IOException e) {
            e.printStackTrace();
            logger.error("xuecheng search error ...{}",e.getMessage());
            return new QueryResponseResult(CommonCode.SUCCESS,new QueryResult<CoursePub>());
        }

        //结果集处理
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        //总记录数
        long totalHits = hits.getTotalHits();
        //数据列表
        List<CoursePub> coursePubs=new ArrayList<CoursePub>();
        for (SearchHit hit:searchHits){
            CoursePub coursePub=new CoursePub();
            //取出course
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            //取出名称
            String name= (String) sourceAsMap.get("name");
//            取出高亮字段内容
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if(highlightFields!=null){
                HighlightField highlightFieldname = highlightFields.get("name");
                if(highlightFieldname!=null){
                    Text[] fragments = highlightFieldname.getFragments();
                    StringBuffer stringBuffer=new StringBuffer();
                    for (Text fragment : fragments) {
                        stringBuffer.append(fragment.toString());
                    }
                    name=stringBuffer.toString();
                }
            }
            coursePub.setName(name);
            //图片
            String pic= (String) sourceAsMap.get("pic");
            coursePub.setPic(pic);
            String ids= (String) sourceAsMap.get("id");
            coursePub.setId(ids);
            //价格
            Double price=null;
            try {
                if(sourceAsMap.get("price")!=null){
                    price= (Double) sourceAsMap.get("price");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            coursePub.setPrice(price);
            Double price_old=null;
            try {
                if(sourceAsMap.get("price_old")!=null){
                    price_old=(Double)sourceAsMap.get("price_old");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            coursePub.setPrice_old(price_old);
            coursePubs.add(coursePub);
        }

        QueryResult queryResult=new QueryResult();
        queryResult.setList(coursePubs);
        queryResult.setTotal(totalHits);

        return new QueryResponseResult<CoursePub>(CommonCode.SUCCESS,queryResult);
    }


    //使用es的客户端向es请求查询索引信息
    public Map<String, CoursePub> getall(String id) {
        //定义搜索请求对象
        SearchRequest searchRequest=new SearchRequest(es_index);
        searchRequest.types(es_type);
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        //使用
        searchSourceBuilder.query(QueryBuilders.termQuery("id",id));
        searchRequest.source(searchSourceBuilder);

        Map<String,CoursePub> map=new HashMap<String,CoursePub>();
        try {
            SearchResponse search = restHighLevelClient.search(searchRequest);
            SearchHits hits = search.getHits();
            SearchHit[] hits1 = hits.getHits();
            for (SearchHit documentFields : hits1) {
                Map<String, Object> sourceAsMap = documentFields.getSourceAsMap();
                //要返回的课程信息
                CoursePub coursePub=new CoursePub();
                String ids= (String) sourceAsMap.get("id");
                String name= (String) sourceAsMap.get("name");
                String grade= (String) sourceAsMap.get("grade");
                String charge= (String) sourceAsMap.get("charge");
                String pic= (String) sourceAsMap.get("pic");
                String description= (String) sourceAsMap.get("description");
                String teachplan= (String) sourceAsMap.get("teachplan");
                coursePub.setId(ids);
                coursePub.setPic(pic);
                coursePub.setName(name);
                coursePub.setCharge(charge);
                coursePub.setGrade(grade);
                coursePub.setDescription(description);
                coursePub.setTeachplan(teachplan);
                map.put(ids,coursePub);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    //根据多个课程计划id查询课程媒资信息
    public QueryResponseResult<TeachplanMediaPub> getallMediaPub(String[] teachplanId) {
        //定义搜索请求对象
        SearchRequest searchRequest=new SearchRequest(media_index);
        searchRequest.types(media_type);
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        //使用
        searchSourceBuilder.query(QueryBuilders.termsQuery("teachplan_id",teachplanId));
        String[] split = media_source_field.split(",");
        searchSourceBuilder.fetchSource(split,new String[]{});
        searchRequest.source(searchSourceBuilder);
        List<TeachplanMediaPub> list=new ArrayList<>();
        long total=0;
        try {
            SearchResponse search = restHighLevelClient.search(searchRequest);
            SearchHits hits = search.getHits();
            total=hits.totalHits;
            SearchHit[] hits1 = hits.getHits();
            for (SearchHit documentFields : hits1) {
                Map<String, Object> sourceAsMap = documentFields.getSourceAsMap();
                //要返回的课程信息
                TeachplanMediaPub teachplanMediaPub=new TeachplanMediaPub();
                String courseid= (String) sourceAsMap.get("courseid");
                String teachplan_id= (String) sourceAsMap.get("teachplan_id");
                String media_id= (String) sourceAsMap.get("media_id");
                String media_url= (String) sourceAsMap.get("media_url");
                String media_fileoriginalname= (String) sourceAsMap.get("media_fileoriginalname");
                teachplanMediaPub.setCourseId(courseid);
                teachplanMediaPub.setTeachplanId(teachplan_id);
                teachplanMediaPub.setMediaFileOriginalName(media_fileoriginalname);
                teachplanMediaPub.setMediaUrl(media_url);
                teachplanMediaPub.setMediaId(media_id);
                list.add(teachplanMediaPub);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        QueryResult queryResult=new QueryResult();
        queryResult.setList(list);
        queryResult.setTotal(total);
        QueryResponseResult<TeachplanMediaPub> res=new QueryResponseResult<TeachplanMediaPub>(CommonCode.SUCCESS,queryResult);
        return res;
    }
}

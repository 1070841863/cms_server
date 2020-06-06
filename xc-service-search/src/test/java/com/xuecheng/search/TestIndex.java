package com.xuecheng.search;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.search.dao.CategoryMapper;
import net.minidev.json.JSONUtil;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.json.JSONString;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.text.Highlighter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author study
 * @create 2020-04-14 20:09
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestIndex {

    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    private RestClient restClient;

    //创建索引库
    @Test
    public void testCreateIndex() throws IOException {
        //创建索引对象
        CreateIndexRequest createIndexRequest=new CreateIndexRequest("xc_course");
        //指定参数
        createIndexRequest.settings(Settings.builder().put("number_of_shards","1").put("number_of_replicas","0").build());

        String mapping_info="{\n" +
                "\t\"properties\": {\n" +
                "\t\t\"description\": {\n" +
                "\t\t\t\"type\": \"text\",\n" +
                "\t\t\t\"analyzer\": \"ik_max_word\",\n" +
                "\t\t\t\"search_analyzer\": \"ik_smart\"\n" +
                "\t\t},\n" +
                "\t\t\"name\": {\n" +
                "\t\t\t\"type\": \"text\",\n" +
                "\t\t\t\"analyzer\": \"ik_max_word\",\n" +
                "\t\t\t\"search_analyzer\": \"ik_smart\"\n" +
                "\t\t},\n" +
                "\t\t\"pic\": {\n" +
                "\t\t\t\"type\": \"text\",\n" +
                "\t\t\t\"index\": false\n" +
                "\t\t},\n" +
                "\t\t\"price\": {\n" +
                "\t\t\t\"type\": \"float\"\n" +
                "\t\t},\n" +
                "\t\t\"studymodel\": {\n" +
                "\t\t\t\"type\": \"keyword\"\n" +
                "\t\t},\n" +
                "\t\t\"timestamp\": {\n" +
                "\t\t\t\"type\": \"date\",\n" +
                "\t\t\t\"format\": \"yyyy‐MM‐dd HH:mm:ss||yyyy‐MM‐dd||epoch_millis\"\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}";
        //指定映射
        createIndexRequest.mapping("doc",mapping_info, XContentType.JSON);


        //操作索引的客户端
        IndicesClient indices = restHighLevelClient.indices();
        //创建索引库
        CreateIndexResponse createIndexResponse = indices.create(createIndexRequest);
        //得到响应结果
        boolean acknowledged = createIndexResponse.isAcknowledged();
        System.out.println(acknowledged);
    }


    //删除索引库
    @Test
    public void testDeleteIndex() throws IOException {
        //删除索引请求对象
        DeleteIndexRequest deleteIndexRequest=new DeleteIndexRequest("xc_course");
        //操作索引的客户端
        IndicesClient indices = restHighLevelClient.indices();
        //执行删除索引
        DeleteIndexResponse deleteIndexResponse=indices.delete(deleteIndexRequest);
        //得到响应结果
        boolean acknowledged = deleteIndexResponse.isAcknowledged();
        System.out.println(acknowledged);
    }

    //添加文档
    @Test
    public void testAddDoc() throws IOException {
        //准备json
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("name","spring cloud实战");
        map.put("description","本章节开始讲解三种注册中心");
        map.put("studymodel","201001");
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy‐MM‐dd HH:mm:ss");
        map.put("timestamp",simpleDateFormat.format(new Date()));
        map.put("price",5.6f);
        map.put("pic","http://www.123.com");
        //索引请求对象
        IndexRequest indexRequest=new IndexRequest("xc_course","doc");
        //指定索引文档内容
        indexRequest.source(map);
        //索引响应对象
        IndexResponse indexResponse=restHighLevelClient.index(indexRequest);
        //获取响应结果
        DocWriteResponse.Result result = indexResponse.getResult();
        System.out.println(result);
    }

    @Test
    public void getDoc() throws IOException {
        GetRequest getRequest=new GetRequest("xc_course","doc","AqzIeHEBG2jDw6V9Hghx");
        GetResponse getResponse=restHighLevelClient.get(getRequest);
        boolean exists = getResponse.isExists();
        Map<String, Object> map = getResponse.getSourceAsMap();
        System.out.println(map);

    }

    @Test
    public void updateDoc() throws IOException {
        UpdateRequest updateRequest=new UpdateRequest("xc_course","doc","AqzIeHEBG2jDw6V9Hghx");
        Map<String,String> map=new HashMap<>();
        map.put("name","cloud-实战");
        updateRequest.doc(map);
        UpdateResponse update = restHighLevelClient.update(updateRequest);
        RestStatus status = update.status();
        System.out.println(status);

    }


    @Test
    public void deleteDoc() throws IOException {
        //删除文档id
        String id="AqzIeHEBG2jDw6V9Hghx";
        //删除文档对象
        DeleteRequest deleteIndexRequest=new DeleteRequest("xc_course","doc",id);
        DeleteResponse delete = restHighLevelClient.delete(deleteIndexRequest);
        //获取响应
        DocWriteResponse.Result result = delete.getResult();
        System.out.println(result);


    }


    @Test
    public void testSearchAll() throws IOException, ParseException {
        SearchRequest searchRequest=new SearchRequest("xc_course");
        //指定类型
        searchRequest.types("doc");
        //
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        //source过滤字段,第一个参数结果集包括哪些字段，第二个结果集不包括哪些字段
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel,timestamp"},new String[]{});
        //向搜索请求对象中设置搜索源
        searchRequest.source(searchSourceBuilder);
        //执行搜索
        SearchResponse search = restHighLevelClient.search(searchRequest);
        SearchHits hits = search.getHits();
        //匹配的总记录数
        long totalHits = hits.getTotalHits();
        //得到匹配度高的文档
        SearchHit[] hits1 = hits.getHits();
        for (SearchHit hit : hits1) {
            String index=hit.getIndex();
            String type = hit.getType();
            String id = hit.getId();
            float score = hit.getScore();
            String sourceAsString = hit.getSourceAsString();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String studymodel = (String) sourceAsMap.get("studymodel");
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date timestamp= simpleDateFormat.parse((String) sourceAsMap.get("timestamp"));
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(timestamp);
        }
    }


    @Test
    public void searchDocByPageAndSize() throws IOException {
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        String[] split = new String[]{"1","3"};
        List<String> strings = Arrays.asList(split);
        searchSourceBuilder.query(QueryBuilders.termsQuery("_id",strings));
        //source源字段过虑
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel"}, new String[]{});
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        SearchHit[] hits = searchResponse.getHits().getHits();
        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            System.out.println(sourceAsString);
        }

    }


    @Test
    public void testMatchQuery() throws IOException {
        SearchRequest searchRequest=new SearchRequest("xc_course");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //sourse源字段过滤
        //匹配关键字
        searchSourceBuilder.fetchSource(new String[]{"name","description"},new String[]{});
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery("spring框架","name","description").minimumShouldMatch("50%").field("name",10));
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = restHighLevelClient.search(searchRequest);
        SearchHit[] hits = search.getHits().getHits();
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }
    }

    @Test
    public void testboolQuery() throws IOException {
        SearchRequest searchRequest=new SearchRequest("xc_course");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //sourse源字段过滤
        searchSourceBuilder.fetchSource(new String[]{"name","description","studymodel","price"},new String[]{});
        MultiMatchQueryBuilder multiMatchQueryBuilder=QueryBuilders.multiMatchQuery("开发框架","name","description")
                .minimumShouldMatch("50%").field("name",10);
        //布尔查询
        BoolQueryBuilder boolQueryBuilder=QueryBuilders.boolQuery();
        //过滤
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(0).lte(100));
        boolQueryBuilder.must(multiMatchQueryBuilder);
        //排序
        searchSourceBuilder.sort(new FieldSortBuilder("price").order(SortOrder.ASC));
        //设置高亮
        HighlightBuilder highlightBuilder=new HighlightBuilder();
        highlightBuilder.preTags("<tag1>");
        highlightBuilder.postTags("</tag2>");
        //对name进行设置高亮
        highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
//        highlightBuilder.fields().add(new HighlightBuilder.Field("description"));
        searchSourceBuilder.highlighter(highlightBuilder);
        //设置布尔查询
        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = restHighLevelClient.search(searchRequest);
        SearchHit[] hits = search.getHits().getHits();
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
            System.out.println("------------------");
            //取出高亮字段内容
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if(highlightFields!=null){
                HighlightField name = highlightFields.get("name");
                if(name!=null){
                    Text[] fragments = name.getFragments();
                    StringBuffer stringBuffer=new StringBuffer();
                    for (Text fragment : fragments) {
                        stringBuffer.append(fragment.toString());
                    }
                    System.out.println(stringBuffer);
                }
            }
            System.out.println("--------------------");
        }
    }


    @Autowired
    private CategoryMapper categoryMapper;

    @Test
    public void testMapper() throws Exception {
        CategoryNode all = categoryMapper.getAll();
        ObjectMapper objectMapper=new ObjectMapper();
        String string = objectMapper.writeValueAsString(all);
        System.out.println(string);
        FileOutputStream fileInputStream=new FileOutputStream(new File("D:\\test\\test.json"));
        fileInputStream.write(string.getBytes());
        fileInputStream.close();
    }



}

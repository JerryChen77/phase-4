package com.qf.data.view.facade.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qf.data.view.core.model.result.ResultModel;
import com.qf.data.view.facade.api.SearchFacade;
import com.qf.data.view.facade.request.SearchDeviceModelRequest;
import com.qf.data.view.facade.response.SearchDeviceModelResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchFacadeImpl implements SearchFacade {
    //通过es做查询
    @Autowired
    private RestHighLevelClient client;
    @Override
    public ResultModel<List<SearchDeviceModelResponse>> searchDevice(SearchDeviceModelRequest request) {
        String deviceName = request.getDeviceName();

        String index = "device";
        String type = "face";

        //1.封装一个searchRequest对象
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);

        //2.
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        String queryContent = "*"+deviceName+"*";
        searchSourceBuilder.query(QueryBuilders.wildcardQuery("deviceName", queryContent));

        //3.
        searchRequest.source(searchSourceBuilder);

        //4.
        try {
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] hits = response.getHits().getHits();
            List<SearchDeviceModelResponse> deviceList= new ArrayList<>();
            for (SearchHit hit : hits) {
                String json = hit.getSourceAsString();
                ObjectMapper objectMapper = new ObjectMapper();
                SearchDeviceModelResponse device = objectMapper.readValue(json, SearchDeviceModelResponse.class);
                //存到集合里
                deviceList.add(device);
            }
            return ResultModel.success(deviceList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

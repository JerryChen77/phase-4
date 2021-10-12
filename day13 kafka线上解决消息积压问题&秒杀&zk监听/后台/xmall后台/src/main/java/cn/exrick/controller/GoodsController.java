package cn.exrick.controller;

import cn.exrick.common.jedis.JedisClient;
import cn.exrick.pojo.TbPanel;
import cn.exrick.pojo.TbPanelContent;
import cn.exrick.pojo.common.Result;
import cn.exrick.pojo.common.SearchResult;
import cn.exrick.pojo.front.AllGoodsResult;
import cn.exrick.pojo.front.ProductDet;
import cn.exrick.service.ContentService;
import cn.exrick.service.SearchItemService;
import cn.exrick.service.SearchService;
import cn.exrick.utils.HttpUtil;
import cn.exrick.utils.ResultUtil;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(description = "商品页面展示")
public class GoodsController {

    private final static Logger log= LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    private ContentService contentService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private JedisClient jedisClient;

    @Value("${ES_CONNECT_IP}")
    private String ES_CONNECT_IP;

    @Value("${ES_NODE_CLIENT_PORT}")
    private String ES_NODE_CLIENT_PORT;

    @RequestMapping(value = "/goods/navList",method = RequestMethod.GET)
    @ApiOperation(value = "获取导航栏")
    public Result<List<TbPanelContent>> getNavList(){

        List<TbPanelContent> list=contentService.getNavList();
        return new ResultUtil<List<TbPanelContent>>().setData(list);
    }

    @RequestMapping(value = "/goods/home",method = RequestMethod.GET)
    @ApiOperation(value = "首页内容展示")
    public Result<List<TbPanel>> getProductHome(){

        List<TbPanel> list=contentService.getHome();
        return new ResultUtil<List<TbPanel>>().setData(list);
    }

    @RequestMapping(value = "/goods/productDet",method = RequestMethod.GET)
    @ApiOperation(value = "商品详情")
    public Result<ProductDet> getProductDet(Long productId){

        ProductDet productDet=contentService.getProductDet(productId);
        return new ResultUtil<ProductDet>().setData(productDet);
    }

    @RequestMapping(value = "/goods/allGoods",method = RequestMethod.GET)
    @ApiOperation(value = "所有商品")
    public Result<AllGoodsResult> getAllProduct(@RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "20") int size,
                                                @RequestParam(defaultValue = "") String sort,
                                                @RequestParam(defaultValue = "") Long cid,
                                                @RequestParam(defaultValue = "-1") int priceGt,
                                                @RequestParam(defaultValue = "-1") int priceLte){

        AllGoodsResult allGoodsResult=new AllGoodsResult();

//        String redisKey="allGoods:"+page+"_"+size+"_"+sort+"_"+cid+"_"+priceGt+"_"+priceLte;
//        //读取缓存
//        String temp=jedisClient.get(redisKey);
//        if(StringUtils.isNotBlank(temp)) {
//            allGoodsResult = new Gson().fromJson(temp, AllGoodsResult.class);
//            log.info("读取了" + redisKey + "缓存");
//            return new ResultUtil<AllGoodsResult>().setData(allGoodsResult);
//        }

        allGoodsResult=contentService.getAllProduct(page,size,sort,cid,priceGt,priceLte);

        //更新缓存
//        String value=new Gson().toJson(allGoodsResult);
//        jedisClient.set(redisKey,value);
//        jedisClient.expire(redisKey,604800);
//        log.info("更新了"+redisKey+"缓存");
        return new ResultUtil<AllGoodsResult>().setData(allGoodsResult);
    }

    @RequestMapping(value = "/goods/search",method = RequestMethod.GET)
    @ApiOperation(value = "搜索商品ES")
    public Result<SearchResult> searchProduct(@RequestParam(defaultValue = "") String key,
                                              @RequestParam(defaultValue = "1") int page,
                                              @RequestParam(defaultValue = "20") int size,
                                              @RequestParam(defaultValue = "") String sort,
                                              @RequestParam(defaultValue = "-1") int priceGt,
                                              @RequestParam(defaultValue = "-1") int priceLte){

        SearchResult searchResult=new SearchResult();

//        String redisKey="search:"+key+"_"+page+"_"+size+"_"+sort+"_"+priceGt+"_"+priceLte;
//        //读取缓存
//        SearchResult searchResult=new SearchResult();
//        String temp=jedisClient.get(redisKey);
//        if(StringUtils.isNotBlank(temp)){
//            searchResult=new Gson().fromJson(temp,SearchResult.class);
//            log.info("读取了"+redisKey+"缓存");
//            return new ResultUtil<SearchResult>().setData(searchResult);
//        }

        searchResult=searchService.search(key,page,size,sort,priceGt,priceLte);

        //更新缓存
//        String value=new Gson().toJson(searchResult);
//        jedisClient.set(redisKey,value);
//        jedisClient.expire(redisKey,604800);
//        log.info("更新了"+redisKey+"缓存");
        return new ResultUtil<SearchResult>().setData(searchResult);
    }

    @RequestMapping(value = "/goods/recommend",method = RequestMethod.GET)
    @ApiOperation(value = "商品推荐板块")
    public Result<List<TbPanel>> getRecommendGoods(){

        List<TbPanel> list=contentService.getRecommendGoods();
        return new ResultUtil<List<TbPanel>>().setData(list);
    }

    @RequestMapping(value = "/goods/thank",method = RequestMethod.GET)
    @ApiOperation(value = "我要捐赠板块")
    public Result<List<TbPanel>> getThankGoods(){

        List<TbPanel> list=contentService.getThankGoods();
        return new ResultUtil<List<TbPanel>>().setData(list);
    }

    @RequestMapping(value = "/goods/quickSearch",produces= "text/plain;charset=UTF-8",method = RequestMethod.GET)
    @ApiOperation(value = "快速搜索")
    public String getQuickSearch(@RequestParam(defaultValue = "") String key){

        String result = null;
        try {
            result = HttpUtil.sendGet("http://"+ES_CONNECT_IP+":"+ES_NODE_CLIENT_PORT+"/item/itemList/_search?q=productName:"+key);
        }catch (Exception e){
            log.error(e.toString());
        }
        return result;
    }

}

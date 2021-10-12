package cn.exrick.service;


import cn.exrick.pojo.common.SearchResult;

public interface SearchService {

	SearchResult search(String keyword, int page, int size, String sort, int priceGt, int priceLte);
}

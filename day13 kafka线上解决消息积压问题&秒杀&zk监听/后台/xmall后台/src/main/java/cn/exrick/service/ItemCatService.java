package cn.exrick.service;

import cn.exrick.pojo.TbItemCat;
import cn.exrick.pojo.common.ZTreeNode;


import java.util.List;

/**
 * Created by Exrick on 2017/8/2.
 */
public interface ItemCatService {

    TbItemCat getItemCatById(Long id);

    List<ZTreeNode> getItemCatList(int parentId);

    int addItemCat(TbItemCat tbItemCat);

    int updateItemCat(TbItemCat tbItemCat);

    void deleteItemCat(Long id);

    void deleteZTree(Long id);
}

package cn.exrick.service;



import cn.exrick.pojo.front.CartProduct;

import java.util.List;

public interface CartService {

    int addCart(long userId, long itemId, int num);

    List<CartProduct> getCartList(long userId);

    int updateCartNum(long userId, long itemId, int num, String checked);

    int deleteCartItem(long userId, long itemId);

    int checkAll(long userId, String checked);

    /**
     * 删除全部勾选的
     * @param userId
     * @return
     */
    int delChecked(long userId);
}

package com.qf.data.view.core.model.common;

public class CommonMethod {

    public static final String MIAOSHA_ORDER_PRE = "miaosha:order:";

    public static final String MIAOSHA_ORDER_WAIT_FLAG_PRE = "miaosha:order:wait:flag:";

    public static final String MIAOSHA_VERIFY_CODE_PRE = "miaosha:verify:code:";

    public static final String MIAOSHA_TOKEN_PRE = "miaosha:token:";

    public static String getMiaoshaOrderRedisKey(Long account, String productId) {

        return new StringBuffer().append(MIAOSHA_ORDER_PRE).append(account).append(":").append(productId).toString();

    }

    public static String getMiaoshaOrderWaitFlagRedisKey(Long account, String productId) {
        return new StringBuffer().append(MIAOSHA_ORDER_WAIT_FLAG_PRE).append(account).append(":").append(productId).toString();
    }

    public static String getMiaoshaVerifyCodeRedisKey(Long account, String productId) {
        return new StringBuffer().append(MIAOSHA_VERIFY_CODE_PRE).append(account).append(":").append(productId).toString();
    }

    public static String getMiaoshaTokenRedisKey(Long account, String productId) {
        return new StringBuffer().append(MIAOSHA_TOKEN_PRE).append(account).append(":").append(productId).toString();
    }
}

package cn.exrick.utils;

import java.util.Random;

/**
 * @author Exrickx
 */
public class IDUtil {

    /**
     * 随机id生成  12381283823233 22  ==》秒杀 应对千万级并发 ==》 leaf美团提供的中间件 唯一性id 全球唯一
     */
    public static long getRandomId() {
        long millis = System.currentTimeMillis();
        //加上两位随机数
        Random random = new Random();
        int end2 = random.nextInt(99);
        //如果不足两位前面补0
        String str = millis + String.format("%02d", end2);
        long id = new Long(str);
        return id;
    }

}


package cn.exrick.service.impl;

import cn.exrick.common.exception.XmallException;
import cn.exrick.common.jedis.JedisClient;
import cn.exrick.mapper.TbMemberMapper;
import cn.exrick.pojo.TbMember;
import cn.exrick.pojo.front.Member;
import cn.exrick.service.LoginService;
import cn.exrick.service.MemberFrontService;
import cn.exrick.utils.QiniuUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Exrickx
 */
@Service
public class MemberFrontServiceImpl implements MemberFrontService {

    @Autowired
    private LoginService loginService;
    @Autowired
    private TbMemberMapper tbMemberMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;

    @Override
    public String imageUpload(Long userId,String token,String imgData) {

        //过滤data:URL
        String base64= QiniuUtil.base64Data(imgData);
        String imgPath= QiniuUtil.qiniuBase64Upload(base64);

        TbMember tbMember=tbMemberMapper.selectByPrimaryKey(userId);
        if(tbMember==null){
            throw new XmallException("通过id获取用户失败");
        }
        tbMember.setFile(imgPath);
        if(tbMemberMapper.updateByPrimaryKey(tbMember)!=1){
            throw new XmallException("更新用户头像失败");
        }

        //更新缓存
        Member member=loginService.getUserByToken(token);
        member.setFile(imgPath);
        jedisClient.set("SESSION:" + token, new Gson().toJson(member));
        return imgPath;
    }
}

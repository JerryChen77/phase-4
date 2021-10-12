package cn.exrick.mapper;

import java.util.List;

import cn.exrick.pojo.TbExpress;
import cn.exrick.pojo.TbExpressExample;
import org.apache.ibatis.annotations.Param;

public interface TbExpressMapper {
    long countByExample(TbExpressExample example);

    int deleteByExample(TbExpressExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbExpress record);

    int insertSelective(TbExpress record);

    List<TbExpress> selectByExample(TbExpressExample example);

    TbExpress selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbExpress record, @Param("example") TbExpressExample example);

    int updateByExample(@Param("record") TbExpress record, @Param("example") TbExpressExample example);

    int updateByPrimaryKeySelective(TbExpress record);

    int updateByPrimaryKey(TbExpress record);
}
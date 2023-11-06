package com.example.mapper;

import com.example.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2023-11-01
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

}

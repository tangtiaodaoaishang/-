package com.example.mapper;

import com.example.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2023-11-02
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    @Select("select c.*,u.nickname from t_comment c left join sys_user u on c.user_id=u.id where c.article_id=#{articleId} order by c.id desc")
    List<Comment> findCurrentDetail(@Param("articleId") Integer articleId);
}

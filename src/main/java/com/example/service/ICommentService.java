package com.example.service;

import com.example.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2023-11-02
 */
public interface ICommentService extends IService<Comment> {

    List<Comment> findCurrentDetail(Integer articleId);
}

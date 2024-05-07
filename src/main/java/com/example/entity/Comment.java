package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author baomidou
 * @since 2023-11-02
 */
@Getter
@Setter
@TableName("t_comment")
@ApiModel(value = "Comment对象", description = "")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("id")
        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("评论内容")
      private String content;

      @ApiModelProperty("评论人id")
      private Integer userId;

      @ApiModelProperty("评论时间")
      private String time;

      @ApiModelProperty("父id")
      private Integer pid;
      @TableField(exist = false)
      private String pNickname;  //父节点的用户昵称
      @TableField(exist = false)
      private Integer pUserId;  //父节点的用户id
      @ApiModelProperty("最上级评论id")
      private Integer originId;

      @ApiModelProperty("关联文章id")
      private Integer articleId;
      @TableField(exist = false)
      private String nickname;

      //子节点(回复内容)
      @TableField(exist = false)
      private List<Comment> children;


}

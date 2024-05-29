package com.example.springboot.entity;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author
 */
@Getter
@Setter
@ApiModel(value = "Comment对象", description = "")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("标题")
    @Alias("标题")
    private String name;

    @ApiModelProperty("内容")
    @Alias("内容")
    private String content;

    @ApiModelProperty("创建时间")
    @Alias("创建时间")
    private String createTime;

    @ApiModelProperty("父级id")
    @Alias("父级id")
    private Integer pid;

    @ApiModelProperty("留言用户id")
    @Alias("留言用户id")
    private Integer userid;

    @ApiModelProperty("留言用户")
    @Alias("留言用户")
    private String user;

    @ApiModelProperty("回复给")
    @Alias("回复给")
    private String replyUser;
    @TableField(exist = false)
    private List<Comment> children;

}

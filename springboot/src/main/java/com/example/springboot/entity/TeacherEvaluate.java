package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since
 */
@Getter
@Setter
@TableName("teacher_evaluate")
@ApiModel(value = "TeacherEvaluate对象", description = "")
public class TeacherEvaluate implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("学生id")
    private Integer studentid;

    @ApiModelProperty("学生姓名")
    private String studentname;


    @ApiModelProperty("教师名称")
    private String teachername;

    @ApiModelProperty("评价内容")
    private String content;

    @ApiModelProperty("创建时间")
    private String createTime;

    @ApiModelProperty("0-满意，1-不满意")
    private String grade;


}

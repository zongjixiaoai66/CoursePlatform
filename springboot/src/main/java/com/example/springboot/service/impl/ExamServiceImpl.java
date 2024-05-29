package com.example.springboot.service.impl;

import com.example.springboot.entity.Exam;
import com.example.springboot.mapper.ExamMapper;
import com.example.springboot.service.IExamService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since
 */
@Service
public class ExamServiceImpl extends ServiceImpl<ExamMapper, Exam> implements IExamService {

}

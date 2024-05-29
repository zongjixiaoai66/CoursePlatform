package com.example.springboot.service.impl;

import com.example.springboot.entity.JobQuestion;
import com.example.springboot.mapper.JobQuestionMapper;
import com.example.springboot.service.IJobQuestionService;
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
public class JobQuestionServiceImpl extends ServiceImpl<JobQuestionMapper, JobQuestion> implements IJobQuestionService {

}

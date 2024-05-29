package com.example.springboot.service.impl;

import com.example.springboot.entity.Job;
import com.example.springboot.mapper.JobMapper;
import com.example.springboot.service.IJobService;
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
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements IJobService {

}

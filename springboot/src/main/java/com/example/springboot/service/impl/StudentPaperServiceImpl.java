package com.example.springboot.service.impl;

import com.example.springboot.entity.StudentPaper;
import com.example.springboot.mapper.StudentPaperMapper;
import com.example.springboot.service.IStudentPaperService;
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
public class StudentPaperServiceImpl extends ServiceImpl<StudentPaperMapper, StudentPaper> implements IStudentPaperService {

}

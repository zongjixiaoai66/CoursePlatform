package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.springboot.entity.ExamPaper;
import com.example.springboot.mapper.ExamPaperMapper;
import com.example.springboot.service.IExamPaperService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since
 */
@Service
public class ExamPaperServiceImpl extends ServiceImpl<ExamPaperMapper, ExamPaper> implements IExamPaperService {

    @Transactional
    @Override
    public void addPaper(ExamPaper examPaper) {
        // 先删除本考试的记录
        remove(new UpdateWrapper<ExamPaper>().eq("exam_id", examPaper.getExamId()));
        save(examPaper);
    }
}

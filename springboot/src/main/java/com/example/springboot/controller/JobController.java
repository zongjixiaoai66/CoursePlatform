package com.example.springboot.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.net.URLEncoder;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.controller.dto.HandPaperDTO;
import com.example.springboot.entity.*;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.service.IJobQuestionService;
import com.example.springboot.service.IPaperQuestionService;
import com.example.springboot.service.IQuestionService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;
import org.springframework.web.multipart.MultipartFile;
import com.example.springboot.utils.TokenUtils;

import com.example.springboot.service.IJobService;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 
 */
@RestController
@RequestMapping("/job")
public class JobController {

    @Resource
    private IJobService jobService;

    @Resource
    private IPaperQuestionService paperQuestionService;

    @Resource
    private IJobQuestionService jobQuestionService;

    private final String now = DateUtil.now();

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Job job) {
        if (job.getId() == null) {
            job.setCreateTime(DateUtil.now());
            //job.setUser(TokenUtils.getCurrentUser().getUsername());
        }
        jobService.saveOrUpdate(job);
        return Result.success();
    }

    @PostMapping("/handPaper")
    public Result handPaper(@RequestBody HandPaperDTO paperDTO) {
        // 删除老的试卷
        UpdateWrapper<JobQuestion> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("job_id", paperDTO.getJobId());
        jobQuestionService.remove(updateWrapper);

        if (CollUtil.isEmpty(paperDTO.getHandleQuestionIds())) {
            throw new ServiceException("-1", "题目数量不足");
        }
        List<Integer> handleQuestionIds = paperDTO.getHandleQuestionIds();
        List<JobQuestion> list = new ArrayList<>();
        for (Integer handleQuestionId : handleQuestionIds) {
            JobQuestion jobQuestion = new JobQuestion();
            jobQuestion.setJobId(paperDTO.getJobId());
            jobQuestion.setQuestionId(handleQuestionId);
            list.add(jobQuestion);
        }
        jobQuestionService.saveBatch(list);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        jobService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        jobService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping("/view/{jobId}")
    public Result view(@PathVariable Integer jobId) {
        List<Question> list = paperQuestionService.selectQuestionsByJobId(jobId);
        return Result.success(list);
    }

    @GetMapping
    public Result findAll() {
        return Result.success(jobService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(jobService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Job> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
//        User currentUser = TokenUtils.getCurrentUser();
//        if (currentUser.getRole().equals("ROLE_USER")) {
//            queryWrapper.eq("user", currentUser.getUsername());
//        }
        return Result.success(jobService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
    * 导出接口
    */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Job> list = jobService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("Job信息表", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

        }

    /**
     * excel 导入
     * @param file
     * @throws Exception
     */
    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
        List<Job> list = reader.readAll(Job.class);

        jobService.saveBatch(list);
        return Result.success();
    }

    private User getUser() {
        return TokenUtils.getCurrentUser();
    }

}


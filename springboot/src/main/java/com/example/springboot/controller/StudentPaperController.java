package com.example.springboot.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.net.URLEncoder;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Exam;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.service.IExamService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;
import org.springframework.web.multipart.MultipartFile;
import com.example.springboot.entity.User;
import com.example.springboot.utils.TokenUtils;

import com.example.springboot.service.IStudentPaperService;
import com.example.springboot.entity.StudentPaper;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 
 * @since
 */
@RestController
@RequestMapping("/studentpaper")
public class StudentPaperController {

    @Resource
    private IStudentPaperService studentPaperService;
    @Resource
    private IExamService examService;

    private final String now = DateUtil.now();

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody StudentPaper studentPaper) {
        if (studentPaper.getId() == null) {

            List<StudentPaper> list = studentPaperService.list(new QueryWrapper<StudentPaper>().eq("exam_id", studentPaper.getExamId())
                    .eq("user_id", TokenUtils.getCurrentUser().getId()));
            if (CollUtil.isNotEmpty(list)) {
                throw new ServiceException("-1", "您已提交考卷！");
            }

            studentPaper.setTime(DateUtil.now());
            studentPaper.setUserId(TokenUtils.getCurrentUser().getId());
        }
        studentPaperService.saveOrUpdate(studentPaper);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        studentPaperService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        studentPaperService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(studentPaperService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(studentPaperService.getById(id));
    }


    @GetMapping("/findExamNameByUserId/{userid}")
    public Result findExamNameByUserId(@PathVariable Integer userid) {
        QueryWrapper<StudentPaper> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userid);
        List<StudentPaper> studentPapers = studentPaperService.list(wrapper);
        List<Integer> examIdList = new ArrayList<>();
        List<String> examNameList = new ArrayList<>();
        for (StudentPaper studentPaper : studentPapers) {
            examIdList.add(studentPaper.getExamId());
        }
        List<Exam> exams;
        if(examIdList.size() > 0) {
            exams = examService.listByIds(examIdList);
        } else {
            exams = examService.list();
        }
        for (Exam exam : exams) {
            examNameList.add(exam.getName());
        }
        return Result.success(examNameList);
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<StudentPaper> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
//        User currentUser = TokenUtils.getCurrentUser();
//        if (currentUser.getRole().equals("ROLE_USER")) {
//            queryWrapper.eq("user", currentUser.getUsername());
//        }
        return Result.success(studentPaperService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
    * 导出接口
    */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<StudentPaper> list = studentPaperService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("StudentPaper信息表", "UTF-8");
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
        List<StudentPaper> list = reader.readAll(StudentPaper.class);

        studentPaperService.saveBatch(list);
        return Result.success();
    }

    private User getUser() {
        return TokenUtils.getCurrentUser();
    }

}


package com.example.springboot.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.net.URLEncoder;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;
import org.springframework.web.multipart.MultipartFile;
import com.example.springboot.entity.User;
import com.example.springboot.utils.TokenUtils;

import com.example.springboot.service.IFinishUserJobService;
import com.example.springboot.entity.FinishUserJob;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 
 */
@RestController
@RequestMapping("/finishUserJob")
public class FinishUserJobController {

    @Resource
    private IFinishUserJobService finishUserJobService;

    private final String now = DateUtil.now();

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody FinishUserJob finishUserJob) {
        if (finishUserJob.getId() == null) {
            //finishUserJob.setTime(DateUtil.now());
            //finishUserJob.setUser(TokenUtils.getCurrentUser().getUsername());
        }
        finishUserJobService.saveOrUpdate(finishUserJob);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        finishUserJobService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        finishUserJobService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(finishUserJobService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(finishUserJobService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<FinishUserJob> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        return Result.success(finishUserJobService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @GetMapping("/submitJob/{jobid}/{userid}")
    public Result submitJob(@PathVariable String jobid,@PathVariable Integer userid) {
        QueryWrapper<FinishUserJob> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("jobid",Integer.valueOf(jobid));
        queryWrapper.eq("userid",userid);
        FinishUserJob one = finishUserJobService.getOne(queryWrapper);
        if(one != null) {
            return Result.error("400","你已经完成了该作业，不能重复提交");
        }
        FinishUserJob finishUserJob = new FinishUserJob();
        finishUserJob.setUserid(userid);
        finishUserJob.setJobid(Integer.valueOf(jobid));
        finishUserJobService.save(finishUserJob);
        return Result.success("完成作业");
    }

    @GetMapping("/isSubmitJob/{jobid}/{userid}")
    public Result isSubmitJob(@PathVariable String jobid,@PathVariable Integer userid) {
        QueryWrapper<FinishUserJob> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("jobid",Integer.valueOf(jobid));
        queryWrapper.eq("userid",userid);
        FinishUserJob one = finishUserJobService.getOne(queryWrapper);
        if(one != null) {
            return Result.success();
        }else{
            return Result.error("400","完成作业以后才可以查看作业解析");
        }
    }

    /**
    * 导出接口
    */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<FinishUserJob> list = finishUserJobService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("FinishUserJob信息表", "UTF-8");
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
        List<FinishUserJob> list = reader.readAll(FinishUserJob.class);

        finishUserJobService.saveBatch(list);
        return Result.success();
    }

    private User getUser() {
        return TokenUtils.getCurrentUser();
    }

}


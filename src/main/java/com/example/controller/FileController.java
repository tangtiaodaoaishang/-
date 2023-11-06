package com.example.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Constants;
import com.example.common.Result;
import com.example.mapper.FileMapper;
import com.example.pojo.Files;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

//文件上传相关接口

@RestController
@RequestMapping("/file")
public class FileController {
    @Value("${files.upload.path}")
    private  String fileUploadPath;
    @Autowired
    private FileMapper f;
    @Autowired
    private StringRedisTemplate r;

    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        //原始名称
        String originalFilename = file.getOriginalFilename();
        //获取文件类型
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();
        //先把文件存储到磁盘
        File uploadParentFile = new File(fileUploadPath);
        //判断该文件目录是否存在,如果不存在则创建一个新的文件目录
        if(!uploadParentFile.exists()){
            uploadParentFile.mkdirs();
        }
        //定义一个文件唯一的标识码
        String uuid = IdUtil.fastSimpleUUID();
        String fileUUID=uuid+StrUtil.DOT+type;

        File uploadFile = new File(fileUploadPath, fileUUID);
        String md5;
        String url;
        //把获取到的文件存到磁盘目录
        file.transferTo(uploadFile);
        //获取文件的md5
        md5 = SecureUtil.md5(uploadFile);
        //从数据库查询是否存在相同的记录
        Files dbFiles = getFileByMd5(md5);
        //获取文件的url
        if (dbFiles != null) {
            url = dbFiles.getUrl();
            //由于文件已存在,删除刚才上传的重复文件
            uploadFile.delete();

        } else {
            //数据库若不存在重复文件,则不删除刚才上传的文件
            url = "http://localhost:8082/file/" + fileUUID;
        }
        //存储到数据库
        Files files = new Files();
        files.setName(originalFilename);
        files.setType(type);
        files.setSize(size/1024); //转成kb单位
        files.setUrl(url);
        files.setMd5(md5);
        f.insert(files);
        flushRedis(Constants.FILES_KEY);
        return url;

    }

    //文件下载路径:"http://localhost:8082/file/{fileUUID}
    //文件下载接口
    @GetMapping("/{fileUUID}")
    public void download(@PathVariable String fileUUID, HttpServletResponse r) throws IOException {
        //根据文件唯一标识码获取文件
        File uploadFile = new File(fileUploadPath, fileUUID);
        //设置输出流的格式
        ServletOutputStream outputStream = r.getOutputStream();
        r.setContentType("application/octet-stream");
        r.addHeader("Content-Disposition","attachment;filename="+URLEncoder.encode(fileUUID,"UTF-8"));
        //读取文件字节流
        outputStream.write(FileUtil.readBytes(uploadFile));
        outputStream.flush();
        outputStream.close();

    }

    //通过文件数据库的md5查询文件
    private Files getFileByMd5(String md5){
        //查询文件的md5是否存在
        QueryWrapper<Files> q=new QueryWrapper<>();
        q.eq("md5",md5);
        List<Files> files = f.selectList(q);
        return files.size()==0?null:files.get(0);
    }
    //更新
    @GetMapping("/front/all")
    public Result frontAll(){
        //新增和修改操作
        return Result.success(f.selectList(null));
    }
    //更新
    @PostMapping("update")
    //清除缓存
//    @CachePut(value = "files",key="'frontAll'")
    public Result update(@RequestBody Files files){
        //新增和修改操作
        f.updateById(files);
        flushRedis(Constants.FILES_KEY);
        return Result.success();
//        return Result.success(f.selectList(null));
    }
    //单行查询操作
    @GetMapping("/detail/{id}")
    public Result getById(@PathVariable("id") Integer id){
        return Result.success(f.selectById(id));
    }

    //单行删除操作
    @DeleteMapping("/{id}")
    //清除一条缓存,key为要清除的数据
//    @CacheEvict(value = "files",key="'frontAll'")
    public Result delete(@PathVariable("id") Integer id){
        Files f1=f.selectById(id);
        f1.setIsDelete(true);
        f.updateById(f1);
        flushRedis(Constants.FILES_KEY);
        return Result.success();
    }
    //多行批量删除
    @PostMapping("/del/batch")
    public Result deleteBetch(@RequestBody List<Integer> ids){
          QueryWrapper<Files> q=new QueryWrapper<>();
          q.in("id",ids);
        List<Files> files = f.selectList(q);
        for(Files f1:files){
            //将Files实体类中的is_delete属性值改为1
            f1.setIsDelete(true);
            f.updateById(f1);
        }
        return Result.success();

    }
    //分页查询
    @GetMapping("page")
    public Result findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam(defaultValue = "") String name){
        IPage<Files> userIPage=new Page(pageNum,pageSize);
        QueryWrapper<Files> objectQueryWrapper = new QueryWrapper<>();
        //查询未删除的记录
        objectQueryWrapper.eq("is_delete",false);
        objectQueryWrapper.orderByDesc("id");
        if(!"".equals(name)){
            objectQueryWrapper.like("name",name);
        }
        IPage<Files> page = f.selectPage(userIPage,objectQueryWrapper);
        return Result.success(page);
    }

    //刷新(删除)redis缓存方法
    public void flushRedis(String key){
            r.delete(key);
    }
}

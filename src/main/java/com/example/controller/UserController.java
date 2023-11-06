package com.example.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Constants;
import com.example.common.Result;
import com.example.controller.dto.UserDTO;
import com.example.pojo.User;
import com.example.service.IUserService;
import com.example.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
/*   @Autowired
    private UserMapper u;*/
    @Autowired
    private UserServiceImpl u0;
    @Resource
    private IUserService u;

    //登录
    @PostMapping("/login")
    public Result login(@RequestBody UserDTO u){
        String username = u.getUsername();
        String password = u.getPassword();
        if(StrUtil.isBlank(username)||StrUtil.isBlank(password)){
           return Result.error(Constants.CODE_400,"参数错误");
        }
        UserDTO login = u0.login(u);
        return Result.success(login);
    }
    //注册
    @PostMapping("/register")
    public Result register(@RequestBody UserDTO u){
        String username = u.getUsername();
        String password = u.getPassword();
        if(StrUtil.isBlank(username)||StrUtil.isBlank(password)){
            return Result.error(Constants.CODE_400,"参数错误");
        }
        return Result.success(u0.register(u));
    }
    //个人信息页面添加
    @GetMapping("/username/{username}")
    public Result findOne(@PathVariable("username") String username){
        QueryWrapper<User> q=new QueryWrapper<>();
        q.eq("username",username);
        return Result.success(u0.getOne(q));
    }

    //导出数据
    @GetMapping("/export")
    public void export(HttpServletResponse r) throws Exception {
        List<User> l=u0.list();
        //通过工具类创建writer写出到磁盘路径
        //使用以下代码记得导入hutool依赖
        //ExcelWriter w=ExcelUtil.getWriter(filesUploadPath+"/用户信息.xlsx");
        //在内存中操作,写出到浏览器
        ExcelWriter w= ExcelUtil.getWriter(true);
        //自定义标题别名
        //注意这里推荐就不要取别名为中文,沿用数据库的英文列名即可,不然后续导入操作实现会很麻烦
      /*
        w.addHeaderAlias("username","用户名");
        w.addHeaderAlias("password","密码");
        w.addHeaderAlias("nickname","昵称");
        w.addHeaderAlias("email","邮箱");
        w.addHeaderAlias("phone","电话");
        w.addHeaderAlias("address","地址");
        w.addHeaderAlias("createTime","创建时间");
        w.addHeaderAlias("avatarUrl","头像");
*/
        //一次性写出list内的对象到excel,使用默认样式,强制输出标题
        w.write(l,true);
        //设置浏览器响应格式
        r.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName= URLEncoder.encode("用户信息","UTF-8");
        r.setHeader("Content-Disposition","attachment;filename="+fileName+".xlsx");
        ServletOutputStream outputStream = r.getOutputStream();
        w.flush(outputStream,true);
        outputStream.close();
        w.close();
    }
    //导入数据
    //MultipartFile file中的file属性名与前端body中file类型数据传的key值一样
    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        //方式一(推荐):这种通过javabean的方式读取Excel内的对象,但是要求表头必须是英文,跟javabean属性要对应起来
        //如果要求表头为中文的话,不太推荐因为实现方式太繁琐
        List<User> list = reader.readAll(User.class);
        u0.saveBatch(list);
        return Result.success(true);
    }

    //查询所有数据
    @GetMapping("select")
    public Result findAll(){
//        return u.findAll();
        return Result.success(u0.list());
    }
    @GetMapping("/role/{role}")
    public Result findUsersByRole(@PathVariable String role){
        QueryWrapper<User> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("role",role);
        List<User> list = u0.list(objectQueryWrapper);
        return Result.success(list);
    }
    //@RequestBody是将前端发送的json数据类型转为对应的java对象类型(这里是转为User对象)
    @PostMapping("insert")
    public Result save(@RequestBody User u1){
        //新增和修改操作
        return Result.success(u0.saveOrUpdate(u1));
    }
    //单行删除操作
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") Integer id){
//        return u.deleteById(id);
        return Result.success(u0.removeById(id));
    }
    //多行批量删除
    @PostMapping("/del/batch")
    public Result deleteBetch(@RequestBody List<Integer> id){
//        return u.deleteById(id);
        return Result.success(u0.removeByIds(id));
    }
    //分页查询
    //@RequestParam注解相当于前端请求路径最后加上?变量名=变量值......
    //limit的第一个参数公式为(pageNum-1)*pageSize
    //mybatis方式
  /*  @GetMapping("page")
    public Map findPage(@RequestParam Integer pageNum,@RequestParam Integer pageSize,@RequestParam String username){
        pageNum=(pageNum-1)*pageSize;
       List<User> data =u.selectPage(pageNum,pageSize,username);
        Integer total = u.selectTotal(username);
        Map<String,Object> m=new HashMap<>();
        m.put("data",data);
        m.put("total",total);
        return m;
    }*/

   @GetMapping("front/page")
   public Result findAllPage(){
      return Result.success(u.list());
   }
    //mybatis-plus方式
    //@RequestParam(defaultValue = "")中使用到了默认值,即前端不传该注解定义的属性值也不会报404错,因为该属性又指定的默认值
    @GetMapping("page")
    public Result findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam(defaultValue = "") String username,@RequestParam(defaultValue = "") String email,@RequestParam(defaultValue = "") String address){
/*
        IPage<User> userIPage=new Page<User>(pageNum,pageSize);
        QueryWrapper<User> objectQueryWrapper = new QueryWrapper<>();
        if(!"".equals(username)){
            objectQueryWrapper.like("username",username);
        }
        if(!"".equals(email)){
            objectQueryWrapper.like("email",email);
        }
        if(!"".equals(address)){
            objectQueryWrapper.like("address",address);
        }
        objectQueryWrapper.orderByDesc("id");

        IPage<User> page = u0.page(userIPage,objectQueryWrapper);

        //在该请求里获取当前登录的用户信息
        User u=TokenUtils.getCurrentUser();
        System.out.println("当前登录的用户信息:"+u.getNickname());
*/
//        return Result.success(page);
        return Result.success(u.findPage(new Page<>(pageNum,pageSize),username,email,address));
    }
}

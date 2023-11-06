package com.example.utils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;

import java.util.Collections;
//逆向工程配置类
//注意:重点不要运行该类
//这个类不要直接运行,需要备份好相关的代码(比如controller和service层和xml文件等代码,否则会导致所有代码刷新重新生成)
public class CodeGenerate {
    public static void main(String[] args) {
        generate();
    }
    private static void generate(){
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/ldjc?serverTimezone=GMT%2b8", "root", "210210")
                .globalConfig(builder -> {
                    builder.author("baomidou") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("D:\\项目\\springboot-reverse-engineering\\src\\main\\java\\"); // 指定输出目录
                })

                .packageConfig(builder -> {
                    builder.parent("com.example") // 设置父包名
                            .moduleName(null) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "D:\\项目\\springboot-reverse-engineering\\src\\main\\resources\\com\\example\\controller")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.entityBuilder().enableLombok(); //对自动生成的pojo实体类用lombok简化开发
                    builder.mapperBuilder().enableMapperAnnotation().build(); //对mapper层的接口类添加@Mapper注解
                    builder.controllerBuilder().enableHyphenStyle()  // 为controller层类开启驼峰转连字符,应用在controller层类中方法的请求路径上带有下划线自动驼峰转连
                            .enableRestStyle();  // 开启生成@RestController(控制器)注解,这个属性必须加上不然会访问不到请求路径

                    builder.addInclude("") // 设置需要生成的表名
                            .addTablePrefix("t_", "sys_"); // 设置过滤表前缀
                })
//                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板

                .execute();

    }
}

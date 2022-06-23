package com.czy.springcloud.controller;

import com.czy.springcloud.pojo.Dept;
import com.czy.springcloud.service.DeptService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/dept")
@RestController
public class DeptController {

    @Autowired
    DeptService deptService;
    //正常的请求代码
    @RequestMapping("/get/{id}")
    //指明熔断的备选方法是哪一个
    @HystrixCommand(fallbackMethod = "erroGetByNo")
    public Dept getDeptByNo(@PathVariable("id") Long id){
        Dept dept= deptService.findDeptByNo(id);
        //传入的id不存在，我们查询不到对应的信息我们就抛出一个异常
        if(dept==null){
            throw new RuntimeException("没有id为:"+id+"的部门信息");
        }
        return dept;
    }
    //发生错误之后的备选程序也就是熔断方法
    public Dept erroGetByNo(@PathVariable("id") Long id){
        //当我们查询不到的时候我们就返回给他一个错误报告的Dept对象
        return new Dept().setDeptno(id)
                        .setDeptname("id->"+id+",没有对应的部门信息")
                        .setDb_source("没有对应的数据库信息！");
    }

}

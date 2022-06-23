package com.czy.springcloud.controller;

import com.czy.springcloud.pojo.Dept;
import com.czy.springcloud.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/dept")
@RestController
public class DeptController {

    @Autowired
    DeptService deptService;

    @RequestMapping("/get/{id}")
    public Dept getDeptByNo(@PathVariable("id") Long id){
        return deptService.findDeptByNo(id);
    }
    @RequestMapping("/getall")
    public List<Dept> getAllDept(){
        return deptService.findAllDept();
    }
    @RequestMapping("/del/{id}")
    public Boolean delDept(@PathVariable("id") Long id){
        return deptService.delDept(id);
    }
    @RequestMapping("/add")
    public Boolean addDept(@RequestBody Dept dept){
        return deptService.addDept(dept);
    }
    @RequestMapping("/test")
    public String test(){
        return "test";
    }
}

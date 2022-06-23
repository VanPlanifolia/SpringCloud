package com.czy.springcloud.controller;

import com.czy.springcloud.pojo.Dept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.util.List;

/**
 * 消费者的controller
 */
@RestController
public class DeptConsumerController {
    //自动注入RestTemplate
    @Autowired
    private RestTemplate restTemplate;
    public static final String REQUESTURL="http://SPRINGCLOUD-PROVIDER-DEPT/";

    /**
     * 按id查询的controller
     * @param deptno 部门编号
     * @return 一个部门信息
     */
    @RequestMapping("/dept/consumer/get/{deptno}")
    public Dept get(@PathVariable("deptno") Long deptno){
        return restTemplate.getForObject(REQUESTURL+"/dept/get/"+deptno,Dept.class);
    }

    /**
     * 查询全部信息
     * @return 全部部门信息
     */
    @RequestMapping("/dept/consumer/getall")
    public List<Dept> getAll(){
        return restTemplate.getForObject(REQUESTURL+"/dept/getall",List.class);
    }

    /**
     * 添加一个部门
     * @param dept 部门信息
     * @return 添加结果
     */
    @RequestMapping("/dept/consumer/add")
    public boolean add(@RequestBody Dept dept){
        return restTemplate.getForObject(REQUESTURL+"/dept/add",boolean.class);
    }
}

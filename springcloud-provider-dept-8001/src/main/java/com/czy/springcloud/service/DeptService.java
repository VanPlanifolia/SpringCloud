package com.czy.springcloud.service;

import com.czy.springcloud.pojo.Dept;
import java.util.List;

/**
 * Dept的service层
 */

public interface DeptService {
    //按no查询
    Dept findDeptByNo(Long deptno);
    //查询全部
    List<Dept> findAllDept();
    //添加一个部门
    boolean addDept(Dept dept);
    //删除一个部门
    boolean delDept(Long deptno);
}

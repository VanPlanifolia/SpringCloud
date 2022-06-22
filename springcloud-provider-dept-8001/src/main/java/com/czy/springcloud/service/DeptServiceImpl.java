package com.czy.springcloud.service;

import com.czy.springcloud.dao.DeptDao;
import com.czy.springcloud.pojo.Dept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DeptServiceImpl implements DeptService{
    @Autowired
    private DeptDao deptDao;

    @Override
    public Dept findDeptByNo(Long deptno) {
        return deptDao.findDeptByNo(deptno);
    }

    @Override
    public List<Dept> findAllDept() {
        return deptDao.findAllDept();
    }

    @Override
    public boolean addDept(Dept dept) {
        return deptDao.addDept(dept);
    }

    @Override
    public boolean delDept(Long deptno) {
        return deptDao.delDept(deptno);
    }
}

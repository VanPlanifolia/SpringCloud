package com.czy.springcloud.dao;

import com.czy.springcloud.pojo.Dept;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 使用mapper整合的方式，这里是dao接口，提供了添加，查询，删除的操作
 */
@Mapper
@Repository
public interface DeptDao {
    /**
     * 根据部门的编号查询部门信息
     * @param deptno 部门编号
     * @return Dept部门信息
     */
    Dept findDeptByNo(Long deptno);

    /**
     * 查询全部的部门
     * @return 全部的部门
     */
    List<Dept> findAllDept();

    /**
     * 添加一个部门
     * @return 是否添加成功
     */
    boolean addDept(Dept dept);

    /**
     * 根据编号删除某一个部门
     * @param deptno 部门编号
     * @return 是否删除成功
     */
    boolean delDept(Long deptno);
}

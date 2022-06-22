package com.czy.springcloud.pojo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Accessors(chain = true)//开启链式编程
public class Dept implements Serializable {

    private Long deptno;
    private String deptname;
    //用于注明这个数据的服务器的服务器是哪一个
    private String db_source;

    Dept(String deptname){
        this.deptname=deptname;
    }
}

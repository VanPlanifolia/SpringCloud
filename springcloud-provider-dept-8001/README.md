## springcloud微服务中的生产者模块

总览：对于一个系统我们必然会有生产者与消费者模块，生产者提供服务，消费者使用服务，之前在没有引入微服务之前我们生产者模块与消费者模块都是在一个系统中的，由于都在本地所以不要去考虑调用的问题，只需要引入本地的类就可以了。而在我们引入了微服务生产者与消费者被分成了不同的组件，那么我们首先要解决的问题就是各个服务之间的调用问题。

1. 搭建数据库，对于一个模块的开始必然是搭建数据库，我们编写的是一个部门管理的模块案例，数据库就为部门信息，包括部门编号作为主键，部门名称，数据库名称，然后编写pojo类，奥这些工作已经在api模块中完成了。具体代码不再粘贴
2. 编写基本业务逻辑，在生产者模块中我们需要做的就是提供服务，所以我们需要在生产者中编写dao，service，这一套，而本模块中本不应该有controller模块但是作为本地测试用。首先就是dao层的编写就像传统的dao层一样，使用mapper整合的方式编写mapper接口绑定对应的mapper文件。

* 2.1 编写dao接口（mapper接口），dao接口中添加了按照id查询，查询全部，添加，删除的四个功能，就是传统的mapper整合具体功能如下。

```java
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
```

* 2.2 编写完毕mapper接口之后应该就是编写mapper文件了，在里面完成sql语句的编写，同样也是要注意命名空间，id，传入传出参数与mapper接口一一对应。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.czy.springcloud.dao.DeptDao">

    <select id="findDeptByNo" parameterType="Long" resultType="dept">
        select * from dept where deptno=#{deptno}
    </select>
    <select id="findAllDept" resultType="dept">
        select * from dept
    </select>
    <insert id="addDept" parameterType="dept">
        insert into dept(deptname, db_source)
        VALUES(#{deptname},DATABASE())
    </insert>
    <update id="delDept" parameterType="Long">
        delete from dept where deptno=#{deptno}
    </update>
</mapper>
```

* 2.3 随后我们要创建application.yaml配置文件，来完成springboot的一些配置，配置数据源，端口号等等等等

```yaml
# 配置端口
server:
  port: 8001
# 配置mybatis的配置信息以及包扫描，mapper文件等等
mybatis:
  type-aliases-package: com.czy.springcloud.pojo
  mapper-locations: classpath:mybatis/mapper/*.xml

# spring与数据源的配置
spring:
  application:
    # 指明这个服务的名字
    name: springcloud-provider-dept
  datasource:
    # 使用德鲁伊的数据源
    type: com.alibaba.druid.pool.DruidDataSource
    # 加载驱动，url，用户账户与密码
    driver-class-name: org.gjt.mm.mysql.Driver
    url: jdbc:mysql://localhost:3306/db1?useUnicode=true&characterEncoding=utf-8
    username: root
    password: '010713'
```

* 2.4 dao层完成之后就要开始service层的编写了，service层就是对dao层的进一步封装，和传统的写法一样就不再赘述直接上代码

```java
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
```

```java
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
```

* 2.5 编写完service层之后其实我们需要编写一下服务端的controiller层，这层controller用于给消费者的controller来调用，controller层全部返回json信息所以直接使用@RestController注解而且所有的参数访问都采用restFul的风格

```java
import com.czy.springcloud.pojo.Dept;
import com.czy.springcloud.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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
    public Boolean addDept(Dept dept){
        return deptService.addDept(dept);
    }
    @RequestMapping("/test")
    public String test(){
        return "test";
    }
}
```

* 2.6 最后就是springboot的启动类编写了，固定模板没什么好说的直接上代码

```java
package com.czy.springcloud;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 */
@SpringBootApplication
public class DeptProvider_8001 {
    public static void main(String[] args) {
        SpringApplication.run(DeptProvider_8001.class,args);
    }
}
```

3.我们在springcloud-eureka-7001模块中已经吧eureka的服务端搭建好了，然后我们只需要吧我们之前写的客户端有也就是提供者模块注册到，eureka-service中就ok了，注册到eureka中其实很简单，只需要在yaml文件中编写服务端的对应地址，以及指明这个需要注册服务的名字，然后在从maven中引入eureka依赖，最后就是在启动类中添加注解。

```yaml
# eureka配置，我们要将服务注册到哪里
eureka:
  client:
    service-url:
      defaultZone: http://localhost:7001/eureka/
  # 配置他的id也就是默认描述信息
  instance:
    instance-id: springcloud-provider-8001
info:
  app.name: springcloud-probider-dept
  author: Planifolia.Van
```

```xml
        <!--starter-eureka -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-eureka</artifactId>
            <version>1.4.6.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
```

4.**我们想要引入ribbon来作为负载均衡的工具**，首先我们就需要创建多个提供者，如项目中我们按照8001的模板创建了三个内容提供者端口号分为8001，8002，8003，然后这三个服务都会注册到我们的注册中心中，然后ribbon根据内部的负载均衡算法来选取使用哪一个内容提供者，引入了三个内容提供者自然需要三个数据库。具体步骤如下

* 4.1 创建三个数据库db1 db2 db3，作为三个内容提供者的链接目标，可以使用我们预先创建好的db1作为数据源，具体内容在下面不再粘贴了
* 4.2 按照8001的模板创建8002，8003，大部分内容只要复制粘贴即可，只需要修改yaml配置文件，修改配置文件中的端口号，数据库链接的位置，服务的id，这样这些提供者在启动的时候都会自动的注册到eureka注册中心中。

```yaml
application:
    # 指明这个服务的名字
    name: springcloud-provider-dept
  datasource:
    # 使用德鲁伊的数据源
    type: com.alibaba.druid.pool.DruidDataSource
    # 加载驱动，url，用户账户与密码
    driver-class-name: org.gjt.mm.mysql.Driver
    url: jdbc:mysql://localhost:3306/db1?useUnicode=true&characterEncoding=utf-8
    username: root
    password: '010713'
# eureka配置，我们要将服务注册到哪里
eureka:
  client:
    service-url:
      defaultZone: http://eureka7002.com:7002/eureka/,http://eureka7003.com:7003/eureka/,http://eureka7001.com:7001/eureka/
  # 配置他的id也就是默认描述信息
  instance:
    instance-id: springcloud-provider-8001
info:
  app.name: springcloud-probider-dept
  author: Planifolia.Van
```

```yaml
# spring与数据源的配置
spring:
  application:
    # 指明这个服务的名字
    name: springcloud-provider-dept
  datasource:
    # 使用德鲁伊的数据源
    type: com.alibaba.druid.pool.DruidDataSource
    # 加载驱动，url，用户账户与密码
    driver-class-name: org.gjt.mm.mysql.Driver
    url: jdbc:mysql://localhost:3306/db2?useUnicode=true&characterEncoding=utf-8
    username: root
    password: '010713'
# eureka配置，我们要将服务注册到哪里
eureka:
  client:
    service-url:
      defaultZone: http://eureka7002.com:7002/eureka/,http://eureka7003.com:7003/eureka/,http://eureka7001.com:7001/eureka/
  # 配置他的id也就是默认描述信息
  instance:
    instance-id: springcloud-provider-8002
info:
  app.name: springcloud-probider-dept
  author: Planifolia.Van
```

```yaml
# spring与数据源的配置
spring:
  application:
    # 指明这个服务的名字
    name: springcloud-provider-dept
  datasource:
    # 使用德鲁伊的数据源
    type: com.alibaba.druid.pool.DruidDataSource
    # 加载驱动，url，用户账户与密码
    driver-class-name: org.gjt.mm.mysql.Driver
    url: jdbc:mysql://localhost:3306/db3?useUnicode=true&characterEncoding=utf-8
    username: root
    password: '010713'
# eureka配置，我们要将服务注册到哪里
eureka:
  client:
    service-url:
      defaultZone: http://eureka7002.com:7002/eureka/,http://eureka7003.com:7003/eureka/,http://eureka7001.com:7001/eureka/
  # 配置他的id也就是默认描述信息
  instance:
    instance-id: springcloud-provider-8003
info:
  app.name: springcloud-probider-dept
  author: Planifolia.Van
```

* 4.3 别忘记修改启动类，然后接下来就是修改80端口里面的内容，具体见dept-80的readme文件

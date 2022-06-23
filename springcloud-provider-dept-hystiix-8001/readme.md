## springcloud熔断机制的实现

综述：企业级分布式项目中熔断机制可以说是必不可少的，因为在微服务架构中某一个服务模块的故障很可能会导致全盘雪崩式的故障产生，所以我们需要引入熔断机制在某一个模块发生故障时立即返回一个错误信息而不是在这个模块中抛出异常导致这个模块崩溃。

1.使用hystix来进行服务熔断，首先需要引入maven依赖，然后根据8001模块的模板建造hystix-8001模块，然后修改yaml文件修改它的id，以及注册服务的地址，最后就是在controller层中引入熔断处理模块，为了节省时间我们只做了一个错误的处理。

* 1.1 引入maven依赖，没啥好说的去maven官网查询如下代码

```xml
<dependencies>
        <!--starter-hystrix -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-hystrix</artifactId>
            <version>1.4.6.RELEASE</version>
        </dependency>
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
        <!--通过maven引入springcloud-api-->
        <dependency>
            <groupId>com.czy</groupId>
            <artifactId>springcloud-api</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
        <!--junit的依赖-->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <scope>test</scope>
        </dependency>
        <!--mysql的依赖-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <!--德鲁伊数据源的依赖-->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
        </dependency>
        <!--mybatis的依赖-->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
        </dependency>
        <!--springboot-web的依赖-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>
```

* 1.2 按照8001的模板创建hystix-8001模板，编写controller，service，引入mybaits，编写mapper接口与mapper文件，具体代码见模块不再粘贴了；
* 1.3 修改yaml文件，在yaml文件中我们要修改这个hystix-8001的id信息并且指明它的注册中心地址

```yaml
# eureka配置，我们要将服务注册到哪里
eureka:
  client:
    service-url:
      defaultZone: http://eureka7002.com:7002/eureka/,http://eureka7003.com:7003/eureka/,http://eureka7001.com:7001/eureka/
  # 配置他的id也就是默认描述信息
  instance:
    instance-id: springcloud-provider-hystix-8001
info:
  app.name: springcloud-probider-dept
  author: Planifolia.Van
```

* 1.4 修改controller层的文件添加错误处理方法以及引入hystix熔断处理

```java
import com.czy.springcloud.pojo.Dept;
import com.czy.springcloud.service.DeptService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
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

```

* 1.5 修改启动类启动hystix服务

```java
@SpringBootApplication
//声明这是一个eureka的客户端，启动之后会将这个服务注册到eureka中
@EnableEurekaClient
//在springboot中开启熔断机制
@EnableHystrix
public class DeptProviderHystix_8001 {
    public static void main(String[] args) {
        SpringApplication.run(DeptProviderHystix_8001.class,args);
    }
}
```

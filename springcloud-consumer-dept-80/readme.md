## springcloud微服务中的消费者模块

总览：对于消费者，我们只有一个任务就是去消费生产者生产出来的服务就可以了，所以我们消费者就只需要编写controller，去调用远程的生产者服务就ok了，其中最重要的问题就是我们如何去远程调用生产者的服务。这就需要用到我们springcloud中的 RestTemplate了使用http协议来完成调用。

1.编写配置文件，由于这是消费者服务，我们需要通过浏览器去访问那么我们就可以直接使用80端口，由于我们这个服务就是用来访问生产者的所以就需要这一个配置，而对于maven配置文件我们只需要引入springweb的启动与API模块中的实体类即可。

```yaml
server:
  port: 80
```

```xml
 <dependencies>
        <!--实体类的依赖-->
        <dependency>
            <groupId>com.czy</groupId>
            <artifactId>springcloud-api</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
        <!--springweb的依赖-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>
```

2.编写BeanConig，由于我们需要用到RestTemplate，我们需要将他交给spring来托管，所以需要编写配置类来配置bean

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * bean的Config
 */
@Configuration
public class BeanConfig {
    /**
     * 把RestTemplate交给spring来托管
     */
    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
```

3.编写消费者的controller，在这个controller中我们需要去远程的调用（通过http协议）生产者中的controller，需要使用RestTemplate，注意远程调用的url与参数。

```java
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
    public static final String REQUESTURL="http://127.0.0.1:8001/";
  
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

```

4. 编写启动类进行测试

```java
/**
 * 启动类
 */
@SpringBootApplication
public class DeptConsumer_80 {
    public static void main(String[] args) {
        SpringApplication.run(DeptConsumer_80.class,args);
    }
}
```

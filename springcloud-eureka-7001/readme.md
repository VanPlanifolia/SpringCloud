## Eureka服务器的配置

概述：erueka本质上是和zookeeper类似的一个东西，他也是注册中心，使用erueka的话所有的提供者服务都会注册到erueka中，然后消费者会到erueka中去取这些服务。

* Springcloud 封装了Netflix公司开发的Eureka模块来实现服务注册与发现 (对比Zookeeper).
* Eureka采用了C-S的架构设计，EurekaServer作为服务注册功能的服务器，他是服务注册中心.
* 而系统中的其他微服务，使用Eureka的客户端连接到EurekaServer并维持心跳连接。这样系统的维护人员就可以通过EurekaServer来监控系统中各个微服务是否正常运行，Springcloud 的一些其他模块 (比如Zuul) 就可以通过EurekaServer来发现系统中的其他微服务，并执行相关的逻辑.

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200521130157770.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MzU5MTk4MA==,size_16,color_FFFFFF,t_70#pic_center)

* Eureka 包含两个组件：Eureka Server 和 Eureka Client.
* Eureka Server 提供服务注册，各个节点启动后，回在EurekaServer中进行注册，这样Eureka Server中的服务注册表中将会储存所有课用服务节点的信息，服务节点的信息可以在界面中直观的看到.
* Eureka Client 是一个Java客户端，用于简化EurekaServer的交互，客户端同时也具备一个内置的，使用轮询负载算法的负载均衡器。
* 在应用启动后，将会向EurekaServer发送心跳 (默认周期为30秒) 。如果Eureka Server在多个心跳周期内没有接收到某个节点的心跳，EurekaServer将会从服务注册表中把这个服务节点移除掉 (默认周期为90s).

Eureka的三大角色：

* Eureka Server：提供服务的注册与发现
* Service Provider：服务生产方，将自身服务注册到Eureka中，从而使服务消费方能狗找到
* Service Consumer：服务消费方，从Eureka中获取注册服务列表，从而找到消费服务

1.本模块是EurekaClient，他的主要功能就是Eureka的管理界面，我们在这里面可以看到已经注册到Eureka中的一些服务。配置这个模块相当的简单只需要去引入pom依赖和application.yaml文件配置就行了，注释在代码中

```xml
 <dependencies>
        <!--spring-cloud-starter-eureka-server -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-eureka-server</artifactId>
            <version>1.4.6.RELEASE</version>
        </dependency>

    </dependencies>
```

```yaml
# 服务的端口号
server:
  port: 7001
# eureka的配置
eureka:
  instance:
    # 主机名
    hostname: localhost
  client:
    # 是否注册自己
    register-with-eureka: false
    # 声明自己是注册中心
    fetch-registry: false
    # eureka的访问地址
    service-url:
      defaltZone: "http://${eureka.instance.hostname}:${server.port}/eureka/"
```

2.然后我们只需要访问http://localhost:7001就可以进入到管理页面了，奥对别忘了添加启动类。

3.eureka的集群配置，在实际开发中我们不可能只使用一个eureka作为服务注册中心，我们肯定要配置eureka集群，具体的配置方法也很简单，我们只需要修改yaml文件就可以了，例如我们创建三个eureka作为注册中心集群，分别为eureka7001，eureka7002，eureka7003，然后我们在yaml文件中关联另外的两个eureka注册中心，然后在提供者中将服务注册到这三个注册中心即可完成eureka集群的配置。

eureka7001

```yaml
# 服务的端口号
server:
  port: 7001
# eureka的配置
eureka:
  instance:
    # 主机名
    hostname: eureka7001.com
  client:
    # 是否注册自己
    register-with-eureka: false
    # 声明自己是注册中心
    fetch-registry: false
    # eureka的访问地址
    service-url:
      # 单机模式下
#      defaltZone: http://${eureka.instance.hostname}:${server.port}/eureka/
      # 集群模式下
      defaltZone: http://eureka7002.com:7002/eureka/,http://eureka7003.com:7003/eureka/
```

eureka7002

```yaml
# 服务的端口号
server:
  port: 7002
# eureka的配置
eureka:
  instance:
    # 主机名
    hostname: eureka7002.com
  client:
    # 是否注册自己
    register-with-eureka: false
    # 声明自己是注册中心
    fetch-registry: false
    # eureka的访问地址
    service-url:
#      defaltZone: http://${eureka.instance.hostname}:${server.port}/eureka/
      defaltZone: http://eureka7001.com:7001/eureka/,http://eureka7003.com:7003/eureka/
```

eureka7003

```yaml
# 服务的端口号
server:
  port: 7003
# eureka的配置
eureka:
  instance:
    # 主机名
    hostname: eureka7003.com
  client:
    # 是否注册自己
    register-with-eureka: false
    # 声明自己是注册中心
    fetch-registry: false
    # eureka的访问地址
    service-url:
#      defaltZone: http://${eureka.instance.hostname}:${server.port}/eureka/
      defaltZone: http://eureka7002.com:7002/eureka/,http://eureka7001.com:7001/eureka/
```

然后将提供者注册到这三个服务中心

```yaml
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

```

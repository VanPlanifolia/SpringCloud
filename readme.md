## SpringCloud形式的微服务架构总览

**1.何为微服务架构与微服务？**

**总体概括**

微服务架构是一种新型的架构模式，他一改传统的All In One的风格，将一个整体的程序模块分割成一组组服务，每个服务作为一个进程在运行并且能够部署到不同的运行环境中，甚至可以使用不同的数据库，然后各个服务之间通过HTTP协议（在springcloud中）互相调用访问通信，进而完成一个系统的整体业务。

微服务强调的是服务的大小，它关注的是某一个点，是具体解决某一个问题/提供落地对应服务的一个服务应用，狭义的看，可以看作是IDEA中的一个个微服务工程，或者Moudel。IDEA 工具里面使用Maven开发的一个个独立的小Moudel，它具体是使用SpringBoot开发的一个小模块，专业的事情交给专业的模块来做，一个模块就做着一件事情。强调的是一个个的个体，每个个体完成一个具体的任务或者功能。

**从技术维度上**

微服务化的核心就是将传统的一站式应用，根据业务拆分成一个一个的服务，彻底地去耦合，每一个微服务提供单个业务功能的服务，一个服务做一件事情，从技术角度看就是一种小而独立的处理过程，类似进程的概念，能够自行单独启动或销毁，拥有自己独立的数据库。

2.微服务架构的四个核心问题

* 微服务有很多，客户如何去访问这些服务（api网关）
* 微服务有很多，服务之间应该怎么通信（http通信，rpc通信）
* 微服务有很多，应该怎么统一管理（服务注册与发现）
* 服务挂了怎么办（服务的熔断机制）

这一套核心问题的解决方案就是springcloud，他是一种生态是基于springboot的
## Nobita-RPC远程调用框架
该项目是一个基于Netty的JAVA远程调用框架
### Future
- 基于接口动态代理实现，使远程调用细节对用户透明
- 底层网络IO使用No-Blocking-IO，在高并发情况下仍然能保证良好性能
- 在框架应用层做了异步转同步处理，使得用户可以同步调用服务
- 实现心跳功能以及断线重连机制
- 远程调用过程的对象传输全部序列化成二进制，提高了传输速率
- 支持Nacos作为注册中心

### 主要技术栈
- JDK1.8
- Maven
- JDK动态代理
- Netty
- Hessian
- Gson



### 项目架构

![hhh _1_.png](https://i.loli.net/2020/07/10/vSzrETHt3GC1a98.png)

### 使用方法

#### 定义接口

```java
public interface HelloService {

    String sayHello();

    String sayHi(String name);

}
```

#### 服务端

- 编写接口实现类

  ```java
  public class HelloServiceImpl implements HelloService {
  
      @Override
      public String sayHello() {
          return "hello! everyone";
      }
  
      @Override
      public String sayHi(String name) {
          return "hello! my dear "+name;
      }
  }
  ```

  

- 编写配置文件
```
{
  "side": "server",
  "serverHost": "localhost",
  "serverPort": 7412,
  "register": {
    "name": "nacos",
    "addr": "sz.hiboy.fun:8848"
  },
  "services": [
    {
      "interface_name": "org.warren.nobita.server.test.HelloService",
      "implement_name": "org.warren.nobita.server.test.HelloServiceImpl",
      "lazy": false
    }
  ]
}
```

- 编写服务端启动类

  ```
  //通过json配置文件初始化Nobita-RPC服务端配置
  ServerConfig config = new JsonServerConfigReader("/user/nobita-config.json").load();
  
  //通过配置信息构建服务端上下文
  NobitaServerContext context = new NobitaServerContext(config);
  
  //构造服务端调用处理器
  ServiceInvocationHandler handler = new ServiceInvocationHandler(context);
  NobitaNettyServer nettyServer = new NobitaNettyServer(handler);
  
  //启动服务端
  nettyServer.start();
  ```

#### 客户端

- 编写客户端配置文件

  ```
  {
    "register": {
      "type": "nacos",
      "addr": "sz.hiboy.fun:8848"
    }
  }
  ```

  

- 编写客户端类

  ```
  NobitaClientContext clientContext = new NobitaClientContext("/user/nobita-client-config.json");
  NobitaProxyFactory proxyFactory = clientContext.getProxyFactory();
  HelloService service = proxyFactory.createService(HelloService.class);
  System.out.println(service.sayHi("warren"));
  System.out.println(service.sayHi("jacky"));
  System.out.println(service.sayHi("mary"));
  System.out.println(service.sayHi("walton"));
  ```

  


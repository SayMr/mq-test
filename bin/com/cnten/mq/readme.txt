一、
1、test01: 发布订阅模式 一个发布者、一个订阅者  发送N条消息，接收<=N条消息
2、test02: p2p队列模式 一个发送者、一个接收者   发送N条消息，接收N条消息
3、test03: 发布订阅模式  一个发布者、多个订阅者  发送N条消息  接收总消息X,每个订阅者都可以接收<=N条消息(取决与发布消息时各订阅者是否在线)
4、test04: p2p队列模式 一个发送者、多个接收者  发送N条消息   共接收N条消息 （不重复）
5、test05: p2p队列模式多个发送者、多个接收者   发送N条消息   共接收N条消息（不重复）
6、test06: p2p队列模式一个发送者、多个接收者  接收者设置消息选择器，只接收特定的消息
7、test07: Activemq支持一下三种模式：
  Session.AUTO_ACKNOWLEDGE  消息自动签收
  Session.CLIENT_ACKNOWLEDGE  客户端调用acknowledge方法手动签收
  Session.DUPS_OK_ACKNOWLEDGE 不必必须签收，消息可能会重复发送。在第二次重新传递消息的时候，消息头的JmsDelivered会被置为true标示当前消息已经传送过一次，客户端需要进行消息的重复处理控制。
8、消息协议
9、持久化消息和非持久化消息   不论使用p2p模式还是发布/订阅模式，非持久化消息在服务节点宕机重启后都会丢失






1、JMS Queue执行load balancer语义：
一条消息仅能被一个consumer收到。如果在message发送的时候没有可用的consumer，那么它将被保存一直到能处理该message的consumer可用。如果一个consumer收到一条message后却不响应它，那么这条消息将被转到另一个consumer那儿。一个Queue可以有很多consumer，并且在多个可用的consumer中负载均衡。

2、Topic实现publish和subscribe语义：
一条消息被publish时，它将发到所有感兴趣的订阅者，所以零到多个subscriber将接收到消息的一个拷贝。但是在消息代理接收到消息时，只有激活订阅的subscriber能够获得消息的一个拷贝。

3、分别对应两种消息模式：
Point-to-Point (点对点),Publisher/Subscriber Model (发布/订阅者)
其中在Publicher/Subscriber 模式下又有Nondurable subscription（非持久订阅）和durable subscription (持久化订阅)2种消息处理方式。
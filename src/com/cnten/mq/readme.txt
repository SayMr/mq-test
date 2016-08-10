test01: 发布订阅模式 一个发布者、一个订阅者  发送N条消息，接收<=N条消息
test02: p2p队列模式 一个发送者、一个接收者   发送N条消息，接收N条消息
test03: 发布订阅模式  一个发布者、多个订阅者  发送N条消息  接收总消息X,每个订阅者都可以接收<=N条消息(取决与发布消息时各订阅者是否在线)
test04: p2p队列模式 一个发送者、多个接收者  发送N条消息   共接收N条消息 （不重复）
test05: p2p队列模式多个发送者、多个接收者   发送N条消息   共接收N条消息（不重复）
test06: p2p队列模式一个发送者、多个接收者  接收者设置消息选择器，只接收特定的消息


1、JMS Queue执行load balancer语义：
一条消息仅能被一个consumer收到。如果在message发送的时候没有可用的consumer，那么它将被保存一直到能处理该message的consumer可用。如果一个consumer收到一条message后却不响应它，那么这条消息将被转到另一个consumer那儿。一个Queue可以有很多consumer，并且在多个可用的consumer中负载均衡。

2、Topic实现publish和subscribe语义：
一条消息被publish时，它将发到所有感兴趣的订阅者，所以零到多个subscriber将接收到消息的一个拷贝。但是在消息代理接收到消息时，只有激活订阅的subscriber能够获得消息的一个拷贝。

3、分别对应两种消息模式：
Point-to-Point (点对点),Publisher/Subscriber Model (发布/订阅者)
其中在Publicher/Subscriber 模式下又有Nondurable subscription（非持久订阅）和durable subscription (持久化订阅)2种消息处理方式。
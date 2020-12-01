## Introduction

This project provides support for receiving [NetFlow](https://en.wikipedia.org/wiki/NetFlow) data from network devices
using [Netty](http://netty.io). Currently only [NetFlow v9](https://en.wikipedia.org/wiki/NetFlow#NetFlow_and_IPFIX) is supported. There are plans to 
support [NetFlow v5](https://en.wikipedia.org/wiki/NetFlow#NetFlow_Versions) in a future release. 

## Usage

### Pipeline Configuration

```java
Bootstrap b = new Bootstrap();
b.group(bossGroup)
    .channel(NioDatagramChannel.class)
    .handler(new ChannelInitializer<DatagramChannel>() {
      @Override
      protected void initChannel(DatagramChannel datagramChannel) throws Exception {
        ChannelPipeline channelPipeline = datagramChannel.pipeline();
        channelPipeline.addLast(
            new LoggingHandler("NetFlow", LogLevel.TRACE),
            new NetFlowV9Decoder(),
            new NetFlowV9RequestHandler()
        );
      }
    })
    .bind(2055) // or whatever the port number
    .sync();    // or skip `.sync()`, if you don't want to wait here until your server is up and running
;
```

### NetFlow Message Processing

```java
class NetFlowV9RequestHandler extends SimpleChannelInboundHandler<NetFlowV9Decoder.NetFlowMessage> {
  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, NetFlowV9Decoder.NetFlowMessage netFlowMessage) throws Exception {

  }
}
```

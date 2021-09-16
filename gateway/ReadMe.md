# A UDP Gateway, now it just opens one port to receive UDP packet.
# 1. Components
## 1.1 decoder folder contains decoders
Can implement Decoder do use for decode. Input is a Bytebuf and output is a Map<String, Object>
With flat message, we just need FlatMessageDecoder, which will read dataType of packet first. Then, base on schema
of this dataType it will parse the packet to Map<String, Object>
## 2.2 business folder contains business handler
Can implement BusinessHandler to add business logic. 
Business handler will receive decoded value from decoder to process.
Now, the folder just have a simple handler, which will print out the decoded data.
## 2.3 schema folder contains schema provider for flat message
Can implement SchemaProvider to specify source, storage for schema.
Now, it has just one simple implementation, which will read schema from a yaml file.
Schema has a list of field. Field contains information need to decode data in udp packet.
Field has: 
* name: name of field
* dataType: text or number
* length: length of field in udp paclet
## 2.4 MessageRouter
Which will receive decoded data. Base on data type of this date(dataType field), router will call right 
businessHandler with this data.
Currently, we have FlatMessageRouter which implement MessageRouter, and extends ChannelInboundHandlerAdapter.
So, we can put this router to netty handler. It will receive UDP packet from Netty channel, call decoder to decode data,
call right business handler base on data type of decoded data.


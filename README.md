# Learning gRPC
gRPC is a free and open-source framework developed by Google. It is part of CNCF (Cloud Native Computation Foundation). Allows you to define Request and Response for RPC (Remote Procedure Calls).

It is modern, fast and efficient, build on top of HTTP/2, low latency, supports streaming, language independent, and makes it easy to plug in authentication, load balancing, logging and monitoring.

## RPC
RPC is a Remote Procedure Call. In the `Client` code, it looks like you are just calling a function directly on `Server`. 

It's not a new concept. gRPC makes cleanly and solves a lot of problems
![Diagram](images/grpc-diagram.png)

## Protobuffer
gRPC is RPC with protobuffer or Protocol Buffer. 
- Is language agnostic
- Code can be generated for pretty much any language
- Data is binary and efficiently serialized, with small payloads.
- Very convenient for transporting a lot of data
- it allows for easy API evolution using rules

Defines Messages (data, requests and Responses) and Services (Service name and RPC endpoints)

## Types of API
4 types:
- Unary: Classic request and response. It is like http rest.
- Server Streaming: Using streaming for server send the response.
- Client Streaming: Using Streaming to send message for server, that response only once.
- Bi Directional Streaming: Using streams to send and received. 

![image](images/type-api.png)

#### Defining services
```proto
service GreetService{ 
  // Unary
  rpc Greet(GreetRequest) returns (GreetResponse) {};

  // Streaming Server
  rpc GreetManyTimes(GreetManyTimesRequest) returns (stream GreetManyTimesResponse) {};

  // Streaming Client
  rpc LongGreet(stream LongGreetRequest) returns (LongGreetResponse) {};

  // Bi Directional Streaming
  rpc GreetEveryone(stream GreetEveryoneRequest) returns (stream GreetEveryoneResponse) {};
}
```

## Scalabitity
- gRPC Servers are asynchronous by default; They do not block threads on request
- Each gRPC server can serve millions of requests in parallel
- gRPC clients can be asynchronous or synchronous
- gRPC clients can perform client side load balancing

## Security in gRPC
- gRPC suggests strongly to use SSL
- Using interceptors, we can also provide authentication


- https://grpc.io/
- https://github.com/grpc/grpc-java
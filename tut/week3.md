layout: true
class: middle

---

class: center, middle, inverse

# Using Thrift

---

## Roadmap

- demo how to use thrift

---

## Demo

*There is also a video on LEARN (Demos -> thrift)*

### Prepare

Download "module05 - ThriftExamples.tar" to eceubuntu or ecetesla machines.

Unzip it. This should create a folder named `thrift_demo/`.

```bash
tar -xf 'module05 - ThriftExamples.tar'
```

Then build it.

```bash
cd thrift_demo/
./build.sh
```

---

### SimpleServer

We will calculate `sqrt()`, but using RPC.

Run server on one ece machine, e.g., ecetesla1

```bash
# ./run_server.sh <port>
./run_server.sh 10000
```

---

Run client from another ece machine, e.g., ecetesla2

```bash
# ./run_client.sh <server> <port> <number_to_sqrt>
./run_client.sh ecetesla1 10000 64
```

It gives

```bash
$ ./run_client.sh ecetesla1 10000 64
sqrt(64.0)=8.0
```

---

To time it, simply use `time`

```bash
$ time ./run_client.sh ecetesla1 10000 64
sqrt(64.0)=8.0

real   0m0.148s
user   0m0.223s
sys    0m0.024s
```

---

We can also try async client

```bash
./run_async_client.sh ecetesla1 10000 64
```

*Try: Which one is faster, sync vs. async client?*

---

## `build.sh`

```bash
$THRIFT_CC --gen java:generated_annotations=suppress demo.thrift
```

These code will ask thrift to generate java implementation.

```bash
$THRIFT_CC --gen py demo.thrift
```

These code will generate python implementation.

---

You can imagine that a python client can also connect to our server, which is
implemented using Java.

```bash
$ ./PythonClient.py ecetesla1 10000 64
sqrt(64)=8.000000
```

---

### Try other servers

In `run_server.sh`, you can change the `JavaServer` in the last line to other
servers (e.g., `JavaHsHaServer`) to see the difference.

---

## Quick work through the code

*If not covered in the lecture*

We will use the slide "module05 - Apache Thrift" under Lectures on LEARN.

There is a lecture recording as well.

---

## Source code

Some source code might worth reading.

I refer to the version 0.13.0 here since that's the version used in the course.

[`TSimpleServer::serve`](https://github.com/apache/thrift/blob/0.13.0/lib/java/src/org/apache/thrift/server/TSimpleServer.java#L42)

[`TThreadPoolServer::serve`](https://github.com/apache/thrift/blob/0.13.0/lib/java/src/org/apache/thrift/server/TThreadPoolServer.java#L170)

[`TThreadPoolServer::execute`](https://github.com/apache/thrift/blob/0.13.0/lib/java/src/org/apache/thrift/server/TThreadPoolServer.java#L181)

You might want to change the `max_length` if the data was large

[`TFramedTransport.java::DEFAULT_MAX_LENGTH`](https://github.com/apache/thrift/blob/0.13.0/lib/java/src/org/apache/thrift/transport/TFramedTransport.java#L30)

---

## A brief summary of the servers

*Also provided in the lecture slide*

| Server                        | Description                                                                                                                           |
|-------------------------------|---------------------------------------------------------------------------------------------------------------------------------------|
| **`TSimpleServer`**           | uses a single thread and blocking I/O.                                                                                                |
| **`TNonblockingServer`**      | uses a single thread and non-blocking I/O. It can handle parallel connections but executes requests serially just like TSimpleServer. |
| **`THsHaServer`**             | uses one thread for network I/O and a pool of worker threads. It can process multiple requests in parallel.                           |
| **`TThreadedSelectorServer`** | uses a pool of threads for network I/O and a pool of worker threads for request processing.                                           |
| **`TThreadPoolServer`**       | uses one thread to accept connections and then handles each connection using a dedicated thread drawn from a pool of worker threads   |

`TNonblockingServer`, `THsHaServer`, and `TThreadedSelectorServer` all extends
`AbstractNonblockingServer`.

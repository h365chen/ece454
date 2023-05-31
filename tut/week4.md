layout: true
class: middle

---

class: center, middle, inverse

# Calculating latency and throughput

---

## Roadmap

- We will talk about some math regarding the minimum latency and the maximum
  throughput in theory.

### Question Context

- We will use a similified model without considering queueing delays

---

## Question 1

A client is executing synchoronous RPCs in a continuous loop in a single thread.
Each RPC is a request to hash one password. What is the approximate formula for
the peak throughput (**upper bound**, in terms of requests per second) and
minimum latency (**lower bound**, in seconds) in the following cases?

- The server is a single-threaded server (e.g., SimpleServer)
- The server is a multi-threaded server (e.g., HsHaServer) with a pool of four
  worker threads, running on four cores without hyperthreading.

---

### Answer

Let's use

- $L$ to denote the one-way network latency
- $D$ to denote the processing delay in the RPC service handler for one
  request

Under both cases, the bottleneck is the client. The latency would be $2L + D$
and throughput is $\frac{1}{2L+D}$.

---

## Question 2

If the client uses $T \gt 1$ threads, assuming each thread executes the same
number of requests per second (i.e., the workload is spread evenly across the
client threads), how does your answer to question 1 change?

---

### Answer

The minimum latency is still $2L + D$, but the peak throughput will change.

Let's use $C$ to denote the number of server cores

Client's throughput $\le \frac{T}{2L+D}$ (since each thread's throughput
$\le \frac{1}{2L+D}$)

Server's throughput $\le \frac{C}{D}$

Therefore, the upper bound of throughput would be $min(\frac{T}{2L+D}, \frac{1}{D})$ if the server is single-threaded, or $min(\frac{T}{2L+D}, \frac{4}{D})$ if it has four cores.

*Think: How many threads are needed to fully utilize the server if we consider $L \eq 0$?*

---

## Question 3

Suppose now we have the server layer comprises:

- One front-end (FE) node with four cores
- Four back-end (BE) nodes, each has four cores

What is the peak throughput (request/sec) and the minimum latency (sec)?

---

### Answer

The minimum latency is $min(2L+D, 4L+D) = 2L+D$

Peak throughput of each thread:

- If request is processed at FENode $\le \frac{1}{2L+D}$
- If request is processed at BENode $\le \frac{1}{4L+D}$

Let's assume each server takes $\frac{1}{5}$ of the requests at peak
throughput

Then, the peak throughput of a client thread is $\frac{1}{\frac{1}{5}(2L+D) + \frac{4}{5}*(4L+D)} = \frac{5}{18L+5D}$

Therefore, the peak throughput of $T$ threads will be $\frac{5T}{18L+5D}$

Given the server's peak throughput is $5*\frac{4}{D} = \frac{20}{D}$

Finally, the peak throughput is $min(\frac{5T}{18L+5D}, \frac{20}{D})$

*Think: How many threads are needed to fully utilize the server if we consider $L=0$?*

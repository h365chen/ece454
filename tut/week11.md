layout: true

class: middle

---

class: center, middle, inverse

# Linearizability Analysis

---

## Roadmap

- We will discuss why your program can produce non-linearizable executions
- The rest of time will be used as Q&A

---

## Sample non-linearizable execution

Assuming the client only touches key X

1. X is initially nil (unknown value)
2. Client writes A
3. Client reads A
4. Sometime later, the client reads nil (stale data)

---

## How can it happen in A3?

Assuming the primary node uses `ConcurrentHashMap` to store the key-value pairs.

Assuming for a get request, the execution on the primary node is as:

1. read the value from its hash table
2. return the value

---

### Case 1

Assuming for a put request, the execution on the primary node is as:

1. update its hash table
2. update backup node's hash table
3. return

_Think: will this guarantee linearizability?_ (assuming the client can send
multiple put&get requests concurrently)

---

### Case 2

Assuming we now have a readers-writer lock, and the execution on the primary
node for a get request is the same as above, but for a put request is now as:

1. lock
2. update its hash table
3. update backup node's hash table
4. unlock
5. return

_Think: will this guarantee linearizability?_ (assuming the client can send
multiple put&get requests concurrently)

---

## Discussion: how to solve it?

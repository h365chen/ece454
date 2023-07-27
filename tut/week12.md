layout: true

class: middle

---

class: center, middle, inverse

# Apache Kafka

---

## Roadmap

- We will see a visualization of Kafka
- We will go through the Kafka demo code

---

## Kafka Visualization

https://softwaremill.com/kafka-visualisation/

Let's try following configurations with one producer and one consumer:

| Brokers | Partitions | Replication | Note                           |
|:-------:|:----------:|:-----------:|--------------------------------|
| 1       | 1          | 1           |                                |
| 1       | 1          | 2           |                                |
| 1       | 2          | 1           | How do you intepret it?        |
| 1       | 1          | 2           | Can this work? Why or why not? |

--

Then tweak the configuration and try it yourselves.

---

## Kafka demo

Download the `kafka_demo.tar` on LEARN, then upload it to ece machines.

Open two terminals side by side (or use `tmux` if you want).

```bash
# term 1
./producer_user.sh
```

```bash
# term 2
./consumer_user.sh
```

---

Type something

```bash
# term 1
./producer_user.sh
>the quick brown fox
>jumps over the lazy dog
```

Then you should see the messages pop up on the consumer side.

```bash
# term 2
./consumer_user.sh
the quick brown fox
jumps over the lazy dog
```

---

If you kill (`Ctrl-C`) both scripts then restart the consumer script only, you
will see the consumer script reads all messages again.

You can comment out the `--from-beginning` in the script so it will only
read messages that have not been read.

---

Let's try the "word count" demo.

```bash
./build_run_wordcount.sh
# --- Cleaning
# --- Compiling Java
# javac 11.0.19
# Success...
# --- Resetting application
# Topic streams-wordcount-output is marked for deletion.
# Note: This will have no impact if delete.topic.enable is not set to true.
# Created topic streams-wordcount-output.
# Topic streams-plaintext-input is marked for deletion.
# Note: This will have no impact if delete.topic.enable is not set to true.
# Created topic streams-plaintext-input.
# Reset-offsets for input topics [streams-plaintext-input]
# Following input topics offsets will be reset to (for consumer group streams-wordcount)
# Topic: streams-plaintext-input Partition: 0 Offset: 0
# Done.
# Deleting all internal/auto-created topics for application streams-wordcount
# Done.
# --- Running
```

---

Then run and type something

```bash
# term 1
./producer_streams.sh
>the quick brown fox
```

Then you should see the messages pop up on the consumer side.

```bash
# term 2
./consumer_streams.sh
the   1
quick 1
brown 1
fox   1
```

---

```bash
# term 1
./producer_streams.sh
>the quick brown fox
>jumps over the lazy dog
```

Then you should see the messages pop up on the consumer side. Notice the word
"`the`" appeared two times and the count is "`2`" the second time.

```bash
# term 2
./consumer_streams.sh
the   1
quick 1
brown 1
fox   1
jumps 1
over  1
the   2
lazy  1
dog   1
```

---

Now, let's do a clean restart.

```bash
./build_run_wordcount.sh
```

But type "`the the the`" for the producer.

```bash
# term 1
./producer_streams.sh
>the the the
```

You will see three messages popped out on the consumer side.

```bash
# term 2
./consumer_streams.sh
the   1
the   2
the   3
```

--

_why?_

---

## Other useful materials

- I put kafka jar files inside `kafka_demo/code/`. By adding them as libraries
  in your IDE, you may find it easier to look into relevant API docs.

- May not be useful for A4, but worth reading
  https://www.confluent.io/blog/enabling-exactly-once-kafka-streams/

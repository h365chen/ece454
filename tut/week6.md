layout: true

class: middle

---

class: center, middle, inverse

# Hadoop MapReduce

---

## Roadmap

- We will follow module07b - Hadoop MapReduce (part 2) to talk about the
  cross-correlation problem
- We will run the cross-correlation.tar sample code
- We will talk about how to kill stuck jobs and retrieve logs

---

## Cross-correlation problem

- Lectures -> module07b - Hadoop MapReduce (part 2)
- Lectures -> cross-correlation.tar

Note that you may need to change the line to match your input

```bash
INPUT_LOCAL=input/Trudeau.txt
```

---

## Apache Hadoop YARN

```bash
yarn app -help
yarn logs -help
```

```bash
yarn app -list -appStates ALL -appTypes MAPREDUCE|grep $USER
```

```bash
yarn logs -applicationId <application ID>
```

---

Try to add printing statements in the code and then collect the logs. For
example, I added

```java
// the first line of the reducer of CCStripes.java
System.out.print("strips: " + word1.toString() + ":");
// inside the first for loop
System.out.print(" " + word2.toString());
// after the first for loop
System.out.println();
```

---

## Question

What are the trade-offs between pair and stripe approaches?

What if we make use of combiners?

# Operating System Labs

Java implementations of the lab assignments from an Operating Systems course. Each lab is a small, runnable program that demonstrates one core OS concept: threads, CPU scheduling, synchronization, deadlock, and virtual memory.

The assignment handouts are the PDFs in the repo root. The code for each lab lives in the matching folder.

| Lab | Topic | Folder | Handout |
|---|---|---|---|
| 2 | Thread management | [`lab2/`](lab2) | `Lab 2 - Thread Management.pdf` |
| 3 | CPU scheduling algorithms | [`lab3/`](lab3) | `Lab 3 - Scheduling Algorithms.pdf` |
| 4 (part 1) | Thread synchronization | [`lab4_1/`](lab4_1) | `Lab 4 - Thread Synchronization 1.pdf` |
| 4 (part 2) | Classic sync problems, thread pool | [`lab4_2/`](lab4_2) | `Lab 4 - Thread Synchronization 2.pdf` |
| 4 (extras) | Semaphore from scratch, Sleeping Barber | [`semaphore/`](semaphore), [`SleepingBarber/`](SleepingBarber) | n/a |
| 5 | Deadlock | [`lab5/`](lab5) | `Lab 5 - Deadlock.pdf` |
| 6 | Main memory | n/a (handout only, no code in this repo) | `Lab 6 - Main Memory.pdf` |
| 7 | Virtual memory | [`lab7/`](lab7) | `Lab 7 - Virtual Memory.pdf` |

---

## Lab 2: Thread Management

Basic Java threading: creating, starting and joining threads, and sharing data between them.

| File | What it does |
|---|---|
| `assignment_1a.java` | Two threads increment and decrement a shared counter 1,000,000 times each, run **one after the other** (`join()` between them). The result is always correct. |
| `assignment_1b.java` | Same counter, but both threads run **at the same time with no synchronization**. This demonstrates a race condition, so the final value is not guaranteed to be 0. |
| `assignment_2.java` | Reads an array from stdin, then computes **average, min and max in three separate threads**. |
| `assignment_3.java` | Takes `N` as a command-line argument and prints all primes up to `N` from a worker thread. |
| `assignment_4.java` | **Monte Carlo estimate of pi**: a worker thread throws `N` random points at the unit square and counts how many land inside the quarter circle. |
| `assignment_5.java` | A worker thread generates the first `N` Fibonacci numbers, and the main thread prints them. |
| `InvalidInputException`, `InvalidValueException` | Custom exceptions for bad command-line arguments. |
| `explain.docx` | Written explanations for the assignment. |

## Lab 3: CPU Scheduling Algorithms

A small **CPU emulator** plus five scheduling algorithms that run on top of it.

**How the emulation works**

- `Task` has a name, a priority (**lower number = higher priority**), a burst time in ms, and a `remain` counter.
- `CPU` "executes" a task by starting a worker thread that sleeps for the task's remaining burst. `execute(task)` runs it to completion, while `execute(task, quantum)` stops waiting after the quantum (via `Thread.join(quantum)`) and subtracts the elapsed time from `remain`. Time is real wall-clock time, so expect a ~1 ms drift per slice.
- Every algorithm implements `IScheduler` (`List<String> Schedule(List<Task>)`) and returns a log of `start time : task, priority, burst, remain`.

**Algorithms**

| Class | Algorithm |
|---|---|
| `FCFS` | First-come, first-served: tasks run in file order |
| `SJF` | Shortest job first (non-preemptive): sorted by burst time |
| `Priority` | Priority scheduling (non-preemptive): sorted by priority |
| `RR` | Round robin with a 100 ms quantum |
| `RRPriority` | Priority scheduling, with round robin among tasks of equal priority |

**Input** is `schedule.txt`, one task per line as `name, priority, burst(ms)`:

```
T1, 4, 200
T2, 3, 250
...
T8, 10, 250
```

`Main` loads the file, runs all five algorithms in turn and prints each schedule. Sample output (FCFS, then the start of Round Robin):

```
--==First come first serve==--
0 : T1, priority=4, burst=200, remain=0
201 : T2, priority=3, burst=250, remain=0
451 : T3, priority=3, burst=250, remain=0
...

--==Round Robin==--
0 : T1, priority=4, burst=200, remain=100
100 : T2, priority=3, burst=250, remain=150
200 : T3, priority=3, burst=250, remain=150
...
```

## Lab 4 (part 1): Thread Synchronization

Locks, condition variables and semaphores in `java.util.concurrent`.

| File | What it does |
|---|---|
| `HELLO.java` | Three threads coordinated with semaphores print `HELLO HELLO HELLO ...` in the right order forever. |
| `HOLE.java` | Same idea with a different semaphore wiring, printing `HOLE HOLE ...`. |
| `BoundedBuffer.java` | Generic **bounded buffer** (producer/consumer) using a `ReentrantLock` and two `Condition`s (not-full, not-empty). The demo pushes 1,000,000 items through a buffer of size 15. |
| `TSQueue.java` | A **thread-safe unbounded queue**. `removeFirst()` blocks while the queue is empty. |
| `Barrier.java` | A reusable **barrier**: threads block in `await()` until `threshold` threads have arrived. |
| `OldBridge.java` | **One-lane bridge** problem: cars may share the bridge only when going the same direction, with at most 3 on it at a time. |
| `assignment2/` | The same shared-counter problem solved three ways: `a2_synchronized`, `a2_semaphore`, `a2_reentrantlock`. All end at 0. |
| `explaination.pdf` | Written explanations. |

## Lab 4 (part 2): Classic Synchronization Problems

| File | What it does |
|---|---|
| `FirstReadersWriters.java` | **First readers-writers problem** (readers' preference), implemented with semaphores. |
| `SecondReadersWriters.java` | Attempt at the **second readers-writers problem** (writers' preference). |
| `WaterFactory.java` | **H2O problem**: 100 hydrogen and 100 oxygen threads arrive at random times, and a water molecule forms every time 2 H + 1 O are present. Also defines the `HydrogenThread`, `OxygenThread` and `IWaterFactory` types. |
| `OrderedWaterFactory.java` | H2O variant that enforces an arrival order (H, then O, then H) with a small state machine. |
| `ThreadPool.java` | A fixed-size **thread pool**: worker threads pulling tasks from a thread-safe queue, with hand-rolled `AtomicInteger`, `TSList` and `TSQueue` helpers. |

## Extras

**`semaphore/`** is a **counting semaphore built from scratch** on top of `synchronized`, `wait()` and `notifyAll()` (`MySemaphore`). `Test` checks it by running two threads that increment/decrement a shared counter 10,000 times each, and the result should be 0. `MyLock` is an empty placeholder.

**`SleepingBarber/`** is the **Sleeping Barber problem**. A barber thread and a customer-generator thread share a waiting room of 3 chairs, coordinated by a lock and two conditions. The barber sleeps when nobody is waiting, and customers wait when the room is full. The simulation runs for 5 seconds and then shuts down cleanly.

## Lab 5: Deadlock

| File | What it does |
|---|---|
| `Bank.java` | Money transfers between accounts. Deadlock is **prevented by lock ordering**: both account locks are always taken in the same global order (by `identityHashCode`), with a tie-break lock for the rare collision. |
| `BankerAlgorithm.java` | **Banker's algorithm**: a safety check that returns a safe sequence (or `null`), and a `request()` method that tentatively grants resources and re-checks safety. The demo uses the textbook example with 5 processes and 3 resource types. It finds the safe sequence `[1, 3, 4, 0, 2]`, and a request of `(1, 0, 2)` from process 1 is granted. |
| `DiningPhilosophers1.java` | **Dining philosophers** with an asymmetric strategy: even-numbered philosophers pick up forks in the opposite order from odd-numbered ones, which breaks the circular wait. Runs 5 seconds, then shuts down. |
| `DiningPhilosophers2.java` | A second take using a shared chopstick counter and a condition variable. |

## Lab 6: Main Memory

Only the handout (`Lab 6 - Main Memory.pdf`) is included, with no code.

## Lab 7: Virtual Memory

### Virtual Memory Manager (`src/VMM/`)

A simulation of **demand paging** with a TLB. Parameters (set in `VMMMain`):

| Parameter | Value |
|---|---|
| Logical address space | 65,536 bytes (256 pages) |
| Page / frame size | 256 bytes |
| Physical memory | 8,192 bytes (32 frames) |
| TLB | 16 entries |
| Replacement policy | FIFO, for both the TLB and the page table |

On each read, the VMM checks the TLB, then the page table, and on a **page fault** loads the 256-byte page from `BACKING_STORE.bin` (a 64 KB file standing in for the disk) into a frame. `VMMMain` replays the 2,000 logical addresses in `addresses.txt`, checks each byte returned against the backing store, and prints the statistics. With the included data:

```
TLB miss count: 945
Page Fault count: 880
```

### Page replacement algorithms (`src/PageReplacementAlgo/`)

`FIFO`, `LRU` and `OPT` (optimal) each take a page reference string and a frame count and return the number of page faults. `test/PageReplacementAlgoTest.java` checks them against the textbook reference string `7 0 1 2 0 3 0 4 2 3 0 3 2 1 2 0 1 7 0 1` with 3 frames:

| Algorithm | Page faults |
|---|---|
| FIFO | 15 |
| LRU | 12 |
| OPT | 9 |

`src/App.java` and `test/AppTest.java` are just the VS Code Java project sample, and `lab7/README.md` is the stock VS Code template.

---

## Running the code

Requires a JDK (tested with Java 21). There is no Maven/Gradle build file, so everything is compiled with plain `javac`.

> Many files in the same folder declare classes with the same names (for example `Data` in `lab2`, or `Main` in `lab5`), so **compile one program at a time** rather than `*.java`. The exceptions are `lab3` and `lab7`, which are self-contained.

Most programs declare a package, so compile and run them from the **repo root**:

```bash
# Lab 2
javac -d out lab2/assignment_4.java lab2/InvalidInputException.java lab2/InvalidValueException.java
java -cp out lab2.assignment_4 1000000

# Lab 4
javac -d out lab4_1/BoundedBuffer.java && java -cp out lab4_1.BoundedBuffer
javac -d out lab4_1/HELLO.java         && java -cp out lab4_1.HELLO
javac -d out lab4_2/WaterFactory.java  && java -cp out lab4_2.WaterFactory

# Semaphore / Sleeping Barber
javac -d out semaphore/*.java          && java -cp out semaphore.Test
javac -d out SleepingBarber/Test.java  && java -cp out SleepingBarber.Test

# Lab 5
javac -d out lab5/BankerAlgorithm.java       && java -cp out lab5.Main
javac -d out lab5/DiningPhilosophers1.java   && java -cp out lab5.Main
```

Lab 3 and Lab 7 read data files from the current directory, so run them from inside their folders:

```bash
# Lab 3: CPU scheduling
cd lab3
javac -d out *.java
java -cp out Main

# Lab 7: virtual memory manager
cd lab7
javac -d out src/VMM/*.java
java -cp out VMM.VMMMain
```

The Lab 7 unit tests use JUnit 5 and are meant to be run from VS Code's Java test runner (or any JUnit 5 setup).

## Notes

- `lab2/assignment_1b.java` has no synchronization **on purpose**. It demonstrates a race, so its output is non-deterministic.
- `lab4_2/SecondReadersWriters.java` is unfinished: it re-declares `Reader`/`Writer` from the other file, so it does not compile as-is.
- `lab4_2/ThreadPool.java` has no `main` and is a work in progress.
- `lab5/DiningPhilosophers2.java` is experimental and can throw `IllegalMonitorStateException`.

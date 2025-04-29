# Case Studies 📕

## Case Study 1

X has 1 doctor. The doctor’s average consultation time is 3 minutes per patient. Currently, there
are 5 patients in the queue and the doctor is not seeing any patient. John decides to queue online and
he becomes the 6th patient in the queue, having 5 patients ahead of him. Since there are 5 patients
ahead and each taking 3 minutes on average, the waiting time for John is estimated to be 15 minutes.

### Question 1

When should the countdown in waiting time, i.e. from 15 minutes, start for John? The moment he
queues online, or when the doctor starts seeing the first patient in the queue?

> The countdown should start only after doctor starts seeing the patients, since starting count down before will make
> the system indeterministic as we cannot be certain the variable in the wait time

### Question 2

Suppose the case has changed – at the time John queues online, there are 5 patients ahead of him
and the doctor is currently seeing one of the 5 patients, Peter. Assuming that the doctor’s
average consultation time per patient is 3 minutes, what will John’s estimated waiting time be given
that, at the time John joins the queue, Peter has already gone to the consultation room for the
following durations?

> If the doctor was not seeing any patient, the wait time would be 15 mins. Now, as the doctor is seeing one of the
> five patients, the wait time will be < 15 mins, if it's the fifth patient, then the wait time would be less than
> 3 mins.

## Case Study 2

Clinic Y has 2 doctors, Doctor A and Doctor B. The average consultation times per patient are 3
minutes for Doctor A and 4 minutes for Doctor B. Currently, there are 10 patients in the queue and
both doctors are not seeing any patient. John decides to queue online and he becomes the 11th
patient in the queue, with 10 patients ahead of him.

### Question 1

Assuming that all the patients in the queue, including John, have no specific preferences for the
doctors they want to consult, what will John’s estimated waiting time be when he joins the queue?

> We'll be allocating the doctor that is available first,
> using this strategy, we can find John's estimated time

| Time | Doctor A (avg. 3mins) | Doctor B (avg. 4mins) |
|------|-----------------------|-----------------------|
| 0    | Patient 1             | Patient 2             |
| 3    | Patient 3             | Patient 2 (ongoing)   |
| 4    | Patient 3 (ongoing)   | Patient 4             |
| 6    | Patient 5             | Patient 4 (ongoing)   |
| 8    | Patient 5 (ongoing)   | Patient 6             |
| 9    | Patient 7             | Patient 6 (ongoing)   |
| 12   | Patient 8             | Patient 9             |
| 15   | Patient 10            | Patient 9 (ongoing)   |
| 16   | Patient 10 (ongoing)  | John*                 |

> As we can see, John's wait time will be 16 mins, and he will
> consult Doctor B

## Question 2

Suppose the case has changed – at the time John queues online, there are 10 patients ahead of him
and while Doctor A is not seeing any patient (i.e. Doctor A is available), Doctor B is currently seeing
the first patient, Lucas, who has been in the consultation room for the past 2 minutes.
Assuming that the average consultation times per patient for both doctors remain as 3 minutes and 4
minutes respectively, what will John’s estimated waiting time be when he joins the queue?

> Now, as Doctor B is already seeing the first patient - Lucas, we can use the same strategy, by adjusting the time
> already spent in consulting by Doctor B

| Time | Doctor A (avg. 3mins) | Doctor B (avg. 4mins) |
|------|-----------------------|-----------------------|
| -2   |                       | Lucas                 |
| 0    | Patient 2             | Lucas (ongoing)       |
| 2    | Patient 2 (ongoing)   | Patient 3             |
| 3    | Patient 4             | Patient 3 (ongoing)   |
| 6    | Patient 5             | Patient 6             |
| 9    | Patient 7             | Patient 6 (ongoing)   |
| 10   | Patient 7 (ongoing)   | Patient 8             |
| 12   | Patient 9             | Patient 8 (ongoing)   |
| 14   | Patient 9 (ongoing)   | Patient 10            |
| 15   | John*                 |                       |

> As we can see, John's wait time will be 15 mins, and he will
> consult Doctor A

## Bonus

Write an algorithm – in Kotlin or Java – to calculate patient’s estimated waiting time. The algorithm
should work for different amount of doctors and patient queue positions – it should accept an array (or
arrayList) of Doctor objects and patient’s position in queue as inputs and return the patient’s waiting
time as output. Your codes should also include the implementation for the Doctor class. For simplicity,
assume all the patients in the queue have no preference for the doctors they want to consult and all
the doctors are available and not seeing any patient initially.

> Please find the example [here](../../Main.kt) for the online queue system and relevant test cases can be found
> [here](../../../../test/kotlin/com/codeitsolo/queuesystem/queue/DefaultOnlineWaitingQueueTest.kt)

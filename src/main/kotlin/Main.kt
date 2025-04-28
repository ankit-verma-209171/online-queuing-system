package com.codeitsolo

import com.codeitsolo.queuesystem.model.Doctor
import com.codeitsolo.queuesystem.model.Patient
import com.codeitsolo.queuesystem.queue.DefaultOnlineWaitingQueue
import com.codeitsolo.queuesystem.queue.WaitingQueue
import kotlin.time.Duration.Companion.minutes

/**
 * The main entry point of the application that demonstrates the usage of the online queue system.
 *
 * This program:
 * 1. Creates a list of doctors with different consultation times
 * 2. Initializes a waiting queue with these doctors
 * 3. Adds multiple patients to the queue
 * 4. Demonstrates how to handle a specific patient's enqueueing process
 * 5. Shows how to estimate waiting time for a specific patient
 */
fun main() {
    val doctors = listOf(
        Doctor(averageConsultationTime = 3.minutes),
        Doctor(averageConsultationTime = 4.minutes),
    )
    val queue: WaitingQueue = DefaultOnlineWaitingQueue(doctors = doctors)
    val patients = List(10) { Patient() }
    patients.forEach(queue::enqueue)

    val john = Patient()
    val johnToken = queue.enqueue(patient = john).getOrElse {
        println("John was not able to enqueue: ${it.message}")
        return@main
    }

    val waitTimeForJohn = queue.estimateWaitingTime(token = johnToken)
    println(waitTimeForJohn)
}
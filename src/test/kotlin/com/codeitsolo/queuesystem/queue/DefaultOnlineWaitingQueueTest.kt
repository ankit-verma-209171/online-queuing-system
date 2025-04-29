package com.codeitsolo.queuesystem.queue

import com.codeitsolo.queuesystem.model.Doctor
import com.codeitsolo.queuesystem.model.Patient
import queuesystem.model.Token
import kotlin.test.*
import kotlin.time.Duration.Companion.minutes

/**
 * Unit tests for the DefaultOnlineWaitingQueue class.
 *
 * This test suite validates the core functionalities of the DefaultOnlineWaitingQueue,
 * including enqueuing patients, calculating waiting times, managing queue limits, and
 * handling scenarios such as no doctors or invalid tokens.
 */
class DefaultOnlineWaitingQueueTest {

    private lateinit var doctors: List<Doctor>
    private lateinit var queue: DefaultOnlineWaitingQueue

    @BeforeTest
    fun setup() {
        // Setup doctors with different consultation times
        doctors = listOf(
            Doctor(averageConsultationTime = 3.minutes),
            Doctor(averageConsultationTime = 4.minutes)
        )
        // Initialize queue with these doctors
        queue = DefaultOnlineWaitingQueue(doctors = doctors)
    }

    @Test
    fun `test enqueue adds patient to queue successfully`() {
        val patient = Patient()
        val result = queue.enqueue(patient)
        
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
    }

    @Test
    fun `test enqueue returns token for the patient`() {
        val patient = Patient()
        val tokenResult = queue.enqueue(patient)
        
        assertTrue(tokenResult.isSuccess)
        val token = tokenResult.getOrNull()
        assertNotNull(token)
        assertEquals(patient, token.patient)
    }

    @Test
    fun `test enqueue fails when queue is full`() {
        // Create a queue with a limit of 2 patients
        val limitedQueue = DefaultOnlineWaitingQueue(doctors = doctors, queueLimit = 2)
        
        // Add 2 patients to fill the queue
        val patient1 = Patient()
        val patient2 = Patient()
        limitedQueue.enqueue(patient1)
        limitedQueue.enqueue(patient2)
        
        // Try to add a third patient
        val patient3 = Patient()
        val result = limitedQueue.enqueue(patient3)
        
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertNotNull(exception)
        assertEquals("Queue is full", exception.message)
    }

    @Test
    fun `test enqueue multiple patients should succeed`() {
        val patients = List(5) { Patient() }
        val tokens = patients.map { queue.enqueue(it) }
        
        assertTrue(tokens.all { it.isSuccess })
        assertEquals(5, tokens.size)
    }

    @Test
    fun `test estimateWaitingTime for first patient should be zero`() {
        val patient = Patient()
        val tokenResult = queue.enqueue(patient)
        assertTrue(tokenResult.isSuccess)
        
        val token = tokenResult.getOrNull()
        assertNotNull(token)
        
        val waitTimeResult = queue.estimateWaitingTime(token)
        assertTrue(waitTimeResult.isSuccess)
        
        val waitTime = waitTimeResult.getOrNull()
        assertNotNull(waitTime)
        assertEquals(0.minutes, waitTime) // The first patient should have zero waiting time
    }

    @Test
    fun `test estimateWaitingTime for subsequent patients`() {
        // Add 3 patients to the queue
        val patients = List(3) { Patient() }
        val tokens = patients.map { queue.enqueue(it).getOrThrow() }
        
        // The first patient should have zero waiting time
        assertEquals(0.minutes, queue.estimateWaitingTime(tokens[0]).getOrThrow())
        
        // The second patient should also have zero waiting time, since there are 2 doctors
        assertEquals(0.minutes, queue.estimateWaitingTime(tokens[1]).getOrThrow())
        
        // The third patient should wait for the first doctor to finish with the first patient (3 minutes)
        assertEquals(3.minutes, queue.estimateWaitingTime(tokens[2]).getOrThrow())
    }

    @Test
    fun `test estimateWaitingTime fails for invalid token`() {
        val patient = Patient()
        val invalidToken = Token(patient = patient)
        
        val result = queue.estimateWaitingTime(invalidToken)
        
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertNotNull(exception)
        assertEquals("Token not found", exception.message)
    }

    @Test
    fun `test queue with no doctors fails on enqueue`() {
        val emptyQueue = DefaultOnlineWaitingQueue(doctors = emptyList())
        val patient = Patient()
        
        val result = emptyQueue.enqueue(patient)
        
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertNotNull(exception)
        assertEquals("No doctors available", exception.message)
    }

    @Test
    fun `test queue with no doctors fails on estimateWaitingTime`() {
        val emptyQueue = DefaultOnlineWaitingQueue(doctors = emptyList())
        val patient = Patient()
        val token = Token(patient = patient)
        
        val result = emptyQueue.estimateWaitingTime(token)
        
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertNotNull(exception)
        assertEquals("No doctors available", exception.message)
    }

    @Test
    fun `test doctors with different consultation times distribute patients optimally`() {
        // Create a queue with doctors having significantly different consultation times
        val specializedDoctors = listOf(
            Doctor(averageConsultationTime = 2.minutes),
            Doctor(averageConsultationTime = 6.minutes)
        )
        val specializedQueue = DefaultOnlineWaitingQueue(doctors = specializedDoctors)
        
        // Add 4 patients
        val patients = List(4) { Patient() }
        val tokens = patients.map { specializedQueue.enqueue(it).getOrThrow() }
        
        // Expected waiting times (the fastest doctor should get more patients):
        // Patient 1: 0 minutes (doctor 1)
        // Patient 2: 0 minutes (doctor 2)
        // Patient 3: 2 minutes (doctor 1)
        // Patient 4: 4 minutes (doctor 1)
        
        assertEquals(0.minutes, specializedQueue.estimateWaitingTime(tokens[0]).getOrThrow())
        assertEquals(0.minutes, specializedQueue.estimateWaitingTime(tokens[1]).getOrThrow())
        assertEquals(2.minutes, specializedQueue.estimateWaitingTime(tokens[2]).getOrThrow())
        assertEquals(4.minutes, specializedQueue.estimateWaitingTime(tokens[3]).getOrThrow())
    }
}

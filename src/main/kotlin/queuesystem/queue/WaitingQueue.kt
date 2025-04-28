package com.codeitsolo.queuesystem.queue

import com.codeitsolo.queuesystem.model.Doctor
import com.codeitsolo.queuesystem.model.Patient
import queuesystem.model.Token
import kotlin.time.Duration

/**
 * Represents a waiting queue system for patients seeking consultation with doctors.
 */
interface WaitingQueue {

    /**
     * The list of doctors available in this queue system.
     *
     * These doctors provide consultations to the patients in the waiting queue.
     * Their availability and consultation times affect the overall waiting times.
     */
    val doctors: List<Doctor>

    /**
     * Adds a patient to the waiting queue and returns a token if successful.
     *
     * This method registers a patient in the waiting queue and returns a token
     * that can be used to identify the patient's position and estimate their waiting time.
     * The operation may fail if the queue is full or no doctors are available.
     *
     * @param patient The patient to be added to the queue
     * @return A [Result] containing the issued token if successful, or an error if the operation fails
     */
    fun enqueue(patient: Patient): Result<Token>

    /**
     * Calculates the estimated waiting time for a patient based on their token.
     *
     * This method uses the token to identify the patient's position in the queue
     * and calculates how long they are expected to wait before being seen by a doctor.
     * The estimate may vary based on the queue implementation, doctor availability,
     * and consultation times.
     *
     * @param token The token issued to the patient when they joined the queue
     * @return A [Result] containing the estimated waiting duration if successful, or an error
     *         if the token is invalid or the estimate cannot be calculated
     */
    fun estimateWaitingTime(token: Token): Result<Duration>
}
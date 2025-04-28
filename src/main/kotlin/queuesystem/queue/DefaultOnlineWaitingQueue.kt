package com.codeitsolo.queuesystem.queue

import com.codeitsolo.queuesystem.model.Doctor
import com.codeitsolo.queuesystem.model.Patient
import com.codeitsolo.queuesystem.model.Slot
import queuesystem.model.Token
import kotlin.time.Duration


/**
 * Default implementation of the [WaitingQueue] interface for managing an online waiting queue system.
 *
 * This implementation maintains a list of available doctors and a queue of waiting patients.
 * It handles patient enqueueing and wait time calculations based on the current queue state.
 * The implementation uses a dynamic programming approach to track doctor availability and
 * efficiently estimate waiting times.
 *
 * @property doctors The list of doctors available for consultations in this queue system
 * @property queueLimit Maximum number of patients, who can be in the queue at once, defaults to [Int.MAX_VALUE]
 */
class DefaultOnlineWaitingQueue(
    override val doctors: List<Doctor>,
    private val queueLimit: Int = Int.MAX_VALUE,
) : WaitingQueue {

    /**
     * Stores the queue of tokens currently representing patients waiting for their consultations.
     *
     * This list maintains the order of tokens in sequence of their addition to the queue,
     * ensuring that patients are served in a first-come, first-served basis unless overridden
     * by specific logic.
     */
    private val tokensQueue = mutableListOf<Token>()

    /**
     * Dynamic programming structure that tracks the state of doctor slots after each patient is added.
     * Each entry represents the state of all doctor slots after adding a specific patient to the queue.
     * The first entry represents the initial state before any patients are added.
     */
    private val slotsDp: MutableList<Array<Slot>> = mutableListOf(
        Array(doctors.size) { Slot(doctor = doctors[it]) }
    )

    /**
     * Adds a patient to the waiting queue and returns a token if successful.
     *
     * This implementation:
     * 1. Checks if the queue has reached its capacity limit
     * 2. Creates a token for the patient
     * 3. Finds the doctor slot with the minimum waiting time
     * 4. Updates the slot's availability time based on the doctor's consultation time
     * 5. Updates the dynamic programming state for future waiting time calculations
     *
     * @param patient The patient to be added to the queue
     * @return A [Result] containing the issued token if successful, or an error if the queue is full
     *         or no doctors are available
     */
    override fun enqueue(patient: Patient): Result<Token> {
        if (tokensQueue.size >= queueLimit) return Result.failure(Exception("Queue is full"))

        val token = Token(
            patient = patient,
        )

        val lastTokenSlots = slotsDp.last()
        val (index, minSlot) = lastTokenSlots
            .withIndex()
            .minByOrNull { (_, slot) -> slot.timeUntilAvailable }
            ?: return Result.failure(Exception("No doctors available"))

        val nextTokenSlots = lastTokenSlots.clone()
        nextTokenSlots[index] = minSlot.copy(
            timeUntilAvailable = minSlot.timeUntilAvailable + minSlot.doctor.averageConsultationTime
        )
        slotsDp += nextTokenSlots

        tokensQueue += token
        return Result.success(token)
    }

    /**
     * Calculates the estimated waiting time for a patient based on their token.
     *
     * @param token The token issued to the patient when they joined the queue
     * @return A [Result] containing the estimated waiting duration if successful, or an error if the token
     *         is invalid or no doctors/slots are available
     */
    override fun estimateWaitingTime(token: Token): Result<Duration> {
        if (doctors.isEmpty()) return Result.failure(Exception("No doctors available"))
        if (token !in tokensQueue) return Result.failure(Exception("Token not found"))

        val index = tokensQueue.indexOf(token)
        return slotsDp[index]
            .minByOrNull { it.timeUntilAvailable }
            ?.let { Result.success(it.timeUntilAvailable) }
            ?: Result.failure(Exception("No slots available"))
    }
}
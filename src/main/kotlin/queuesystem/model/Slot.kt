package com.codeitsolo.queuesystem.model

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Represents a consultation slot associated with a doctor in the queue system.
 *
 * @property id The unique identifier for this slot, automatically generated as a UUID string
 * @property doctor The doctor associated with this slot
 * @property timeUntilAvailable The duration until this doctor will be available for the next patient,
 *                              defaults to 0 seconds (immediately available)
 */
@OptIn(ExperimentalUuidApi::class)
data class Slot(
    val id: String = Uuid.random().toString(),
    val doctor: Doctor,
    val timeUntilAvailable: Duration = 0.seconds,
)

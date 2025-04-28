package com.codeitsolo.queuesystem.model

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Represents a patient entity in the queue system.
 *
 * @property id The unique identifier for this patient, automatically generated as a UUID string
 * @property preferredDoctor The doctor that this patient prefers to consult with, it can be null if no preference is set
 */
@OptIn(ExperimentalUuidApi::class)
data class Patient(
    val id: String = Uuid.random().toString(),
    val preferredDoctor: Doctor? = null,
)

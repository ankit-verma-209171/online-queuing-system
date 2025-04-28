package com.codeitsolo.queuesystem.model

import kotlin.time.Duration
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Represents a doctor in the system.
 *
 * @property id Unique identifier for the doctor. Automatically generated if not provided.
 * @property averageConsultationTime The typical duration a doctor spends with each patient during consultation.
 */
@OptIn(ExperimentalUuidApi::class)
data class Doctor(
    val id: String = Uuid.random().toString(),
    val averageConsultationTime: Duration,
)

package queuesystem.model

import com.codeitsolo.queuesystem.model.Patient
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Represents a token issued to a patient when they join the waiting queue.
 *
 * @property id Unique identifier for the token. Automatically generated if not provided.
 * @property patient The patient associated with this token.
 */
@OptIn(ExperimentalUuidApi::class)
data class Token(
    val id: String = Uuid.random().toString(),
    val patient: Patient,
)
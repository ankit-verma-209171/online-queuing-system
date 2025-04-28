package com.codeitsolo.queuesystem.model

import com.codeitsolo.queuesystem.queue.WaitingQueue

/**
 * Represents a time control entity in the queue system simulation.
 *
 * This singleton object is used as a parameter in time simulation operations,
 * particularly in the [WaitingQueue.elapseTimeInSeconds] method, to control
 * and validate time-based operations in the queue system.
 */
data object God
package com.sebastianvm.bgcomp.features.kombio.enterpoints.viewmodel

import androidx.savedstate.serialization.decodeFromSavedState
import androidx.savedstate.serialization.encodeToSavedState
import io.kotest.matchers.shouldBe
import kotlin.test.Test

// val EnterPointsTest by testSuite {
//
//    testSerialization("Serialization and deserialization works with default values",
// EnterPointsState())
//
//    testSerialization("Serialization and deserialization works with non-default values",
// EnterPointsState(
//        playerNames = persistentListOf("A", "B"),
//        handPoints = persistentListOf("1", "2"),
//        kombioCallerIndex = 1,
//        roundNumber = 2,
//        isRestored = true
//    ))
//
// }

class EnterPointsSerializationtest {

    @Test
    fun testSerialization() {
        val state = EnterPointsState()
        val serialized = encodeToSavedState(state)
        val deserialized = decodeFromSavedState<EnterPointsState>(serialized)
        deserialized shouldBe state
    }
}
//
// fun TestSuite.testSerialization(name: String, state: EnterPointsState) = test(name) {
//    val serialized = encodeToSavedState(state)
//    val deserialized = decodeFromSavedState<EnterPointsState>(serialized)
//    deserialized shouldBe state
//
// }

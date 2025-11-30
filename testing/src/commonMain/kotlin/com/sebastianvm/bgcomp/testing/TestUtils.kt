package com.sebastianvm.bgcomp.testing

import de.infix.testBalloon.framework.core.TestExecutionScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent as kotlinxRunCurrent

@OptIn(ExperimentalCoroutinesApi::class)
context(executionScope: TestExecutionScope)
fun runCurrent() {
    executionScope.testScope.kotlinxRunCurrent()
}

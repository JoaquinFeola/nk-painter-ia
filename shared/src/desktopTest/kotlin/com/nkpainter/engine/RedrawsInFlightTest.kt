package com.nkpainter.engine

import kotlin.test.Test
import kotlin.test.assertEquals

class RedrawsInFlightTest {
    private var redraws = 0
    private val gate = RedrawsInFlight { redraws++ }

    @Test
    fun aRedrawAskedWithNothingInFlightStartsAtOnce() {
        gate.ask()

        assertEquals(1, redraws)
    }

    @Test
    fun redrawsAskedWhileOneIsInFlightWaitForItAndBecomeOne() {
        gate.ask()
        repeat(30) { gate.ask() }

        gate.finished()

        assertEquals(2, redraws)
    }

    @Test
    fun nothingStartsWhenTheRedrawFinishesWithNothingAsked() {
        gate.ask()

        gate.finished()

        assertEquals(1, redraws)
    }

    @Test
    fun aRedrawStartedElsewhereHoldsBackTheNextOne() {
        gate.startedElsewhere()
        gate.ask()
        val whileElsewhereRuns = redraws

        gate.finished()

        assertEquals(0, whileElsewhereRuns)
        assertEquals(1, redraws)
    }

    @Test
    fun afterTheSurfaceIsLostTheNextAskStartsAtOnce() {
        gate.ask()
        gate.ask()
        gate.forget()

        gate.ask()

        assertEquals(2, redraws)
    }
}

package com.rapidsos.emergencydatasdk.test_helpers

import org.mockito.Mockito

/**
 * @author Josias Sena
 */
class UnitTestHelpers {

    fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T

}
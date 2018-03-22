package com.rapidsos.emergencydatasdk.sdk

import com.rapidsos.emergencydatasdk.internal.exceptions.EraSdkUnInitializedException

/**
 * @author Josias Sena
 */
internal object SdkInitiatedValidator {

    var isSdkInit = false

    fun checkIfSDKIsInitialized(errorMessage: String): Boolean {
        return if (isSdkInit) {
            true
        } else {
            throw EraSdkUnInitializedException(errorMessage)
        }
    }
}
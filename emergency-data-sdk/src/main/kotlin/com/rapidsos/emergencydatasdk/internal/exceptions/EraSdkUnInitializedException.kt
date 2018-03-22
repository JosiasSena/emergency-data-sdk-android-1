package com.rapidsos.emergencydatasdk.internal.exceptions

import com.rapidsos.emergencydatasdk.sdk.EraSdk

/**
 * Thrown when an application tries to use a class which requires the Era sdk to be initialized first.
 *
 * To initialize the sdk the user must do the following:
 *
 * ```
 * val eraSdk = EraSdk(context)
 * eraSdk.initialize(HOST, RSOS_CLIENT_ID, RSOS_CLIENT_SECRET)
 * ```
 *
 * @author  Josias Sena
 * @see EraSdk
 */
class EraSdkUnInitializedException(errorMessage: String) : RuntimeException(errorMessage)
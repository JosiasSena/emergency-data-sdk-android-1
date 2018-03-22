package com.rapidsos.emergencydatasdk.rules

import io.appflate.restmock.JVMFileParser
import io.appflate.restmock.RESTMockServer
import io.appflate.restmock.RESTMockServerStarter
import io.appflate.restmock.logging.RESTMockLogger
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * @author Josias Sena
 */
class MockWebServerRule : TestRule {

    override fun apply(base: Statement?, description: Description?): Statement {
        return object : Statement() {
            override fun evaluate() {
                RESTMockServerStarter.startSync(JVMFileParser())
                RESTMockServer.enableLogging(object : RESTMockLogger {

                    override fun error(errorMessage: String?) {
                        println("RESTMockServer Error $errorMessage")
                    }

                    override fun error(errorMessage: String?, exception: Throwable?) {
                        println("RESTMockServer Error $errorMessage ${exception?.message}")
                    }

                    override fun log(message: String?) {
                        println("RESTMockServer Log $message")
                    }
                })

                base?.evaluate()

                RESTMockServer.reset()
            }
        }
    }

}
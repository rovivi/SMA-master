package com.kyadevs.stepdroid

class Global {
    companion object {
        fun logError(throwable: Throwable, type: String = "Error", optional: String? = null) {
            val reason =
                "${(throwable.message ?: throwable.toString())}${(if (optional != null) " => $optional" else "")}"
            var where = type
            if (throwable.stackTrace.isNotEmpty()) {
                val f = throwable.stackTrace.first()
                where = "${f.className}.${f.methodName}"
            }
            /*events.clear()
            events[where] = reason
            trackEvent(type)
            Log.v(where, reason)*/
        }
    }


}
package com.rapidsos.emergencydatasdk.data.devices

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CrashPulseValue(@SerializedName("label")
                           @Expose
                           var label: String? = null,

                           @SerializedName("url")
                           @Expose
                           var url: String? = null,

                           @SerializedName("note")
                           @Expose
                           var note: String? = null)
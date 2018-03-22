package com.rapidsos.emergencydatasdk.data.devices

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CallbackPhoneValue(@SerializedName("label")
                              @Expose
                              var label: String? = null,

                              @SerializedName("number")
                              @Expose
                              var number: String? = null,

                              @SerializedName("note")
                              @Expose
                              var note: String? = null)
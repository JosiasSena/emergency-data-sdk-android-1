package com.rapidsos.emergencydatasdk.data.devices

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CallbackPhone(@SerializedName("value")
                         @Expose
                         var value: List<CallbackPhoneValue>? = null,

                         @SerializedName("type")
                         @Expose
                         var type: String? = null,

                         @SerializedName("display_name")
                         @Expose
                         var displayName: String? = null)
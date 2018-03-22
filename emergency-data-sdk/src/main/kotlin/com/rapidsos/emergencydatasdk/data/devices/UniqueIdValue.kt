package com.rapidsos.emergencydatasdk.data.devices

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UniqueIdValue(@SerializedName("type")
                         @Expose
                         var type: String? = null,

                         @SerializedName("id")
                         @Expose
                         var id: String? = null)

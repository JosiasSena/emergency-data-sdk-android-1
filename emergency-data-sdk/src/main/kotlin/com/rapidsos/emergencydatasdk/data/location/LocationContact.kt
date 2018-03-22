package com.rapidsos.emergencydatasdk.data.location

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LocationContact(@SerializedName("value")
                           @Expose
                           var value: List<LocationContactValue>? = mutableListOf(),

                           @SerializedName("type")
                           @Expose
                           var type: String = "",

                           @SerializedName("display_name")
                           @Expose
                           var displayName: String = "")
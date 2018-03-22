package com.rapidsos.emergencydatasdk.data.devices

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BodyTemperature(@SerializedName("value")
                           @Expose
                           var value: List<BodyTemperatureValue>? = null,

                           @SerializedName("type")
                           @Expose
                           var type: String? = null,

                           @SerializedName("display_name")
                           @Expose
                           var displayName: String? = null,

                           @SerializedName("units")
                           @Expose
                           var units: String? = null)

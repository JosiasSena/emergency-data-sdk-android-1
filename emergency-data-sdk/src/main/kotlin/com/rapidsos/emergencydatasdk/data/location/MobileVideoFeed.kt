package com.rapidsos.emergencydatasdk.data.location

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MobileVideoFeed(@SerializedName("value")
                           @Expose
                           var value: List<MobileVideoFeedValue>? = mutableListOf(),

                           @SerializedName("type")
                           @Expose
                           var type: String = "",

                           @SerializedName("display_name")
                           @Expose
                           var displayName: String = "")

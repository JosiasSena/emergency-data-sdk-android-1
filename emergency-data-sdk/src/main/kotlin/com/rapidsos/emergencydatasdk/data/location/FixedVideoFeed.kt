package com.rapidsos.emergencydatasdk.data.location

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FixedVideoFeed(@SerializedName("value")
                          @Expose
                          var value: List<FixedVideoFeedValue> = mutableListOf(),

                          @SerializedName("type")
                          @Expose
                          var type: String = "",

                          @SerializedName("display_name")
                          @Expose
                          var displayName: String = "")
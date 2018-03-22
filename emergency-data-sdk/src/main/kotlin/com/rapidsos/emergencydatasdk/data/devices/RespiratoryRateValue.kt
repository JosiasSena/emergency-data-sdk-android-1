package com.rapidsos.emergencydatasdk.data.devices

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RespiratoryRateValue(@SerializedName("label")
                                @Expose
                                var label: String? = null,

                                @SerializedName("data_feed")
                                @Expose
                                var dataFeed: String? = null,

                                @SerializedName("protocol")
                                @Expose
                                var protocol: String? = null,

                                @SerializedName("note")
                                @Expose
                                var note: String? = null)
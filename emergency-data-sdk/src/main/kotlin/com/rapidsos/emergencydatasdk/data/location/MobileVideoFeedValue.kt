package com.rapidsos.emergencydatasdk.data.location

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MobileVideoFeedValue(@SerializedName("label") @Expose var label: String?,
                                @SerializedName("url") @Expose var url: String?,
                                @SerializedName("format") @Expose var format: String?,
                                @SerializedName("latitude") @Expose var latitude: Double?,
                                @SerializedName("longitude") @Expose var longitude: Double?,
                                @SerializedName("note") @Expose var note: String?)

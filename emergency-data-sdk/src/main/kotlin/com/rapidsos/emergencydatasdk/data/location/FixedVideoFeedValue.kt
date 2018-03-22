package com.rapidsos.emergencydatasdk.data.location

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FixedVideoFeedValue(@SerializedName("label")
                               @Expose
                               private val label: String?,

                               @SerializedName("url")
                               @Expose
                               private val url: String?,

                               @SerializedName("format")
                               @Expose
                               private val format: String?,

                               @SerializedName("latitude")
                               @Expose
                               private val latitude: Double?,

                               @SerializedName("longitude")
                               @Expose
                               private val longitude: Double?,

                               @SerializedName("note")
                               @Expose
                               private val note: String?)
package com.rapidsos.emergencydatasdk.data.devices

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Manufacturer(@SerializedName("value")
                        @Expose
                        var value: List<String>? = null,

                        @SerializedName("type")
                        @Expose
                        var type: String? = null,

                        @SerializedName("display_name")
                        @Expose
                        var displayName: String? = null)
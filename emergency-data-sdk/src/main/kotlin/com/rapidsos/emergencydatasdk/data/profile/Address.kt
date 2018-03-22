package com.rapidsos.emergencydatasdk.data.profile

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.rapidsos.emergencydatasdk.data.profile.values.AddressValue

data class Address(@SerializedName("value")
                   @Expose
                   var value: List<AddressValue>? = mutableListOf(),

                   @SerializedName("type")
                   @Expose
                   var type: String? = "",

                   @SerializedName("display_name")
                   @Expose
                   var displayName: String? = "")
package com.rapidsos.emergencydatasdk.data.location

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LocationContactValue(@SerializedName("label") @Expose var label: String = "",
                                @SerializedName("full_name") @Expose var fullName: String = "",
                                @SerializedName("phone") @Expose var phone: String = "",
                                @SerializedName("email") @Expose var email: String = "",
                                @SerializedName("note") @Expose var note: String = "")
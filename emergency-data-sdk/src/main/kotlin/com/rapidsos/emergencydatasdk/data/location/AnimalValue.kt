package com.rapidsos.emergencydatasdk.data.location

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AnimalValue(@SerializedName("label") @Expose var label: String = "",
                       @SerializedName("full_name") @Expose var fullName: String = "",
                       @SerializedName("species") @Expose var species: String = "",
                       @SerializedName("photo") @Expose var photo: String = "",
                       @SerializedName("note") @Expose var note: String = "")

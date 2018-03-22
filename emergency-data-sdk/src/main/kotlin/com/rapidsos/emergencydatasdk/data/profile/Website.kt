package com.rapidsos.emergencydatasdk.data.profile

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.rapidsos.emergencydatasdk.data.profile.values.WebsiteValue

data class Website(@SerializedName("value") @Expose var value: List<WebsiteValue> = arrayListOf(),
                   @SerializedName("type") @Expose var type: String = "",
                   @SerializedName("display_name") @Expose var displayName: String = "")
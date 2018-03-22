package com.rapidsos.emergencydatasdk.data.profile

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.rapidsos.emergencydatasdk.data.profile.values.LanguageValue
import java.util.*

data class Language(@SerializedName("value") @Expose var value: List<LanguageValue> = ArrayList(),
                    @SerializedName("type") @Expose var type: String = "",
                    @SerializedName("display_name") @Expose var displayName: String = "")
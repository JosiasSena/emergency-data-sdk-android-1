package com.rapidsos.emergencydatasdk.data.network_response

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author Josias Sena
 */
@Entity
data class CallerId(@PrimaryKey @SerializedName("id") @Expose var id: Long = 0,
                    @SerializedName("caller_id") @Expose var callerId: String = "")
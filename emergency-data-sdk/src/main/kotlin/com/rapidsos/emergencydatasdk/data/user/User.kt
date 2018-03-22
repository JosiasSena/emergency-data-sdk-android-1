package com.rapidsos.emergencydatasdk.data.user

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author Josias Sena
 */
@Entity
data class User @JvmOverloads constructor(@PrimaryKey
                                          @SerializedName("id")
                                          var id: Long = 0,

                                          @SerializedName("username")
                                          var username: String? = null,

                                          @SerializedName("email")
                                          var email: String? = null,

                                          @SerializedName("password")
                                          var password: String? = null,

                                          @SerializedName("created")
                                          @Expose
                                          var created: String? = null,

                                          @SerializedName("modified")
                                          @Expose
                                          var modified: String? = null)
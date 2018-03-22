package com.rapidsos.emergencydatasdk.data.location

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.rapidsos.emergencydatasdk.data.profile.Address

@Entity
data class Location(@PrimaryKey
                    @SerializedName("id")
                    @Expose
                    var id: Int = 0,

                    @SerializedName("address")
                    @Expose
                    var address: Address? = null,

                    @SerializedName("animal")
                    @Expose
                    var animal: Animal? = null,

                    @SerializedName("comment")
                    @Expose
                    var comment: Comment? = null,

                    @SerializedName("external_data_portal")
                    @Expose
                    var externalDataPortal: ExternalDataPortal? = null,

                    @SerializedName("fixed_video_feed")
                    @Expose
                    var fixedVideoFeed: FixedVideoFeed? = null,

                    @SerializedName("floorplan")
                    @Expose
                    var floorplan: Floorplan? = null,

                    @SerializedName("location_contact")
                    @Expose
                    var locationContact: LocationContact? = null,

                    @SerializedName("location_name")
                    @Expose
                    var locationName: LocationName? = null,

                    @SerializedName("mobile_video_feed")
                    @Expose
                    var mobileVideoFeed: MobileVideoFeed? = null,

                    @SerializedName("updated_time")
                    @Expose
                    var updatedTime: Int? = null)
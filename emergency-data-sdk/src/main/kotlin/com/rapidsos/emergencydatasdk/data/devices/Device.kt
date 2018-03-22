package com.rapidsos.emergencydatasdk.data.devices

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity
data class Device(@PrimaryKey(autoGenerate = true)
                  @SerializedName("id")
                  @Expose
                  var id: Int? = null,

                  @SerializedName("airbags_deployed")
                  @Expose(serialize = false)
                  val airbagsDeployed: AirbagsDeployed? = null,

                  @SerializedName("battery_power")
                  @Expose
                  var batteryPower: BatteryPower? = null,

                  @SerializedName("blood_glucose")
                  @Expose
                  var bloodGlucose: BloodGlucose? = null,

                  @SerializedName("blood_oxygen_saturation")
                  @Expose
                  var bloodOxygenSaturation: BloodOxygenSaturation? = null,

                  @SerializedName("body_temperature")
                  @Expose
                  var bodyTemperature: BodyTemperature? = null,

                  @SerializedName("break_in_detected")
                  @Expose(serialize = false)
                  val breakInDetected: BreakInDetected? = null,

                  @SerializedName("callback_phone")
                  @Expose
                  var callbackPhone: CallbackPhone? = null,

                  @SerializedName("carbon_dioxide_detected")
                  @Expose(serialize = false)
                  val carbonDioxideDetected: CarbonDioxideDetected? = null,

                  @SerializedName("carbon_monoxide_detected")
                  @Expose(serialize = false)
                  val carbonMonoxideDetected: CarbonMonoxideDetected? = null,

                  @SerializedName("classification")
                  @Expose
                  var classification: Classification? = null,

                  @SerializedName("color")
                  @Expose
                  var color: Color? = null,

                  @SerializedName("comment")
                  @Expose
                  var comment: Comment? = null,

                  @SerializedName("crash_pulse")
                  @Expose
                  var crashPulse: CrashPulse? = null,

                  @SerializedName("crash_severity")
                  @Expose(serialize = false)
                  val crashSeverity: CrashSeverity? = null,

                  @SerializedName("device_name")
                  @Expose
                  var deviceName: DeviceName? = null,

                  @SerializedName("ekg12")
                  @Expose
                  var ekg12: Ekg12? = null,

                  @SerializedName("ekg4")
                  @Expose
                  var ekg4: Ekg4? = null,

                  @SerializedName("fire_detected")
                  @Expose(serialize = false)
                  val fireDetected: FireDetected? = null,

                  @SerializedName("flooding_detected")
                  @Expose(serialize = false)
                  val floodingDetected: FloodingDetected? = null,

                  @SerializedName("gas_detected")
                  @Expose(serialize = false)
                  val gasDetected: GasDetected? = null,

                  @SerializedName("hardware_description")
                  @Expose
                  var hardwareDescription: HardwareDescription? = null,

                  @SerializedName("heart_rate")
                  @Expose
                  var heartRate: HeartRate? = null,

                  @SerializedName("impact_speed")
                  @Expose(serialize = false)
                  val impactSpeed: ImpactSpeed? = null,

                  @SerializedName("license_plate")
                  @Expose
                  var licensePlate: LicensePlate? = null,

                  @SerializedName("manufacturer")
                  @Expose
                  var manufacturer: Manufacturer? = null,

                  @SerializedName("model")
                  @Expose
                  var model: Model? = null,

                  @SerializedName("network_carrier_registration")
                  @Expose
                  var networkCarrierRegistration: NetworkCarrierRegistration? = null,

                  @SerializedName("occupant_count")
                  @Expose(serialize = false)
                  val occupantCount: OccupantCount? = null,

                  @SerializedName("respiratory_rate")
                  @Expose
                  var respiratoryRate: RespiratoryRate? = null,

                  @SerializedName("rollover_count")
                  @Expose(serialize = false)
                  val rolloverCount: RolloverCount? = null,

                  @SerializedName("rollover_detected")
                  @Expose(serialize = false)
                  val rolloverDetected: RolloverDetected? = null,

                  @SerializedName("seatbelted_occupant_count")
                  @Expose(serialize = false)
                  val seatbeltedOccupantCount: SeatbeltedOccupantCount? = null,

                  @SerializedName("steps_taken")
                  @Expose
                  var stepsTaken: StepsTaken? = null,

                  @SerializedName("unique_id")
                  @Expose
                  var uniqueId: UniqueId? = null,

                  @SerializedName("updated_time")
                  @Expose(serialize = false)
                  val updatedTime: Int? = null,

                  @SerializedName("user_agent")
                  @Expose
                  var userAgent: UserAgent? = null,

                  @SerializedName("vehicle_orientation")
                  @Expose(serialize = false)
                  val vehicleOrientation: VehicleOrientation? = null,

                  @SerializedName("video_feed")
                  @Expose
                  var videoFeed: VideoFeed? = null)

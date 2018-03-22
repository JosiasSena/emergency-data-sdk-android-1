package com.rapidsos.emergencydatasdk

/**
 * ###################### Test Responses ######################
 */
internal const val TEST_UNAUTHORIZED_RESPONSE = "{\"detail\": \"Invalid user credentials.\"}"
internal const val TEST_TOO_MANY_REQUESTS_RESPONSE = "{\"detail\": \"Too many requests. Please try again later\"}"
internal const val NOT_FOUND_RESPONSE = "{\n" +
        "    \"detail\": \"Not found.\"\n" +
        "}"

internal const val REGISTRATION_SUCCESS_RESPONSE = "{\n" +
        "    \"created\": \"2017-12-20T18:34:21.050989+00:00\",\n" +
        "    \"email\": \"test@test.com\",\n" +
        "    \"id\": 0,\n" +
        "    \"modified\": null,\n" +
        "    \"username\": \"testusername\"\n" +
        "}"

internal const val REGISTRATION_USER_WITH_USERNAME_EXISTS_RESPONSE = "{\n" +
        "    \"detail\": \"User with username `some_username` already exists.\"\n" +
        "}"

internal const val REGISTRATION_INVALID_USERNAME_LENGTH_RESPONSE = "{\n" +
        "    \"detail\": {\n" +
        "        \"username\": [\n" +
        "            \"Length must be between 7 and 150.\"\n" +
        "        ]\n" +
        "    }\n" +
        "}"

internal const val REGISTRATION_INVALID_PASSWORD_LENGTH_RESPONSE = "{\n" +
        "    \"detail\": {\n" +
        "        \"password\": [\n" +
        "            \"Length must be between 8 and 150.\"\n" +
        "        ]\n" +
        "    }\n" +
        "}"

internal const val REGISTRATION_INVALID_EMAIL_RESPONSE = "{\n" +
        "    \"detail\": {\n" +
        "        \"email\": [\n" +
        "            \"Not a valid email address.\"\n" +
        "        ]\n" +
        "    }\n" +
        "}"

internal const val REGISTRATION_ALL_FIELDS_INVALID__RESPONSE = "{\n" +
        "    \"detail\": {\n" +
        "        \"email\": [\n" +
        "            \"Not a valid email address.\"\n" +
        "        ],\n" +
        "        \"password\": [\n" +
        "            \"Length must be between 8 and 150.\"\n" +
        "        ],\n" +
        "        \"username\": [\n" +
        "            \"Length must be between 7 and 150.\"\n" +
        "        ]\n" +
        "    }\n" +
        "}"

internal const val ACCESS_TOKEN_SUCCESS_RESPONSE = "{\n" +
        "    \"access_token\": \"sCGstIloMEOfc2Ved25UZcsU45EL\",\n" +
        "    \"token_type\": \"BearerToken\",\n" +
        "    \"issued_at\": \"1513793277823\",\n" +
        "    \"expires_in\": \"3599\"\n" +
        "}"

internal const val ACCESS_TOKEN_INVALID_RESPONSE = "{\n" +
        "    \"fault\": {\n" +
        "        \"faultstring\": \"Invalid Access Token\",\n" +
        "        \"detail\": {\n" +
        "            \"errorcode\": \"keymanagement.service.invalid_access_token\"\n" +
        "        }\n" +
        "    }\n" +
        "}"

internal const val ACCESS_TOKEN_EXPIRED_RESPONSE = "{\n" +
        "    \"fault\": {\n" +
        "        \"faultstring\": \"Access Token expired\",\n" +
        "        \"detail\": {\n" +
        "            \"errorcode\": \"keymanagement.service.access_token_expired\"\n" +
        "        }\n" +
        "    }\n" +
        "}"

internal const val SESSION_TOKEN_SUCCESS_RESPONSE = "{\n" +
        "    \"access_token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYmYiOjE1MTIxNDk" +
        "3ODAsImlkZW50aXR5IjoxMzMsImV4cCI6MTUxMjE1MzM4MCwiaWF0IjoxNTEyMTQ5NzgwfQ.6ysLVirM" +
        "kWGeB0VKCDC9I9BYbKFfKlTLNNr03cb-3MI\",\n" +
        "    \"token_type\": \"BearerToken\",\n" +
        "    \"issued_at\": \"1512149780354\",\n" +
        "    \"expires_in\": \"3599\",\n" +
        "    \"refresh_token\": \"Nqnt4vs0dKblsGzp08FJskm2IyRUfTW9\",\n" +
        "    \"refresh_token_issued_at\": \"1512149780354\",\n" +
        "    \"refresh_token_expires_in\": \"0\"\n" +
        "}"

internal const val SESSION_TOKEN_INVALID_RESPONSE = "{\n" +
        "    \"access_token\": \"\",\n" + // empty access token makes this fail
        "    \"token_type\": \"BearerToken\",\n" +
        "    \"issued_at\": \"1513793435911\",\n" +
        "    \"expires_in\": \"3599\",\n" +
        "    \"refresh_token\": \"bsRV99hoziMnTEUoad25S9GKepsaX3KT\",\n" +
        "    \"refresh_token_issued_at\": \"1513793435911\",\n" +
        "    \"refresh_token_expires_in\": \"0\"\n" +
        "}"

internal const val RESET_PWD_SUCCESS_RESPONSE = "{}"

internal const val VALIDATE_PIN_INVALID_PIN_RESPONSE = "{\"detail\": \"Validation code is invalid\"}"

internal const val CALLER_ID_SUCCESS_RESPONSE = "{\n" +
        "  \"caller_id\": \"15555555555\", \n" +
        "  \"id\": 8\n" +
        "}\n"

internal const val PROFILE_SUCCESS_RESPONSE = "{\n" +
        "    \"address\": {\n" +
        "        \"display_name\": \"Known Addresses\",\n" +
        "        \"type\": \"address\",\n" +
        "        \"updated_time\": 1513645936917,\n" +
        "        \"value\": [\n" +
        "            {\n" +
        "                \"country_code\": \"US\",\n" +
        "                \"label\": \"Home\",\n" +
        "                \"latitude\": 0,\n" +
        "                \"locality\": \"Bronx\",\n" +
        "                \"longitude\": 0,\n" +
        "                \"note\": \"\",\n" +
        "                \"postal_code\": \"10457\",\n" +
        "                \"region\": \"NY\",\n" +
        "                \"street_address\": \"1700 grand concourse\"\n" +
        "            }\n" +
        "        ]\n" +
        "    },\n" +
        "    \"allergy\": {\n" +
        "        \"display_name\": \"Allergies\",\n" +
        "        \"type\": \"string\",\n" +
        "        \"updated_time\": 1513645936918,\n" +
        "        \"value\": [\n" +
        "            \"\"\n" +
        "        ]\n" +
        "    },\n" +
        "    \"disability\": {\n" +
        "        \"display_name\": \"Disabilities\",\n" +
        "        \"type\": \"string\",\n" +
        "        \"updated_time\": 1513645936919,\n" +
        "        \"value\": [\n" +
        "            \"\"\n" +
        "        ]\n" +
        "    },\n" +
        "    \"ethnicity\": {\n" +
        "        \"display_name\": \"Ethnicity\",\n" +
        "        \"type\": \"string\",\n" +
        "        \"updated_time\": 1513645936919,\n" +
        "        \"value\": [\n" +
        "            \"\"\n" +
        "        ]\n" +
        "    },\n" +
        "    \"full_name\": {\n" +
        "        \"display_name\": \"Name\",\n" +
        "        \"type\": \"string\",\n" +
        "        \"updated_time\": 1513645936920,\n" +
        "        \"value\": [\n" +
        "            \"\"\n" +
        "        ]\n" +
        "    },\n" +
        "    \"id\": 14,\n" +
        "    \"medical_condition\": {\n" +
        "        \"display_name\": \"Medical Conditions\",\n" +
        "        \"type\": \"string\",\n" +
        "        \"updated_time\": 1513645936921,\n" +
        "        \"value\": [\n" +
        "            \"\"\n" +
        "        ]\n" +
        "    },\n" +
        "    \"medical_note\": {\n" +
        "        \"display_name\": \"Medical Notes\",\n" +
        "        \"type\": \"string\",\n" +
        "        \"updated_time\": 1513645936921,\n" +
        "        \"value\": [\n" +
        "            \"\"\n" +
        "        ]\n" +
        "    },\n" +
        "    \"medication\": {\n" +
        "        \"display_name\": \"Medications\",\n" +
        "        \"type\": \"string\",\n" +
        "        \"updated_time\": 1513645936922,\n" +
        "        \"value\": [\n" +
        "            \"\"\n" +
        "        ]\n" +
        "    }\n" +
        "}"

internal const val LOCATIONS_SUCCESS_RESPONSE = "[\n" +
        "    {\n" +
        "        \"address\": {\n" +
        "            \"display_name\": \"Known Addresses\",\n" +
        "            \"type\": \"address\",\n" +
        "            \"updated_time\": 1515706811503,\n" +
        "            \"value\": [\n" +
        "                {\n" +
        "                    \"country_code\": \"US\",\n" +
        "                    \"label\": \"Home\",\n" +
        "                    \"latitude\": 10181810,\n" +
        "                    \"locality\": \"NY\",\n" +
        "                    \"longitude\": 123213,\n" +
        "                    \"note\": \"This is my home!\",\n" +
        "                    \"postal_code\": \"10457\",\n" +
        "                    \"region\": \"NYC\",\n" +
        "                    \"street_address\": \"324 Avenue of the americas\"\n" +
        "                }\n" +
        "            ]\n" +
        "        },\n" +
        "        \"animal\": {\n" +
        "            \"display_name\": \"Animals\",\n" +
        "            \"type\": \"animal\",\n" +
        "            \"updated_time\": 1515706811504,\n" +
        "            \"value\": [\n" +
        "                {\n" +
        "                    \"full_name\": \"Max\",\n" +
        "                    \"label\": \"My favorite pet\",\n" +
        "                    \"note\": \"This is my best friend\",\n" +
        "                    \"photo\": \"https://www.rapidsos.com\",\n" +
        "                    \"species\": \"Squirrel\"\n" +
        "                }\n" +
        "            ]\n" +
        "        },\n" +
        "        \"id\": 1\n" +
        "    }\n" +
        "]"

internal const val LOCATION_SUCCESS_RESPONSE = "{\n" +
        "    \"address\": {\n" +
        "        \"display_name\": \"Known Addresses\",\n" +
        "        \"type\": \"address\",\n" +
        "        \"updated_time\": 1515730058613,\n" +
        "        \"value\": [\n" +
        "            {\n" +
        "                \"country_code\": \"US\",\n" +
        "                \"label\": \"Home\",\n" +
        "                \"latitude\": 12334567,\n" +
        "                \"locality\": \"NY\",\n" +
        "                \"longitude\": 123213,\n" +
        "                \"note\": \"This is my home..my lovely, lovely home!\",\n" +
        "                \"postal_code\": \"10457\",\n" +
        "                \"region\": \"NYC\",\n" +
        "                \"street_address\": \"124 Manhattan Ave\"\n" +
        "            }\n" +
        "        ]\n" +
        "    },\n" +
        "    \"animal\": {\n" +
        "        \"display_name\": \"Animals\",\n" +
        "        \"type\": \"animal\",\n" +
        "        \"updated_time\": 1515730058614,\n" +
        "        \"value\": [\n" +
        "            {\n" +
        "                \"full_name\": \"Max\",\n" +
        "                \"label\": \"my pet\",\n" +
        "                \"note\": \"This is my best friend\",\n" +
        "                \"photo\": \"http://www.goog.com\",\n" +
        "                \"species\": \"Squirrel\"\n" +
        "            }\n" +
        "        ]\n" +
        "    },\n" +
        "    \"id\": 6\n" +
        "}"

internal const val DEVICES_SUCCESS_RESPONSE = "[\n" +
        "    {\n" +
        "        \"battery_power\": {\n" +
        "            \"display_name\": \"Battery Level\",\n" +
        "            \"type\": \"realtime-metric\",\n" +
        "            \"units\": \"percentage\",\n" +
        "            \"updated_time\": 1516832577517,\n" +
        "            \"value\": [\n" +
        "                {\n" +
        "                    \"data_feed\": \"https://live.rapidsos.com/9eab6a993f294252a5ea3ffca6cf886b\",\n" +
        "                    \"label\": \"Smartwatch\",\n" +
        "                    \"note\": \"Long lasting battery\",\n" +
        "                    \"protocol\": \"https\"\n" +
        "                }\n" +
        "            ]\n" +
        "        },\n" +
        "        \"blood_glucose\": {\n" +
        "            \"display_name\": \"Glucose (mg/DL)\",\n" +
        "            \"type\": \"realtime-metric\",\n" +
        "            \"units\": \"mg/DL\",\n" +
        "            \"updated_time\": 1516832577516,\n" +
        "            \"value\": [\n" +
        "                {\n" +
        "                    \"data_feed\": \"https://live.rapidsos.com/9eab6a993f294252a5ea3ffca6cf886b\",\n" +
        "                    \"label\": \"Smartwatch\",\n" +
        "                    \"note\": \"Glucose readings from smartwatch\",\n" +
        "                    \"protocol\": \"https\"\n" +
        "                }\n" +
        "            ]\n" +
        "        },\n" +
        "        \"blood_oxygen_saturation\": {\n" +
        "            \"display_name\": \"Oxygen Saturation (% SpO2)\",\n" +
        "            \"type\": \"realtime-metric\",\n" +
        "            \"units\": \"%spO2\",\n" +
        "            \"updated_time\": 1516832577523,\n" +
        "            \"value\": [\n" +
        "                {\n" +
        "                    \"data_feed\": \"https://live.rapidsos.com/9eab6a993f294252a5ea3ffca6cf886b\",\n" +
        "                    \"label\": \"Smartwatch\",\n" +
        "                    \"note\": \"Blood oxygen saturation from smartwatch\",\n" +
        "                    \"protocol\": \"https\"\n" +
        "                }\n" +
        "            ]\n" +
        "        },\n" +
        "        \"body_temperature\": {\n" +
        "            \"display_name\": \"Body Temperature (F)\",\n" +
        "            \"type\": \"realtime-metric\",\n" +
        "            \"units\": \"F\",\n" +
        "            \"updated_time\": 1516832577513,\n" +
        "            \"value\": [\n" +
        "                {\n" +
        "                    \"data_feed\": \"https://live.rapidsos.com/9eab6a993f294252a5ea3ffca6cf886b\",\n" +
        "                    \"label\": \"Smartwatch\",\n" +
        "                    \"note\": \"Fahrenheit body temp readings\",\n" +
        "                    \"protocol\": \"https\"\n" +
        "                }\n" +
        "            ]\n" +
        "        },\n" +
        "        \"callback_phone\": {\n" +
        "            \"display_name\": \"Callback Number\",\n" +
        "            \"type\": \"phone-number\",\n" +
        "            \"updated_time\": 1516832577506,\n" +
        "            \"value\": [\n" +
        "                {\n" +
        "                    \"label\": \"cell\",\n" +
        "                    \"number\": \"+12345678901\"\n" +
        "                }\n" +
        "            ]\n" +
        "        },\n" +
        "        \"classification\": {\n" +
        "            \"display_name\": \"Device Classification\",\n" +
        "            \"type\": \"string\",\n" +
        "            \"updated_time\": 1516832577514,\n" +
        "            \"value\": [\n" +
        "                \"smart-phone-app\"\n" +
        "            ]\n" +
        "        },\n" +
        "        \"color\": {\n" +
        "            \"display_name\": \"Color\",\n" +
        "            \"type\": \"string\",\n" +
        "            \"updated_time\": 1516832577512,\n" +
        "            \"value\": [\n" +
        "                \"Red\"\n" +
        "            ]\n" +
        "        },\n" +
        "        \"comment\": {\n" +
        "            \"display_name\": \"Comments\",\n" +
        "            \"type\": \"string\",\n" +
        "            \"updated_time\": 1516832577528,\n" +
        "            \"value\": [\n" +
        "                \"I have a smartwatch and smartphone\"\n" +
        "            ]\n" +
        "        },\n" +
        "        \"crash_pulse\": {\n" +
        "            \"display_name\": \"Crash Pulse\",\n" +
        "            \"type\": \"image-url\",\n" +
        "            \"updated_time\": 1516832577507,\n" +
        "            \"value\": [\n" +
        "                {\n" +
        "                    \"label\": \"Crash Pulse\",\n" +
        "                    \"note\": \"Pulse of the crash\",\n" +
        "                    \"url\": \"https://cdn.rapidsos.com/63949328-ea28-4178-b0e9-3caff01a204a.png\"\n" +
        "                }\n" +
        "            ]\n" +
        "        },\n" +
        "        \"device_name\": {\n" +
        "            \"display_name\": \"Device Name\",\n" +
        "            \"type\": \"string\",\n" +
        "            \"updated_time\": 1516832577511,\n" +
        "            \"value\": [\n" +
        "                \"John Doe's iPhone\"\n" +
        "            ]\n" +
        "        },\n" +
        "        \"ekg12\": {\n" +
        "            \"display_name\": \"EKG 12-lead\",\n" +
        "            \"type\": \"image-url\",\n" +
        "            \"updated_time\": 1516832577519,\n" +
        "            \"value\": [\n" +
        "                {\n" +
        "                    \"label\": \"EKG12\",\n" +
        "                    \"note\": \"EKG reading\",\n" +
        "                    \"url\": \"https://cdn.rapidsos.com/63949328-ea28-4178-b0e9-3caff01a204a.png\"\n" +
        "                }\n" +
        "            ]\n" +
        "        },\n" +
        "        \"ekg4\": {\n" +
        "            \"display_name\": \"EKG 4-lead\",\n" +
        "            \"type\": \"image-url\",\n" +
        "            \"updated_time\": 1516832577510,\n" +
        "            \"value\": [\n" +
        "                {\n" +
        "                    \"label\": \"EKG4\",\n" +
        "                    \"note\": \"EKG reading\",\n" +
        "                    \"url\": \"https://cdn.rapidsos.com/63949328-ea28-4178-b0e9-3caff01a204a.png\"\n" +
        "                }\n" +
        "            ]\n" +
        "        },\n" +
        "        \"hardware_description\": {\n" +
        "            \"display_name\": \"Device Description\",\n" +
        "            \"type\": \"string\",\n" +
        "            \"updated_time\": 1516832577518,\n" +
        "            \"value\": [\n" +
        "                \"DDR3 RAM\"\n" +
        "            ]\n" +
        "        },\n" +
        "        \"heart_rate\": {\n" +
        "            \"display_name\": \"Heart Rate (bpm)\",\n" +
        "            \"type\": \"realtime-metric\",\n" +
        "            \"units\": \"bpm\",\n" +
        "            \"updated_time\": 1516832577526,\n" +
        "            \"value\": [\n" +
        "                {\n" +
        "                    \"data_feed\": \"https://cdn.rapidsos.com/63949328-ea28-4178-b0e9-3caff01a204a.png\",\n" +
        "                    \"label\": \"Smartphone\",\n" +
        "                    \"note\": \"Heartrate estimates from smartphone\",\n" +
        "                    \"protocol\": \"https\"\n" +
        "                }\n" +
        "            ]\n" +
        "        },\n" +
        "        \"id\": 1,\n" +
        "        \"license_plate\": {\n" +
        "            \"display_name\": \"License Plate\",\n" +
        "            \"type\": \"string\",\n" +
        "            \"updated_time\": 1516832577525,\n" +
        "            \"value\": [\n" +
        "                \"2FAST2FURIOUS\"\n" +
        "            ]\n" +
        "        },\n" +
        "        \"manufacturer\": {\n" +
        "            \"display_name\": \"Device Manufacturer\",\n" +
        "            \"type\": \"string\",\n" +
        "            \"updated_time\": 1516832577515,\n" +
        "            \"value\": [\n" +
        "                \"Nokia\"\n" +
        "            ]\n" +
        "        },\n" +
        "        \"model\": {\n" +
        "            \"display_name\": \"Device Model\",\n" +
        "            \"type\": \"string\",\n" +
        "            \"updated_time\": 1516832577527,\n" +
        "            \"value\": [\n" +
        "                \"Lumia 800\"\n" +
        "            ]\n" +
        "        },\n" +
        "        \"network_carrier_registration\": {\n" +
        "            \"display_name\": \"Network Carrier\",\n" +
        "            \"type\": \"string\",\n" +
        "            \"updated_time\": 1516832577512,\n" +
        "            \"value\": [\n" +
        "                \"Verizon\"\n" +
        "            ]\n" +
        "        },\n" +
        "        \"respiratory_rate\": {\n" +
        "            \"display_name\": \"Respiratory Rate (bpm)\",\n" +
        "            \"type\": \"realtime-metric\",\n" +
        "            \"updated_time\": 1516832577508,\n" +
        "            \"value\": [\n" +
        "                {\n" +
        "                    \"data_feed\": \"https://cdn.rapidsos.com/63949328-ea28-4178-b0e9-3caff01a204a.png\",\n" +
        "                    \"label\": \"Smartphone\",\n" +
        "                    \"note\": \"Transmittedd from smart device\",\n" +
        "                    \"protocol\": \"https\"\n" +
        "                }\n" +
        "            ]\n" +
        "        },\n" +
        "        \"steps_taken\": {\n" +
        "            \"display_name\": \"Steps Taken\",\n" +
        "            \"type\": \"realtime-metric\",\n" +
        "            \"updated_time\": 1516832577509,\n" +
        "            \"value\": [\n" +
        "                {\n" +
        "                    \"data_feed\": \"https://cdn.rapidsos.com/63949328-ea28-4178-b0e9-3caff01a204a.png\",\n" +
        "                    \"label\": \"Fitbit\",\n" +
        "                    \"note\": \"Average 10,000 a day\",\n" +
        "                    \"protocol\": \"https\"\n" +
        "                }\n" +
        "            ]\n" +
        "        },\n" +
        "        \"unique_id\": {\n" +
        "            \"display_name\": \"Unique Device ID\",\n" +
        "            \"type\": \"device-id\",\n" +
        "            \"updated_time\": 1516832577523,\n" +
        "            \"value\": [\n" +
        "                {\n" +
        "                    \"id\": \"35788104\",\n" +
        "                    \"type\": \"IMEI\"\n" +
        "                }\n" +
        "            ]\n" +
        "        },\n" +
        "        \"user_agent\": {\n" +
        "            \"display_name\": \"User Agent\",\n" +
        "            \"type\": \"string\",\n" +
        "            \"updated_time\": 1516832577528,\n" +
        "            \"value\": [\n" +
        "                \"Mozilla\"\n" +
        "            ]\n" +
        "        },\n" +
        "        \"video_feed\": {\n" +
        "            \"display_name\": \"Video Feed\",\n" +
        "            \"type\": \"video-stream\",\n" +
        "            \"updated_time\": 1516832577522,\n" +
        "            \"value\": [\n" +
        "                {\n" +
        "                    \"format\": \"mjpg\",\n" +
        "                    \"label\": \"cafeteria\",\n" +
        "                    \"latitude\": 40.754475,\n" +
        "                    \"longitude\": -73.989874,\n" +
        "                    \"note\": \"Live dashcam footage\",\n" +
        "                    \"url\": \"https://cdn.rapidsos.com/demo-static-files/dashcam.mp4\"\n" +
        "                }\n" +
        "            ]\n" +
        "        }\n" +
        "    }\n" +
        "]"

internal const val DEVICE_SUCCESS_RESPONSE = "{\n" +
        "    \"battery_power\": {\n" +
        "        \"display_name\": \"Battery Level\",\n" +
        "        \"type\": \"realtime-metric\",\n" +
        "        \"units\": \"percentage\",\n" +
        "        \"updated_time\": 1516832577517,\n" +
        "        \"value\": [\n" +
        "            {\n" +
        "                \"data_feed\": \"https://live.rapidsos.com/9eab6a993f294252a5ea3ffca6cf886b\",\n" +
        "                \"label\": \"Smartwatch\",\n" +
        "                \"note\": \"Long lasting battery\",\n" +
        "                \"protocol\": \"https\"\n" +
        "            }\n" +
        "        ]\n" +
        "    },\n" +
        "    \"blood_glucose\": {\n" +
        "        \"display_name\": \"Glucose (mg/DL)\",\n" +
        "        \"type\": \"realtime-metric\",\n" +
        "        \"units\": \"mg/DL\",\n" +
        "        \"updated_time\": 1516832577516,\n" +
        "        \"value\": [\n" +
        "            {\n" +
        "                \"data_feed\": \"https://live.rapidsos.com/9eab6a993f294252a5ea3ffca6cf886b\",\n" +
        "                \"label\": \"Smartwatch\",\n" +
        "                \"note\": \"Glucose readings from smartwatch\",\n" +
        "                \"protocol\": \"https\"\n" +
        "            }\n" +
        "        ]\n" +
        "    },\n" +
        "    \"blood_oxygen_saturation\": {\n" +
        "        \"display_name\": \"Oxygen Saturation (% SpO2)\",\n" +
        "        \"type\": \"realtime-metric\",\n" +
        "        \"units\": \"%spO2\",\n" +
        "        \"updated_time\": 1516832577523,\n" +
        "        \"value\": [\n" +
        "            {\n" +
        "                \"data_feed\": \"https://live.rapidsos.com/9eab6a993f294252a5ea3ffca6cf886b\",\n" +
        "                \"label\": \"Smartwatch\",\n" +
        "                \"note\": \"Blood oxygen saturation from smartwatch\",\n" +
        "                \"protocol\": \"https\"\n" +
        "            }\n" +
        "        ]\n" +
        "    },\n" +
        "    \"body_temperature\": {\n" +
        "        \"display_name\": \"Body Temperature (F)\",\n" +
        "        \"type\": \"realtime-metric\",\n" +
        "        \"units\": \"F\",\n" +
        "        \"updated_time\": 1516832577513,\n" +
        "        \"value\": [\n" +
        "            {\n" +
        "                \"data_feed\": \"https://live.rapidsos.com/9eab6a993f294252a5ea3ffca6cf886b\",\n" +
        "                \"label\": \"Smartwatch\",\n" +
        "                \"note\": \"Fahrenheit body temp readings\",\n" +
        "                \"protocol\": \"https\"\n" +
        "            }\n" +
        "        ]\n" +
        "    },\n" +
        "    \"callback_phone\": {\n" +
        "        \"display_name\": \"Callback Number\",\n" +
        "        \"type\": \"phone-number\",\n" +
        "        \"updated_time\": 1516832577506,\n" +
        "        \"value\": [\n" +
        "            {\n" +
        "                \"label\": \"cell\",\n" +
        "                \"number\": \"+12345678901\"\n" +
        "            }\n" +
        "        ]\n" +
        "    },\n" +
        "    \"classification\": {\n" +
        "        \"display_name\": \"Device Classification\",\n" +
        "        \"type\": \"string\",\n" +
        "        \"updated_time\": 1516832577514,\n" +
        "        \"value\": [\n" +
        "            \"smart-phone-app\"\n" +
        "        ]\n" +
        "    },\n" +
        "    \"color\": {\n" +
        "        \"display_name\": \"Color\",\n" +
        "        \"type\": \"string\",\n" +
        "        \"updated_time\": 1516832577512,\n" +
        "        \"value\": [\n" +
        "            \"Red\"\n" +
        "        ]\n" +
        "    },\n" +
        "    \"comment\": {\n" +
        "        \"display_name\": \"Comments\",\n" +
        "        \"type\": \"string\",\n" +
        "        \"updated_time\": 1516832577528,\n" +
        "        \"value\": [\n" +
        "            \"I have a smartwatch and smartphone\"\n" +
        "        ]\n" +
        "    },\n" +
        "    \"crash_pulse\": {\n" +
        "        \"display_name\": \"Crash Pulse\",\n" +
        "        \"type\": \"image-url\",\n" +
        "        \"updated_time\": 1516832577507,\n" +
        "        \"value\": [\n" +
        "            {\n" +
        "                \"label\": \"Crash Pulse\",\n" +
        "                \"note\": \"Pulse of the crash\",\n" +
        "                \"url\": \"https://cdn.rapidsos.com/63949328-ea28-4178-b0e9-3caff01a204a.png\"\n" +
        "            }\n" +
        "        ]\n" +
        "    },\n" +
        "    \"device_name\": {\n" +
        "        \"display_name\": \"Device Name\",\n" +
        "        \"type\": \"string\",\n" +
        "        \"updated_time\": 1516832577511,\n" +
        "        \"value\": [\n" +
        "            \"John Doe's iPhone\"\n" +
        "        ]\n" +
        "    },\n" +
        "    \"ekg12\": {\n" +
        "        \"display_name\": \"EKG 12-lead\",\n" +
        "        \"type\": \"image-url\",\n" +
        "        \"updated_time\": 1516832577519,\n" +
        "        \"value\": [\n" +
        "            {\n" +
        "                \"label\": \"EKG12\",\n" +
        "                \"note\": \"EKG reading\",\n" +
        "                \"url\": \"https://cdn.rapidsos.com/63949328-ea28-4178-b0e9-3caff01a204a.png\"\n" +
        "            }\n" +
        "        ]\n" +
        "    },\n" +
        "    \"ekg4\": {\n" +
        "        \"display_name\": \"EKG 4-lead\",\n" +
        "        \"type\": \"image-url\",\n" +
        "        \"updated_time\": 1516832577510,\n" +
        "        \"value\": [\n" +
        "            {\n" +
        "                \"label\": \"EKG4\",\n" +
        "                \"note\": \"EKG reading\",\n" +
        "                \"url\": \"https://cdn.rapidsos.com/63949328-ea28-4178-b0e9-3caff01a204a.png\"\n" +
        "            }\n" +
        "        ]\n" +
        "    },\n" +
        "    \"hardware_description\": {\n" +
        "        \"display_name\": \"Device Description\",\n" +
        "        \"type\": \"string\",\n" +
        "        \"updated_time\": 1516832577518,\n" +
        "        \"value\": [\n" +
        "            \"DDR3 RAM\"\n" +
        "        ]\n" +
        "    },\n" +
        "    \"heart_rate\": {\n" +
        "        \"display_name\": \"Heart Rate (bpm)\",\n" +
        "        \"type\": \"realtime-metric\",\n" +
        "        \"units\": \"bpm\",\n" +
        "        \"updated_time\": 1516832577526,\n" +
        "        \"value\": [\n" +
        "            {\n" +
        "                \"data_feed\": \"https://cdn.rapidsos.com/63949328-ea28-4178-b0e9-3caff01a204a.png\",\n" +
        "                \"label\": \"Smartphone\",\n" +
        "                \"note\": \"Heartrate estimates from smartphone\",\n" +
        "                \"protocol\": \"https\"\n" +
        "            }\n" +
        "        ]\n" +
        "    },\n" +
        "    \"id\": 1,\n" +
        "    \"license_plate\": {\n" +
        "        \"display_name\": \"License Plate\",\n" +
        "        \"type\": \"string\",\n" +
        "        \"updated_time\": 1516832577525,\n" +
        "        \"value\": [\n" +
        "            \"2FAST2FURIOUS\"\n" +
        "        ]\n" +
        "    },\n" +
        "    \"manufacturer\": {\n" +
        "        \"display_name\": \"Device Manufacturer\",\n" +
        "        \"type\": \"string\",\n" +
        "        \"updated_time\": 1516832577515,\n" +
        "        \"value\": [\n" +
        "            \"Nokia\"\n" +
        "        ]\n" +
        "    },\n" +
        "    \"model\": {\n" +
        "        \"display_name\": \"Device Model\",\n" +
        "        \"type\": \"string\",\n" +
        "        \"updated_time\": 1516832577527,\n" +
        "        \"value\": [\n" +
        "            \"Lumia 800\"\n" +
        "        ]\n" +
        "    },\n" +
        "    \"network_carrier_registration\": {\n" +
        "        \"display_name\": \"Network Carrier\",\n" +
        "        \"type\": \"string\",\n" +
        "        \"updated_time\": 1516832577512,\n" +
        "        \"value\": [\n" +
        "            \"Verizon\"\n" +
        "        ]\n" +
        "    },\n" +
        "    \"respiratory_rate\": {\n" +
        "        \"display_name\": \"Respiratory Rate (bpm)\",\n" +
        "        \"type\": \"realtime-metric\",\n" +
        "        \"updated_time\": 1516832577508,\n" +
        "        \"value\": [\n" +
        "            {\n" +
        "                \"data_feed\": \"https://cdn.rapidsos.com/63949328-ea28-4178-b0e9-3caff01a204a.png\",\n" +
        "                \"label\": \"Smartphone\",\n" +
        "                \"note\": \"Transmittedd from smart device\",\n" +
        "                \"protocol\": \"https\"\n" +
        "            }\n" +
        "        ]\n" +
        "    },\n" +
        "    \"steps_taken\": {\n" +
        "        \"display_name\": \"Steps Taken\",\n" +
        "        \"type\": \"realtime-metric\",\n" +
        "        \"updated_time\": 1516832577509,\n" +
        "        \"value\": [\n" +
        "            {\n" +
        "                \"data_feed\": \"https://cdn.rapidsos.com/63949328-ea28-4178-b0e9-3caff01a204a.png\",\n" +
        "                \"label\": \"Fitbit\",\n" +
        "                \"note\": \"Average 10,000 a day\",\n" +
        "                \"protocol\": \"https\"\n" +
        "            }\n" +
        "        ]\n" +
        "    },\n" +
        "    \"unique_id\": {\n" +
        "        \"display_name\": \"Unique Device ID\",\n" +
        "        \"type\": \"device-id\",\n" +
        "        \"updated_time\": 1516832577523,\n" +
        "        \"value\": [\n" +
        "            {\n" +
        "                \"id\": \"35788104\",\n" +
        "                \"type\": \"IMEI\"\n" +
        "            }\n" +
        "        ]\n" +
        "    },\n" +
        "    \"user_agent\": {\n" +
        "        \"display_name\": \"User Agent\",\n" +
        "        \"type\": \"string\",\n" +
        "        \"updated_time\": 1516832577528,\n" +
        "        \"value\": [\n" +
        "            \"Mozilla\"\n" +
        "        ]\n" +
        "    },\n" +
        "    \"video_feed\": {\n" +
        "        \"display_name\": \"Video Feed\",\n" +
        "        \"type\": \"video-stream\",\n" +
        "        \"updated_time\": 1516832577522,\n" +
        "        \"value\": [\n" +
        "            {\n" +
        "                \"format\": \"mjpg\",\n" +
        "                \"label\": \"cafeteria\",\n" +
        "                \"latitude\": 40.754475,\n" +
        "                \"longitude\": -73.989874,\n" +
        "                \"note\": \"Live dashcam footage\",\n" +
        "                \"url\": \"https://cdn.rapidsos.com/demo-static-files/dashcam.mp4\"\n" +
        "            }\n" +
        "        ]\n" +
        "    }\n" +
        "}"
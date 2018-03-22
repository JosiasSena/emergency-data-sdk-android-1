[![codecov](https://codecov.io/gh/RapidSOS/emergency-data-sdk-android/branch/develop/graph/badge.svg?token=xyxkO6diLw)](https://codecov.io/gh/RapidSOS/emergency-data-sdk-android)

# Emergency Data Android SDK

The RapidSOS SDK Emergency Data SDK enables the RapidSOS Android solution to supply additional
information about the user during an emergency call. Only official 9-1-1 centers and emergency
responders can securely access data from the RapidSOS Clearinghouse, and only through their
official call-taking and dispatching software.

The emergency data schema consists of several core object types:

- `User` - the authenticated User account, used to access and edit the user's information.

- `CallerId` - the verified phone number belonging to the user. Users must verify that a phone number belongs to them in order for their emergency information to be made available to public safety officials.

- `Profile` - personal information about the user, including Medical info, emergency contacts, demographic info.

- `Device` - information about the user's device. This can be identifying information about a phone or other smart device - make, model, unique ID. It can also contain links to live information being reported by the device, such as the live camera feed from a smart home camera or connected car.

- `Location` - a location listed by the user as one of their habitual locations - home, work, etc.

## Getting Started

### Download
On the project level `build.gradle` file add:

```groovy
    allprojects {
        repositories {

            // For release/stable versions
            maven {
                url "http://rapidsos.jfrog.io/rapidsos/rapidsos-android"
                credentials {
                    username 'userName'
                    password 'password'
                }
            }

            // For the snapshot versions
            maven {
                url mavenUrlSnapshot
                credentials {
                    username 'userName'
                    password 'password'
                }
            }
        }
    }
```

The `username` and `password` will be provided by RapidSOS.

On the app level build.gradle file add:

```groovy
    dependencies {
        compile "com.rapidsos:emergencydatasdk:$latest_sdk_version"
    }
```

The latest SDK version can be found under releases.

### Initializing the SDK
The Only thing required before using the SDK is initializing it though the `RainSDK` class.
Preferably in your `Application` class.

```kotlin
class App : Application() {

    companion object {
        private const val CLIENT_ID = "your_rapidsos_cliend_id"
        private const val CLIENT_SECRET = "your_rapidsos_client_secret"
    }

    override fun onCreate() {
        super.onCreate()
        RainSDK(this).initialize(CLIENT_ID, CLIENT_SECRET)
    }

}
```

## Guide

A reference application for the emergency data SDK can be found [HERE](https://github.com/RapidSOS/era-android)

This guide covers the following topics:

### Setup
- [Getting a Session](#getting-a-session)
- [User registration](#user-registration)
- [Resetting a users password](#resetting-a-users-password)
- [Creating/updating a profile](#creatingupdating-a-profile)
- [Getting profile information](#getting-profile-information)
- [Pin validation](#pin-validation)

### Getting a Session
A session token is required to perform most operations in the SDK. To get a session token you need
to use the `SessionManager`. The `SessionManager` requires an existing users credentials.

```kotlin
    private fun getSessionToken() {
        val sessionManager = SessionManager()
        sessionManager.getSessionToken("username", "password")
                .subscribe(object : MaybeObserver<SessionToken?> {
                    override fun onSubscribe(d: Disposable) {
                        // Called as soon as the method is called. The disposable is used to stop the
                        // request at any given time if needed by calling d.dispose()
                    }

                    override fun onSuccess(sessionToken: SessionToken) {
                        // Called when a successful response has been returned. The SessionToken to be used
                        // is returned.
                    }

                    override fun onError(e: Throwable) {
                        // Called when an error occurs
                    }

                    override fun onComplete() {
                        // Called once the deferred computation completes normally.
                    }
                })
    }
```
The `SessionToken` is required to make all other requests using the SDK, so it should be stored in a location here it can later be accessed.

Note: The `SessionToken` is short lived (1 hour to be exact), before it needs to be renewed. It is not automatically renewed by the SDK.

### User registration

To register a user the `RegistrationController` is used. A `User` with a username, password, and
email is required.

```kotlin
    private fun registerNewUser() {
        val registrationController = RegistrationController()
        val user = User(username = "username", password = "password", email = "test@email.com")
        registrationController.register(user).subscribe(object : MaybeObserver<User?> {
            override fun onSubscribe(d: Disposable) {
                // Called as soon as the method is called. The disposable is used to stop the
                // request at any given time if needed by calling d.dispose()
            }

            override fun onSuccess(user: User) {
                // Called when a successful response has been returned. The new registered User is
                // returned for confirmation.
            }

            override fun onError(e: Throwable) {
                // Called when an error occurs
            }

            override fun onComplete() {
                // Called once the deferred computation completes normally.
            }
        })
    }
```

When registering a user the following must be true:
 - The username length must be between 7 and 150
 - The password length must be between 8 and 150

### Pin validation
You may want to validate a users phone number with a pin sent through SMS. To do so the
`PinProvider` and `PinValidator` classes can be used. In order to request and validate a pin, a
`SessionToken` is required.

The `PinProvider` sends an SMS with a pin the the phone number specified.

```kotlin
    private fun requestPin() {
        val pinProvider = PinProvider()
        pinProvider.requestPin(sessionToken, "15555555555")
                .subscribe(object : MaybeObserver<Response<CallerId>> {
                    override fun onSubscribe(d: Disposable) {
                        // Called as soon as the method is called. The disposable is used to stop the
                        // request at any given time if needed by calling d.dispose()
                    }

                    override fun onSuccess(response: Response<CallerId>) {
                        // Called on success. Returns a response object with a `CallerId` object as
                        // its body as confirmation that the request was successful
                    }

                    override fun onError(e: Throwable) {
                        // Called when an error occurs
                    }

                    override fun onComplete() {
                        // Called once the deferred computation completes normally.
                    }
                })
    }
```
On success an SMS will be sent to the phone number provided with the pin to validate. To validate
a pin received through SMS the `PinValidator` is used.
```kotlin
    private fun validatePin() {
        val pinValidator = PinValidator()
        pinValidator.validatePin(sessionToken, "15555555555", pinProvided)
                .subscribe(object : MaybeObserver<CallerId?> {
                    override fun onSubscribe(d: Disposable) {
                        // Called as soon as the method is called. The disposable is used to stop the
                        // request at any given time if needed by calling d.dispose()
                    }

                    override fun onSuccess(callerId: CallerId) {
                        // Called on success. Returns a `CallerId` object as confirmation that the
                        // request was successful
                    }

                    override fun onError(e: Throwable) {
                        // Called when an error occurs
                    }

                    override fun onComplete() {
                        // Called once the deferred computation completes normally.
                    }
                })
    }
```


### Resetting a users password
To reset a users password, the `PasswordHelper` can be used.

```kotlin
    private fun resetPassword() {
        val passwordHelper = PasswordHelper()
        passwordHelper.resetPassword("email@email.com")
                .subscribe(object : MaybeObserver<Response<ResponseBody>> {
                    override fun onSubscribe(d: Disposable) {
                        // Called as soon as the method is called. The disposable is used to stop the
                        // request at any given time if needed by calling d.dispose()
                    }

                    override fun onSuccess(response: Response<ResponseBody>) {
                        // Called on success. At this point an email will be sent the the email provided.
                    }

                    override fun onError(e: Throwable) {
                        // Called when an error occurs
                    }

                    override fun onComplete() {
                        // Called once the deferred computation completes normally.
                    }
                })
    }
```


### Creating/updating a profile
To create a profile or update a profile, the `ProfileUpdater` is used. In the Emergency Data SDK,
everything is an object. The reason for this is mostly for flexibility.

If the profile does not exist, one will be created. Otherwise, the values for an existing profile
will be updated with the new values provided.

All null values are considered deleted values. For this reason it is recommended to fetch profile information before updating profile information, this way you have access to existing/latest profile information.

Here is an example of a new profile with a phone number and an email address.

```kotlin
    // Create `PhoneNumber` object
    private val phoneNumber: PhoneNumber
        get() {
            val phoneNumbers = ArrayList<PhoneNumberValue>()
            val phoneNumberValue = PhoneNumberValue("Cell", "+15555555555")
            phoneNumbers.add(phoneNumberValue)

            return PhoneNumber(phoneNumbers)
        }

    // Create an `Email` object
    private val email: Email
        get() {
            val emails = ArrayList<EmailValue>()
            val emailValue = EmailValue("Personal", "email@email.com")
            emails.add(emailValue)

            return Email(emails)
        }

    private fun createProfile() {
        val profileUpdater = ProfileUpdater()
        val profile = Profile().apply {
            email = email
            phoneNumber = phoneNumber
        }

        profileUpdater.updatePersonalInfo(sessionToken, profile)
                .subscribe(object : MaybeObserver<Profile?> {
                    override fun onSubscribe(d: Disposable) {
                        // Called as soon as the method is called. The disposable is used to stop the
                        // request at any given time if needed by calling d.dispose()
                    }

                    override fun onSuccess(profile: Profile) {
                        // Called on success. Returns a `Profile` object as confirmation that the
                        // request was successful. The profile will also have an auto generated id.
                    }

                    override fun onError(e: Throwable) {
                        // Called when an error occurs
                    }

                    override fun onComplete() {
                        // Called once the deferred computation completes normally.
                    }
                })
    }
```

### Getting profile information
In order to get personal information, the `ProfileProvider` needs to be used.

```kotlin
    private fun getProfileInformation() {
        val profileProvider = ProfileProvider()
        profileProvider.getPersonalInfo(sessionToken)
                .subscribe(object : MaybeObserver<Profile?> {
                    override fun onSubscribe(d: Disposable) {
                        // Called as soon as the method is called. The disposable is used to stop the
                        // request at any given time if needed by calling d.dispose()
                    }

                    override fun onSuccess(profile: Profile) {
                        // Called on success. Returns a `Profile` object as confirmation that the
                        // request was successful.
                    }

                    override fun onError(e: Throwable) {
                        // Called when an error occurs
                    }

                    override fun onComplete() {
                        // Called once the deferred computation completes normally.
                    }
                })
    }
```
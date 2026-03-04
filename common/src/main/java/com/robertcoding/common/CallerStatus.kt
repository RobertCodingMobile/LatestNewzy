package com.robertcoding.common

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.annotation.StringRes
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.sql.SQLException

/**
 * A comprehensive, generic sealed class for wrapping the result of Network (Retrofit/OkHttp)
 * and Database (Room/SQLite) operations.
 *
 * This class provides type-safe state management with three possible states:
 * - [Loading]: Operation in progress
 * - [Success]: Operation completed successfully with data of type [T]
 * - [Error]: Operation failed with detailed error information
 *
 * ## Usage in Repository Pattern
 *
 * ### Example 1: Network Call with Retrofit
 * ```kotlin
 * class UserRepository(private val api: UserApi) {
 *     suspend fun getUser(id: String): CallerStatus<User> {
 *         return try {
 *             val response = api.getUser(id)
 *             CallerStatus.Success(response)
 *         } catch (e: Exception) {
 *             e.mapToCallerStatus()
 *         }
 *     }
 * }
 * ```
 *
 * ### Example 2: Database Operation with Room
 * ```kotlin
 * class LocalUserRepository(private val userDao: UserDao) {
 *     suspend fun insertUser(user: User): CallerStatus<Unit> {
 *         return try {
 *             userDao.insert(user)
 *             CallerStatus.Success(Unit)
 *         } catch (e: Exception) {
 *             e.mapToCallerStatus()
 *         }
 *     }
 * }
 * ```
 *
 * ### Example 3: ViewModel Usage
 * ```kotlin
 * class UserViewModel(private val repository: UserRepository) : ViewModel() {
 *     private val _userState = MutableStateFlow<CallerStatus<User>>(CallerStatus.Loading)
 *     val userState: StateFlow<CallerStatus<User>> = _userState.asStateFlow()
 *
 *     fun loadUser(id: String) {
 *         viewModelScope.launch {
 *             _userState.value = CallerStatus.Loading
 *             _userState.value = repository.getUser(id)
 *         }
 *     }
 * }
 * ```
 *
 * ### Example 4: Compose UI Consumption
 * ```kotlin
 * @Composable
 * fun UserScreen(viewModel: UserViewModel) {
 *     val userState by viewModel.userState.collectAsState()
 *
 *     when (val state = userState) {
 *         is CallerStatus.Loading -> CircularProgressIndicator()
 *         is CallerStatus.Success -> UserContent(user = state.data)
 *         is CallerStatus.Error -> ErrorMessage(message = state.message.asString())
 *     }
 * }
 * ```
 *
 * @param T The type of data wrapped in the Success state
 */
sealed class CallerStatus<out T> {

    /**
     * Represents a loading state where an operation is in progress.
     * Use this to show loading indicators in the UI.
     */
    data object Loading : CallerStatus<Nothing>()

    /**
     * Represents a successful operation with the resulting data.
     *
     * @param data The successful result of type [T]
     */
    data class Success<T>(val data: T) : CallerStatus<T>()

    /**
     * Represents a failed operation with comprehensive error information.
     *
     * @param throwable The original exception that caused the failure
     * @param errorCode Optional HTTP status code or database error code
     * @param message User-friendly error message wrapped in [UiText] for proper localization
     */
    data class Error(
        val throwable: Throwable,
        val errorCode: Int? = null,
        val message: UiText
    ) : CallerStatus<Nothing>()

    companion object {
        /**
         * Maps a generic [Throwable] to a [CallerStatus.Error] with user-friendly messages.
         *
         * This function intelligently categorizes exceptions into:
         * - **Network Errors**: HttpException, SocketTimeoutException, UnknownHostException
         * - **Database Errors**: SQLiteConstraintException, SQLException
         * - **Generic Errors**: All other throwables
         *
         * @return A [CallerStatus.Error] instance with appropriate error details
         */
        fun <T> Throwable.mapToCallerStatus(): CallerStatus<T> {
            return when (this) {
                // ============================================================
                // NETWORK ERROR HANDLING (Retrofit/OkHttp)
                // ============================================================

                is ClientRequestException -> {
                    val code = response.status.value
                    val message = when (code) {
                        400 -> UiText.DynamicString("Bad Request: The server couldn't understand your request")
                        401 -> UiText.DynamicString("Unauthorized: Please log in to continue")
                        403 -> UiText.DynamicString("Forbidden: You don't have permission to access this resource")
                        404 -> UiText.DynamicString("Not Found: The requested resource doesn't exist")
                        408 -> UiText.DynamicString("Request Timeout: The server took too long to respond")
                        429 -> UiText.DynamicString("Too Many Requests: Please slow down and try again later")
                        500 -> UiText.DynamicString("Server Error: Something went wrong on our end")
                        502 -> UiText.DynamicString("Bad Gateway: The server received an invalid response")
                        503 -> UiText.DynamicString("Service Unavailable: The server is temporarily down")
                        504 -> UiText.DynamicString("Gateway Timeout: The server didn't respond in time")
                        in 400..499 -> UiText.DynamicString("Client Error: Request failed (Code: $code)")
                        in 500..599 -> UiText.DynamicString("Server Error: Service unavailable (Code: $code)")
                        else -> UiText.DynamicString("HTTP Error: Unexpected error occurred (Code: $code)")
                    }
                    Error(
                        throwable = this,
                        errorCode = code,
                        message = message
                    )
                }


                is ServerResponseException -> {
                    Error(
                        this,
                        errorCode = response.status.value,
                        message = UiText.DynamicString("Server error (${response.status.value})")
                    )
                }

                is SocketTimeoutException -> {
                    Error(
                        throwable = this,
                        errorCode = null,
                        message = UiText.DynamicString("Connection Timeout: The server took too long to respond. Please check your connection and try again.")
                    )
                }

                is UnknownHostException -> {
                    Error(
                        throwable = this,
                        errorCode = null,
                        message = UiText.DynamicString("No Internet Connection: Please check your network settings and try again.")
                    )
                }

                is IOException -> {
                    Error(
                        throwable = this,
                        errorCode = null,
                        message = UiText.DynamicString("Network Error: Failed to connect to the server. Please check your internet connection.")
                    )
                }

                // ============================================================
                // DATABASE ERROR HANDLING (Room/SQLite)
                // ============================================================

                is SQLiteConstraintException -> {
                    Error(
                        throwable = this,
                        errorCode = null,
                        message = UiText.DynamicString("Database Constraint Error: The operation violates data integrity rules. This might be a duplicate entry.")
                    )
                }

                is SQLException -> {
                    Error(
                        throwable = this,
                        errorCode = null,
                        message = UiText.DynamicString("Database Error: Failed to perform database operation. Please try again.")
                    )
                }

                // ============================================================
                // GENERIC ERROR HANDLING
                // ============================================================

                else -> {
                    Error(
                        throwable = this,
                        errorCode = null,
                        message = UiText.DynamicString("Unknown Error: ${this.message ?: "An unexpected error occurred. Please try again."}")
                    )
                }
            }
        }
    }
}

/**
 * A lightweight wrapper for UI text that can represent either a dynamic string
 * or a string resource ID. This ensures proper localization and configuration
 * change handling in Android.
 *
 * ## Why UiText?
 *
 * ViewModels that directly expose localized strings become stale when users change
 * device language because ViewModels survive configuration changes. Using [UiText],
 * you create "text blueprints" that get resolved in the View layer, ensuring
 * locale-aware rendering that automatically updates when the locale changes.
 *
 * ## Usage Examples
 *
 * ### In Repository/ViewModel:
 * ```kotlin
 * // Using dynamic string
 * val error1 = UiText.DynamicString("User not found")
 *
 * // Using string resource
 * val error2 = UiText.StringResource(R.string.error_network)
 *
 * // Using string resource with format args
 * val error3 = UiText.StringResource(R.string.error_user_name, "John")
 * ```
 *
 * ### In Composable:
 * ```kotlin
 * @Composable
 * fun ErrorMessage(message: UiText) {
 *     Text(text = message.asString())
 * }
 * ```
 *
 * ### In View (with Context):
 * ```kotlin
 * fun showError(context: Context, message: UiText) {
 *     Toast.makeText(context, message.asString(context), Toast.LENGTH_SHORT).show()
 * }
 * ```
 */
sealed class UiText {

    /**
     * Represents a dynamic string that doesn't require localization.
     * Use this for server-provided error messages or user-generated content.
     *
     * @param value The raw string value
     */
    data class DynamicString(val value: String) : UiText()

    /**
     * Represents a string resource ID for proper localization.
     * Use this for all app-defined messages that need translation.
     *
     * @param resId The string resource ID (e.g., R.string.error_message)
     * @param args Optional format arguments for string formatting (e.g., %s, %d)
     */
    data class StringResource(
        @StringRes val resId: Int,
        val args: Any
    ) : UiText()

    /**
     * Resolves the [UiText] to a plain [String] using the provided [Context].
     *
     * @param context Android context for accessing string resources
     * @return The resolved string value
     */
    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> context.getString(resId, args)
        }
    }
}

/**
 * Composable-friendly extension to resolve [UiText] without explicit Context.
 * Uses [androidx.compose.ui.platform.LocalContext] internally.
 *
 * @return The resolved string value
 */
//@androidx.compose.runtime.Composable
//fun UiText.asString(): String {
//    return when (this) {
//        is UiText.DynamicString -> value
//        is UiText.StringResource -> stringResource(resId, args)
//    }
//}

/**
 * Extension function to check if [CallerStatus] is in Loading state.
 */
fun <T> CallerStatus<T>.isLoading(): Boolean = this is CallerStatus.Loading

/**
 * Extension function to check if [CallerStatus] is in Success state.
 */
fun <T> CallerStatus<T>.isSuccess(): Boolean = this is CallerStatus.Success

/**
 * Extension function to check if [CallerStatus] is in Error state.
 */
fun <T> CallerStatus<T>.isError(): Boolean = this is CallerStatus.Error

/**
 * Extension function to get data if Success, null otherwise.
 */
fun <T> CallerStatus<T>.getOrNull(): T? {
    return when (this) {
        is CallerStatus.Success -> data
        else -> null
    }
}

/**
 * Extension function to execute a block if the status is Success.
 */
inline fun <T> CallerStatus<T>.onSuccess(block: (T) -> Unit): CallerStatus<T> {
    if (this is CallerStatus.Success) {
        block(data)
    }
    return this
}

/**
 * Extension function to execute a block if the status is Error.
 */
inline fun <T> CallerStatus<T>.onError(block: (CallerStatus.Error) -> Unit): CallerStatus<T> {
    if (this is CallerStatus.Error) {
        block(this)
    }
    return this
}

/**
 * Extension function to execute a block if the status is Loading.
 */
inline fun <T> CallerStatus<T>.onLoading(block: () -> Unit): CallerStatus<T> {
    if (this is CallerStatus.Loading) {
        block()
    }
    return this
}

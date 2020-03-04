package com.edricchan.studybuddy.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle

/**
 * Starts an activity [T] with the current activity's context
 * @param T The activity to launch
 */
inline fun <reified T : Activity> Activity.startActivity() {
    startActivity(Intent(this, T::class.java))
}

/**
 * Starts an activity [T] with the current activity's context
 * @param T The activity to launch
 * @param intentOptions Intent options
 */
inline fun <reified T : Activity> Activity.startActivity(intentOptions: Intent.() -> Unit) {
    val intent = Intent(this, T::class.java).apply(intentOptions)
    startActivity(intent)
}

/**
 * Starts an activity [T] with the specified [context]
 * @param T The activity to launch
 * @param context The context to be used to launch the activity
 */
inline fun <reified T : Activity> Activity.startActivity(context: Context) {
    startActivity(Intent(context, T::class.java))
}

/**
 * Starts an activity [T] with the specified [context]
 * @param T The activity to launch
 * @param context The context to be used to launch the activity
 * @param intentOptions Options for the intent
 */
inline fun <reified T : Activity> Activity.startActivity(
    context: Context,
    intentOptions: Intent.() -> Unit
) {
    val intent = Intent(context, T::class.java).apply(intentOptions)
    startActivity(intent)
}

/**
 * Starts an activity [T] with the current activity's context and [options] to be passed to the intent
 * @param T The activity to launch
 * @param options Options to be passed to the activity
 * @see Intent.getExtras This can be used in the activity launched to retrieve the [options] specified
 */
inline fun <reified T : Activity> Activity.startActivity(options: Bundle?) {
    startActivity(Intent(this, T::class.java), options)
}

/**
 * Starts an activity [T] with the current activity's context and [options] to be passed to the intent
 * @param T The activity to launch
 * @param options Options to be passed to the activity
 * @param intentOptions Options to be set on the [Intent]
 * @see Intent.getExtras This can be used in the activity launched to retrieve the [options] specified
 */
inline fun <reified T : Activity> Activity.startActivity(
    options: Bundle?,
    intentOptions: Intent.() -> Unit
) {
    val intent = Intent(this, T::class.java).apply(intentOptions)
    startActivity(intent, options)
}

/**
 * Starts an activity [T] with the specified [context] and [options] to be passed to the intent
 * @param T The activity to launch
 * @param context The context to be used to launch the activity
 * @param options Options to be passed to the activity
 * @see Intent.getExtras This can be used in the activity launched to retrieve the [options] specified
 */
inline fun <reified T : Activity> Activity.startActivity(context: Context, options: Bundle?) {
    startActivity(Intent(context, T::class.java), options)
}

/**
 * Starts an activity [T] with the specified [context] and [options] to be passed to the intent
 * @param T The activity to launch
 * @param context The context to be used to launch the activity
 * @param options Options to be passed to the activity
 * @param intentOptions Options to be set on the [Intent]
 * @see Intent.getExtras This can be used in the activity launched to retrieve the [options] specified
 */
inline fun <reified T : Activity> Activity.startActivity(
    context: Context,
    options: Bundle?,
    intentOptions: Intent.() -> Unit
) {
    val intent = Intent(context, T::class.java).apply(intentOptions)
    startActivity(intent, options)
}

/**
 * Starts an activity [T] with the current activity's context for [requestCode]
 * @param T The activity to launch
 * @param requestCode The request code returned by the activity once finished
 * @see Activity.onActivityResult
 */
inline fun <reified T : Activity> Activity.startActivityForResult(requestCode: Int) {
    startActivityForResult(Intent(this, T::class.java), requestCode)
}

/**
 * Starts an activity [T] with the current activity's context for [requestCode]
 * @param T The activity to launch
 * @param requestCode The request code returned by the activity once finished
 * @param intentOptions Options to be set on the [Intent]
 * @see Activity.onActivityResult
 */
inline fun <reified T : Activity> Activity.startActivityForResult(
    requestCode: Int,
    intentOptions: Intent.() -> Unit
) {
    val intent = Intent(this, T::class.java).apply(intentOptions)
    startActivityForResult(intent, requestCode)
}

/**
 * Starts an activity [T] with the specified [context] and return a [requestCode] when the activity is finished
 * @param T The activity to launch
 * @param context The context to be used to launch the activity
 * @param requestCode The request code returned by the activity once finished
 * @see Activity.onActivityResult
 */
inline fun <reified T : Activity> Activity.startActivityForResult(
    context: Context,
    requestCode: Int
) {
    startActivityForResult(Intent(context, T::class.java), requestCode)
}

/**
 * Starts an activity [T] with the specified [context] and return a [requestCode] when the activity is finished
 * @param T The activity to launch
 * @param context The context to be used to launch the activity
 * @param requestCode The request code returned by the activity once finished
 * @param intentOptions Options to be set on the [Intent]
 * @see Activity.onActivityResult
 */
inline fun <reified T : Activity> Activity.startActivityForResult(
    context: Context,
    requestCode: Int,
    intentOptions: Intent.() -> Unit
) {
    val intent = Intent(context, T::class.java).apply(intentOptions)
    startActivityForResult(intent, requestCode)
}

/**
 * Starts an activity [T] with the current activity's context, passes [options] to the intent and return the [requestCode]
 * when the activity is finished
 * @param T The activity to launch
 * @param requestCode The request code returned by the activity once finished
 * @param options Options to be passed to the activity
 * @see Activity.onActivityResult
 * @see Intent.getExtras
 */
inline fun <reified T : Activity> Activity.startActivityForResult(
    requestCode: Int,
    options: Bundle?
) {
    startActivityForResult(Intent(this, T::class.java), requestCode, options)
}

/**
 * Starts an activity [T] with the current activity's context, passes [options] to the intent and return the [requestCode]
 * when the activity is finished
 * @param T The activity to launch
 * @param requestCode The request code returned by the activity once finished
 * @param options Options to be passed to the activity
 * @param intentOptions Options to be set on the [Intent]
 * @see Activity.onActivityResult
 * @see Intent.getExtras
 */
inline fun <reified T : Activity> Activity.startActivityForResult(
    requestCode: Int,
    options: Bundle?,
    intentOptions: Intent.() -> Unit
) {
    val intent = Intent(this, T::class.java).apply(intentOptions)
    startActivityForResult(intent, requestCode, options)
}

/**
 * Starts an activity [T] with the specified [context], passes [options] to the intent and return the [requestCode]
 * when the activity is finished
 * @param T The activity to launch
 * @param context The context to be used to launch the activity
 * @param options Options to be passed to the activity
 * @see Intent.getExtras This can be used in the activity launched to retrieve the [options] specified
 */
inline fun <reified T : Activity> Activity.startActivityForResult(
    context: Context,
    requestCode: Int,
    options: Bundle?
) {
    startActivityForResult(Intent(context, T::class.java), requestCode, options)
}

/**
 * Starts an activity [T] with the specified [context], passes [options] to the intent and return the [requestCode]
 * when the activity is finished
 * @param T The activity to launch
 * @param context The context to be used to launch the activity
 * @param options Options to be passed to the activity
 * @param intentOptions Options to be set on the [Intent]
 * @see Intent.getExtras This can be used in the activity launched to retrieve the [options] specified
 */
inline fun <reified T : Activity> Activity.startActivityForResult(
    context: Context, requestCode: Int, options: Bundle?, intentOptions: Intent.() -> Unit
) {
    val intent = Intent(context, T::class.java).apply(intentOptions)
    startActivityForResult(intent, requestCode, options)
}
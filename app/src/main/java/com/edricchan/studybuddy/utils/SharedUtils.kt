package com.edricchan.studybuddy.utils

import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceManager
import com.crashlytics.android.Crashlytics
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.MainActivity
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.extensions.editTextStrValue
import com.edricchan.studybuddy.interfaces.NotificationRequest
import com.edricchan.studybuddy.interfaces.TaskItem
import com.edricchan.studybuddy.receiver.ActionButtonReceiver
import com.github.javiersantos.appupdater.AppUpdaterUtils
import com.github.javiersantos.appupdater.enums.AppUpdaterError
import com.github.javiersantos.appupdater.enums.UpdateFrom
import com.github.javiersantos.appupdater.objects.Update
import com.google.android.gms.tasks.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * Shared utility methods
 * @property context The context used for non-static methods
 */
class SharedUtils
(
		context: Context
) {
	// Since IDs 0 and 1 have been taken, use 2
	private val dynamicId = 2
	private val atomicInteger = AtomicInteger(dynamicId)

	/**
	 * Dynamically creates a new ID for use with Android's notification manager
	 *
	 * @return The ID in question
	 */
	fun getDynamicId(): Int {
		return atomicInteger.incrementAndGet()
	}

	/**
	 * A newer implementation of the former `sendNotificationToUser` method.
	 *
	 *
	 * This implementation reinforces that notifications are sent as requests to Cloud Firestore
	 * (which gets saved as a document under the `notificationRequests` collection) and are automatically sent to the associated topic or username.
	 *
	 *
	 * This implementation also uses only 1 parameter to save on the amount of characters required to call the former method.
	 *
	 * @param request The notification request to send to Cloud Firestore
	 * @return A reference of the task
	 */
	fun sendNotificationRequest(request: NotificationRequest): Task<DocumentReference> {
		val fs = FirebaseFirestore.getInstance()
		return fs.collection("notificationRequests").add(request)
	}

	companion object {
		/**
		 * Intent for notification settings action button for notifications
		 */
		@Deprecated(
				"Use Constants#actionNotificationsSettingsIntent}",
				ReplaceWith(
						"Constants.actionNotificationsSettingsIntent",
						"com.edricchan.studybuddy.utils.Constants"
				)
		)
		val ACTION_NOTIFICATIONS_SETTINGS_INTENT = Constants.actionNotificationsSettingsIntent
		/**
		 * Broadcaster for starting download
		 */
		@Deprecated(
				"Use Constants#actionNotificationsStartDownloadReceiver",
				ReplaceWith(
						"Constants.actionNotificationsStartDownloadReceiver",
						"com.edricchan.studybuddy.utils.Constants"
				)
		)
		val ACTION_NOTIFICATIONS_START_DOWNLOAD_RECEIVER = Constants.actionNotificationsStartDownloadReceiver
		/**
		 * Broadcaster for retrying check for updates
		 */
		@Deprecated(
				"Use Constants#actionNotificationsRetryCheckForUpdateReceiver",
				ReplaceWith(
						"Constants.actionNotificationsRetryCheckForUpdateReceiver",
						"com.edricchan.studybuddy.utils.Constants"
				)
		)
		val ACTION_NOTIFICATIONS_RETRY_CHECK_FOR_UPDATE_RECEIVER = Constants.actionNotificationsRetryCheckForUpdateReceiver
		/**
		 * Action icon for settings
		 */
		@Deprecated(
				"Use Constants#fcmSettingsIcon",
				ReplaceWith(
						"Constants.fcmSettingsIcon",
						"com.edricchan.studybuddy.utils.Constants"
				)
		)
		val ACTION_SETTINGS_ICON = Constants.fcmSettingsIcon
		/**
		 * Action icon for notification
		 */
		@Deprecated(
				"Use Constants#fcmNotificationIcon",
				ReplaceWith(
						"Constants.fcmNotificationIcon",
						"com.edricchan.studybuddy.utils.Constants"
				)
		)
		val ACTION_NOTIFICATION_ICON = Constants.fcmNotificationIcon
		/**
		 * Action icon for mark as done
		 */
		@Deprecated(
				"Use Constants#fcmMarkAsDoneIcon}",
				ReplaceWith(
						"Constants.fcmMarkAsDoneIcon",
						"com.edricchan.studybuddy.utils.Constants"
				)
		)
		val ACTION_MARK_AS_DONE_ICON = Constants.fcmMarkAsDoneIcon
		// IDs for notifications
		/**
		 * ID for checking for updates
		 */
		@Deprecated(
				"Use Constants#notificationCheckForUpdatesId",
				ReplaceWith(
						"Constants.notificationCheckForUpdatesId",
						"com.edricchan.studybuddy.utils.Constants"
				)
		)
		val NOTIFICATION_CHECK_FOR_UPDATES = Constants.notificationCheckForUpdatesId
		/**
		 * ID for media player notification
		 */
		@Deprecated("Use Constants#notificationMediaId",
				ReplaceWith(
						"Constants.notificationMediaId",
						"com.edricchan.studybuddy.utils.Constants"
				)
		)
		val NOTIFICATION_MEDIA = Constants.notificationMediaId
		/**
		 * [android.content.SharedPreferences] file used for all classes
		 */
		@Deprecated(
				"Use Constants#defaultSharedPrefsFile",
				ReplaceWith(
						"Constants.defaultSharedPrefsFile",
						"com.edricchan.studybuddy.utils.Constants"
				)
		)
		val DEFAULT_SHARED_PREFS_FILE = Constants.defaultSharedPrefsFile

		private val TAG = getTag(this::class.java)

		/**
		 * Clears the on click listener for the [com.google.android.material.bottomappbar.BottomAppBar]'s [FloatingActionButton]
		 *
		 * @param activity An instance of the activity (use [Fragment.getActivity] if you're in a [Fragment], or the current instance if you're in an activity which extends [AppCompatActivity])
		 */
		fun clearBottomAppBarFabOnClickListener(activity: AppCompatActivity) {
			if (activity is MainActivity) {
				val fab = activity.findViewById<FloatingActionButton>(R.id.fab)
				fab.setOnClickListener(null)
			}
		}

		/**
		 * Clears the on click listener for the [com.google.android.material.bottomappbar.BottomAppBar]'s [FloatingActionButton]
		 *
		 * @param activity An instance of the activity (use [Fragment.getActivity] if you're in a [Fragment], or the current instance if you're in an activity which extends [AppCompatActivity])
		 */
		fun clearBottomAppBarFabOnClickListener(activity: FragmentActivity) {
			if (activity is MainActivity) {
				val fab = activity.findViewById<FloatingActionButton>(R.id.fab)
				fab.setOnClickListener(null)
			}
		}

		/**
		 * Sets the on click listener for the [com.google.android.material.bottomappbar.BottomAppBar]'s [com.google.android.material.floatingactionbutton.FloatingActionButton]
		 *
		 * @param activity An instnace of the activity (use [Fragment.getActivity] if you're in a [Fragment], or the current instance if you're in an activity which extends [AppCompatActivity])
		 * @param listener The on click listener to set
		 */
		fun setBottomAppBarFabOnClickListener(activity: AppCompatActivity, listener: View.OnClickListener) {
			if (activity is MainActivity) {
				val fab = activity.findViewById<FloatingActionButton>(R.id.fab)
				fab.setOnClickListener(listener)
			}
		}

		/**
		 * Sets the on click listener for the [com.google.android.material.bottomappbar.BottomAppBar]'s [com.google.android.material.floatingactionbutton.FloatingActionButton]
		 *
		 * @param activity An instnace of the activity (use [Fragment.getActivity] if you're in a [Fragment], or the current instance if you're in an activity which extends [AppCompatActivity])
		 * @param listener The on click listener to set
		 */
		fun setBottomAppBarFabOnClickListener(activity: FragmentActivity, listener: View.OnClickListener) {
			if (activity is MainActivity) {
				val fab = activity.findViewById<FloatingActionButton>(R.id.fab)
				fab.setOnClickListener(listener)
			}
		}

		/**
		 * Retrieves the image drawable of the FAB in the main activity
		 *
		 * @param activity An instnace of the activity (use [Fragment.getActivity] if you're in a [Fragment], or the current instance if you're in an activity which extends [AppCompatActivity])
		 * @return The drawable of the FAB
		 */
		fun getBottomAppBarFabSrc(activity: AppCompatActivity): Drawable? {
			if (activity is MainActivity) {
				val fab = activity.findViewById<FloatingActionButton>(R.id.fab)
				return fab.drawable
			}
			return null
		}

		/**
		 * Sets the image drawable of the FAB in the main activity
		 *
		 * @param activity An instance of the activity (use [Fragment.getActivity] if you're in a [Fragment], or the current instance if you're in an activity which extends [AppCompatActivity])
		 * @param src      The drawable to set
		 */
		fun setBottomAppBarFabSrc(activity: AppCompatActivity, src: Drawable) {
			if (activity is MainActivity) {
				val fab = activity.findViewById<FloatingActionButton>(R.id.fab)
				fab.setImageDrawable(src)
			}
		}


		/**
		 * Sets the image drawable of the FAB in the main activity
		 *
		 * @param activity An instance of the activity (use [Fragment.getActivity] if you're in a [Fragment], or the current instance if you're in an activity which extends [AppCompatActivity])
		 * @param srcRes   The drawable resource to set
		 */
		fun setBottomAppBarFabSrc(activity: AppCompatActivity, @DrawableRes srcRes: Int) {
			if (activity is MainActivity) {
				val fab = activity.findViewById<FloatingActionButton>(R.id.fab)
				fab.setImageResource(srcRes)
			}
		}

		/**
		 * Utility method to set the app's theme.
		 * Note that the activity should be recreated after this function is called in order for the theme to be applied.
		 *
		 * @param context The context
		 */
		fun setAppTheme(context: Context) {
			val appTheme = PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.prefDarkTheme, Constants.prefDarkThemeAuto)!!
			when (appTheme) {
				// Note: The old values of the preference will still be supported
				// TODO: Completely remove support for old values
				"1" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
				"2" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO)
				"3" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
				// New values
				Constants.prefDarkThemeAlways -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
				Constants.prefDarkThemeAuto -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO)
				Constants.prefDarkThemeFollowSystem -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
				Constants.prefDarkThemeNever -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
				else -> Log.w(TAG, "Please supply a valid string integer (1, 2, or 3), or a valid option (\"always\", \"automatic\", \"follow_system\" or \"never\")!")
			}
		}

		/**
		 * Enables Crashlytics user tracking
		 *
		 * @param context The context
		 * @param user    The current logged-in user, retrieved from [FirebaseAuth.getCurrentUser]
		 */
		fun setCrashlyticsUserTracking(context: Context, user: FirebaseUser?) {
			val enableUserTrack = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Constants.prefEnableCrashlyticsUserTracking, false)
			if (enableUserTrack) {
				if (user != null) {
					Crashlytics.setUserEmail(user.email)
					Crashlytics.setUserIdentifier(user.uid)
					Crashlytics.setUserName(user.displayName)
				} else {
					Log.w(TAG, "No current logged-in user exists!")
				}
			}
		}

		/**
		 * Enables Crashlytics user tracking
		 *
		 * @param context The context
		 * @param auth    An instance of [FirebaseAuth], retrieved from [FirebaseAuth.getInstance]
		 */
		fun setCrashlyticsUserTracking(context: Context, auth: FirebaseAuth) {
			setCrashlyticsUserTracking(context, auth.currentUser)
		}

		/**
		 * Replaces a view with an initialised fragment.
		 * Note: This method checks if there's already a fragment in the view.
		 *
		 * @param activity       The activity.
		 * @param fragment       The fragment to replace the view with. (Needs to be initialised with a `new` constructor).
		 * @param viewId         The ID of the view.
		 * @param tag            The tag to assign to the fragment.
		 * @param addToBackStack Whether to add the fragment to the back stack.
		 * @return True if the fragment was replaced, false if there's already an existing fragment.
		 */
		fun replaceFragment(activity: AppCompatActivity, fragment: Fragment, @IdRes viewId: Int, tag: String, addToBackStack: Boolean): Boolean {
			// Check if fragment already has been replaced
			if (activity.supportFragmentManager.findFragmentByTag(tag) !== fragment && activity.supportFragmentManager.findFragmentById(viewId) !== fragment) {
				val transaction = activity.supportFragmentManager.beginTransaction()
				transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
				transaction.replace(
						viewId,
						fragment,
						tag
				)
				if (addToBackStack) {
					transaction.addToBackStack(null)
				}
				transaction.commit()
				// Indicate that the fragment replacement has been done.
				return true
			}
			// Return false if there's already an existing fragment.
			return false
		}

		/**
		 * Replaces a view with an initialised fragment.
		 * Note: This method checks if there's already a fragment in the view.
		 *
		 * @param activity       The activity.
		 * @param fragment       The fragment to replace the view with. (Needs to be initialised with a `new` constructor).
		 * @param viewId         The ID of the view.
		 * @param addToBackStack Whether to add the fragment to the back stack.
		 * @return True if the fragment was replaced, false if there's already an existing fragment.
		 */
		fun replaceFragment(activity: AppCompatActivity, fragment: Fragment, @IdRes viewId: Int, addToBackStack: Boolean): Boolean {
			if (activity.supportFragmentManager.findFragmentById(viewId) !== fragment) {
				val transaction = activity.supportFragmentManager.beginTransaction()
				transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
				transaction.replace(
						viewId,
						fragment
				)
				if (addToBackStack) {
					transaction.addToBackStack(null)
				}
				transaction.commit()
				// Indicate that the fragment replacement has been done.
				return true
			}
			// Return false if there's already an existing fragment.
			return false
		}

		/**
		 * Saves preferences to [SharedPreferences]
		 *
		 * @param context   The context
		 * @param prefsFile The preference file to save to
		 * @param mode      The mode of writing the file.
		 * @param key       The preference key to save to
		 * @param value     The value to save (as a boolean)
		 * @return An instance of the [SharedPreferences.Editor]
		 */
		fun putPrefs(context: Context, prefsFile: String, mode: Int, key: String, value: Boolean): SharedPreferences.Editor {
			val preferences = context.getSharedPreferences(prefsFile, mode)
			return preferences.edit()
					.putBoolean(key, value)
		}

		/**
		 * Saves preferences to [SharedPreferences]
		 *
		 * @param context   The context
		 * @param prefsFile The preference file to save to
		 * @param mode      The mode of writing the file.
		 * @param key       The preference key to save to
		 * @param value     The value to save (as a float)
		 * @return An instance of the [SharedPreferences.Editor]
		 */
		fun putPrefs(context: Context, prefsFile: String, mode: Int, key: String, value: Float): SharedPreferences.Editor {
			val preferences = context.getSharedPreferences(prefsFile, mode)
			return preferences.edit()
					.putFloat(key, value)
		}

		/**
		 * Saves preferences to [SharedPreferences]
		 *
		 * @param context   The context
		 * @param prefsFile The preference file to save to
		 * @param mode      The mode of writing the file.
		 * @param key       The preference key to save to
		 * @param value     The value to save (as an integer)
		 * @return An instance of the [SharedPreferences.Editor]
		 */
		fun putPrefs(context: Context, prefsFile: String, mode: Int, key: String, value: Int): SharedPreferences.Editor {
			val preferences = context.getSharedPreferences(prefsFile, mode)
			return preferences.edit()
					.putInt(key, value)
		}

		/**
		 * Saves preferences to [SharedPreferences]
		 *
		 * @param context   The context
		 * @param prefsFile The preference file to save to
		 * @param mode      The mode of writing the file.
		 * @param key       The preference key to save to
		 * @param value     The value to save (as a long)
		 * @return An instance of the [SharedPreferences.Editor]
		 */
		fun putPrefs(context: Context, prefsFile: String, mode: Int, key: String, value: Long): SharedPreferences.Editor {
			val preferences = context.getSharedPreferences(prefsFile, mode)
			return preferences.edit()
					.putLong(key, value)
		}

		/**
		 * Saves preferences to [SharedPreferences]
		 *
		 * @param context   The context
		 * @param prefsFile The preference file to save to
		 * @param mode      The mode of writing the file.
		 * @param key       The preference key to save to
		 * @param value     The value to save (as a string)
		 * @return An instance of the [SharedPreferences.Editor]
		 */
		fun putPrefs(context: Context, prefsFile: String, mode: Int, key: String, value: String): SharedPreferences.Editor {
			val preferences = context.getSharedPreferences(prefsFile, mode)
			return preferences.edit()
					.putString(key, value)
		}

		/**
		 * Saves preferences to [SharedPreferences]
		 *
		 * @param context   The context
		 * @param prefsFile The preference file to save to
		 * @param mode      The mode of writing the file.
		 * @param key       The preference key to save to
		 * @param value     The value to save (as a [<])
		 * @return An instance of the [SharedPreferences.Editor]
		 */
		fun putPrefs(context: Context, prefsFile: String, mode: Int, key: String, value: Set<String>): SharedPreferences.Editor {
			val preferences = context.getSharedPreferences(prefsFile, mode)
			return preferences.edit()
					.putStringSet(key, value)
		}

		/**
		 * @param datePicker The datepicker
		 * @return a java.util.Date
		 */
		fun getDateFromDatePicker(datePicker: DatePicker): Date {
			val day = datePicker.dayOfMonth
			val month = datePicker.month
			val year = datePicker.year

			val calendar = Calendar.getInstance()
			calendar.set(year, month, day)

			return calendar.time
		}

		/**
		 * Checks whether the network is available
		 *
		 * @param context The context
		 * @return A boolean
		 */
		fun isNetworkAvailable(context: Context): Boolean {
			val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
			val activeNetworkInfo = connectivityManager.activeNetworkInfo
			return activeNetworkInfo != null && activeNetworkInfo.isConnected
		}

		/**
		 * Checks whether the network is unavailable
		 *
		 * @param context The context
		 * @return A boolean
		 */
		fun isNetworkUnavailable(context: Context): Boolean {
			return !isNetworkAvailable(context)
		}

		/**
		 * Checks whether the network is cellular
		 *
		 * @param context The context
		 * @return A boolean
		 * See https://stackoverflow.com/a/32771164
		 * TODO: Use other way of checking for mobile data
		 * TODO: Deprecate this method
		 */
		fun isCellularNetworkAvailable(context: Context): Boolean {
			try {
				val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
				val activeNetwork = cm.activeNetworkInfo
				if (activeNetwork != null) {
					// connected to the mobile provider's data plan
					return activeNetwork.type == ConnectivityManager.TYPE_MOBILE
				}
			} catch (e: Exception) {
				Log.w(TAG, "An error occurred while attempting to retrieve the cellular network: ", e)
				return false
			}

			return false
		}

		/**
		 * Retrieves an [EditText] from Material's [TextInputLayout]
		 *
		 * @param inputLayout The [TextInputLayout]
		 * @return The [EditText] in the [TextInputLayout]
		 */
		@Deprecated(
				"Use TextInputLayout#getEditText()",
				ReplaceWith(
						"inputLayout.editText",
						"com.google.android.material.textfield.TextInputLayout"
				)
		)
		fun getEditText(inputLayout: TextInputLayout?): EditText? {
			return inputLayout?.editText
		}

		/**
		 * Retrieves the text from an [EditText] (or [com.google.android.material.textfield.TextInputEditText])
		 *
		 * @param editText The [EditText]
		 * @return The text of the [EditText]
		 */
		@Deprecated(
				"Use the EditText.editTextStrValue Kotlin extension function",
				ReplaceWith(
						"editText.strValue",
						"com.edricchan.studybuddy.extensions.strValue"
				)
		)
		fun getEditTextString(editText: EditText?): String {
			return editText?.text.toString()
		}

		/**
		 * Retrieves the text from a [TextInputLayout]
		 *
		 * @param inputLayout The [TextInputLayout]
		 * @return The text of the [com.google.android.material.textfield.TextInputEditText] in [TextInputLayout], or [null] if no such [com.google.android.material.textfield.TextInputEditText] exists
		 */
		@Deprecated(
				"Use the TextInputLayout.editTextStrValue Kotlin extension function",
				ReplaceWith(
						"inputLayout.editTextStrValue",
						"com.edricchan.studybuddy.editTextStrValue"
				)
		)
		fun getEditTextString(inputLayout: TextInputLayout?): String? {
			return if (inputLayout?.editText != null) {
				inputLayout.editTextStrValue
			} else {
				Log.w(TAG, "An EditText/TextInputEditText doesn't exist in the TextInputLayout.")
				null
			}
		}

		/**
		 * Retrieves the tag of a class. Useful for [android.util.Log]
		 *
		 * @param tagClass The class to retrieve the simple name of. Can be any Java class extending [Class].
		 * @return The tag
		 */
		fun getTag(tagClass: Class<*>): String {
			return tagClass.simpleName
		}

		/**
		 * Adds a new task to the Firebase Firestore database
		 *
		 * @param item The task item to add
		 * @param user The currently authenticated user
		 * @param fs   An instance of [FirebaseFirestore]
		 * @return The result.
		 */
		fun addTask(item: TaskItem, user: FirebaseUser, fs: FirebaseFirestore): Task<DocumentReference> {
			return fs.collection("users/" + user.uid + "/todos").add(item)
		}

		/**
		 * Retrieves todos from the Firebase Firestore database
		 *
		 * @param user The currently authenticated user
		 * @param fs   An instance of [FirebaseFirestore]
		 * @return A collection reference
		 */
		fun getTasks(user: FirebaseUser, fs: FirebaseFirestore): CollectionReference {
			return fs.collection("users/" + user.uid + "/todos")
		}

		/**
		 * Removes a task from the Firebase Firestore database
		 *
		 * @param docID The document's ID
		 * @param user  The currently authenticated user
		 * @param fs    An instance of [FirebaseFirestore]
		 * @return The result of the deletion
		 */
		fun removeTask(docID: String, user: FirebaseUser, fs: FirebaseFirestore): Task<Void> {
			return fs.document("users/" + user.uid + "/todos/" + docID).delete()
		}

		/**
		 * Shows a version dialog
		 *
		 * @param context The context to retrieve a [LayoutInflater] instance from & instantiate [com.google.android.material.dialog.MaterialAlertDialogBuilder]
		 */
		fun showVersionDialog(context: Context) {
			val versionDialogView = LayoutInflater.from(context).inflate(R.layout.version_dialog, null)
			val appIconImageView = versionDialogView.findViewById<ImageView>(R.id.appIconImageView)
			val appNameTextView = versionDialogView.findViewById<TextView>(R.id.appNameTextView)
			val appVersionTextView = versionDialogView.findViewById<TextView>(R.id.appVersionTextView)
			try {
				appIconImageView.setImageDrawable(context.packageManager.getApplicationIcon(BuildConfig.APPLICATION_ID))
			} catch (e: PackageManager.NameNotFoundException) {
				Log.e(TAG, "An error occurred while attempting to retrieve the app's icon:", e)
			}

			appNameTextView.text = context.applicationInfo.loadLabel(context.packageManager)
			appVersionTextView.text = BuildConfig.VERSION_NAME
			val versionDialogBuilder = MaterialAlertDialogBuilder(context)
			versionDialogBuilder
					.setView(versionDialogView)
					.show()
		}

		/**
		 * Used for setting up notification channels
		 *
		 * NOTE: This will only work if the device is Android Oreo or later
		 *
		 * @param context The context
		 */
		fun createNotificationChannels(context: Context) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
				// Create a new list
				val channels = ArrayList<NotificationChannel>()
				// Create another list for channel groups
				val channelGroups = ArrayList<NotificationChannelGroup>()
				// Task updates notifications
				val todoUpdatesChannel = NotificationChannel(context.getString(R.string.notification_channel_todo_updates_id), context.getString(R.string.notification_channel_todo_updates_title), NotificationManager.IMPORTANCE_HIGH)
				todoUpdatesChannel.description = context.getString(R.string.notification_channel_todo_updates_desc)
				todoUpdatesChannel.group = context.getString(R.string.notification_channel_group_todos_id)
				todoUpdatesChannel.enableLights(true)
				todoUpdatesChannel.lightColor = Color.YELLOW
				todoUpdatesChannel.enableVibration(true)
				todoUpdatesChannel.setShowBadge(true)
				channels.add(todoUpdatesChannel)

				// Weekly summary notifications
				val weeklySummaryChannel = NotificationChannel(context.getString(R.string.notification_channel_weekly_summary_id), context.getString(R.string.notification_channel_weekly_summary_title), NotificationManager.IMPORTANCE_LOW)
				weeklySummaryChannel.description = context.getString(R.string.notification_channel_weekly_summary_desc)
				weeklySummaryChannel.group = context.getString(R.string.notification_channel_group_todos_id)
				weeklySummaryChannel.setShowBadge(true)
				channels.add(weeklySummaryChannel)

				// Syncing notifications
				val syncChannel = NotificationChannel(context.getString(R.string.notification_channel_sync_id), context.getString(R.string.notification_channel_sync_title), NotificationManager.IMPORTANCE_LOW)
				syncChannel.description = context.getString(R.string.notification_channel_sync_desc)
				syncChannel.setShowBadge(false)
				channels.add(syncChannel)

				// Update error notifications
				val updateErrorChannel = NotificationChannel(context.getString(R.string.notification_channel_update_error_id), context.getString(R.string.notification_channel_update_error_title), NotificationManager.IMPORTANCE_HIGH)
				updateErrorChannel.description = context.getString(R.string.notification_channel_update_error_desc)
				updateErrorChannel.group = context.getString(R.string.notification_channel_group_updates_id)
				updateErrorChannel.setShowBadge(false)
				channels.add(updateErrorChannel)
				// Update status notifications
				val updateStatusChannel = NotificationChannel(context.getString(R.string.notification_channel_update_status_id), context.getString(R.string.notification_channel_update_status_title), NotificationManager.IMPORTANCE_LOW)
				updateStatusChannel.description = context.getString(R.string.notification_channel_update_status_desc)
				updateStatusChannel.group = context.getString(R.string.notification_channel_group_updates_id)
				updateStatusChannel.setShowBadge(false)
				channels.add(updateStatusChannel)
				// Update complete notifications
				val updateCompleteChannel = NotificationChannel(context.getString(R.string.notification_channel_update_complete_id), context.getString(R.string.notification_channel_update_complete_title), NotificationManager.IMPORTANCE_LOW)
				updateCompleteChannel.description = context.getString(R.string.notification_channel_update_complete_desc)
				updateCompleteChannel.group = context.getString(R.string.notification_channel_group_updates_id)
				updateCompleteChannel.setShowBadge(false)
				channels.add(updateCompleteChannel)
				// Update not available notifications
				val updateNotAvailableChannel = NotificationChannel(context.getString(R.string.notification_channel_update_not_available_id), context.getString(R.string.notification_channel_update_not_available_title), NotificationManager.IMPORTANCE_DEFAULT)
				updateNotAvailableChannel.description = context.getString(R.string.notification_channel_update_not_available_desc)
				updateNotAvailableChannel.group = context.getString(R.string.notification_channel_group_updates_id)
				updateNotAvailableChannel.setShowBadge(false)
				channels.add(updateNotAvailableChannel)
				// Update available notifications
				val updateAvailableChannel = NotificationChannel(context.getString(R.string.notification_channel_update_available_id), context.getString(R.string.notification_channel_update_available_title), NotificationManager.IMPORTANCE_HIGH)
				updateAvailableChannel.description = context.getString(R.string.notification_channel_update_available_desc)
				updateAvailableChannel.group = context.getString(R.string.notification_channel_group_updates_id)
				updateAvailableChannel.setShowBadge(false)
				channels.add(updateAvailableChannel)

				// Media playback notifications
				val playbackChannel = NotificationChannel(context.getString(R.string.notification_channel_playback_id), context.getString(R.string.notification_channel_playback_title), NotificationManager.IMPORTANCE_LOW)
				playbackChannel.description = context.getString(R.string.notification_channel_playback_desc)
				// We don't want to consider a playback notification to show a badge
				playbackChannel.setShowBadge(false)
				channels.add(playbackChannel)

				// Uncategorized notifications
				val uncategorisedChannel = NotificationChannel(context.getString(R.string.notification_channel_uncategorised_id), context.getString(R.string.notification_channel_uncategorised_title), NotificationManager.IMPORTANCE_DEFAULT)
				uncategorisedChannel.description = context.getString(R.string.notification_channel_uncategorised_desc)
				uncategorisedChannel.setShowBadge(true)
				channels.add(uncategorisedChannel)
				// Notification channel groups
				val todoChannelGroup = NotificationChannelGroup(context.getString(R.string.notification_channel_group_todos_id), context.getString(R.string.notification_channel_group_todos_title))
				channelGroups.add(todoChannelGroup)
				val updatesChannelGroup = NotificationChannelGroup(context.getString(R.string.notification_channel_group_updates_id), context.getString(R.string.notification_channel_group_updates_title))
				channelGroups.add(updatesChannelGroup)
				notificationManager.createNotificationChannelGroups(channelGroups)
				// Pass list to method
				notificationManager.createNotificationChannels(channels)

			}
		}

		/**
		 * An utility method to check for updates.
		 *
		 * @param context The context.
		 */
		fun checkForUpdates(context: Context) {
			val notificationManager = NotificationManagerCompat.from(context)
			val notifyBuilder = NotificationCompat.Builder(context, context.getString(R.string.notification_channel_update_status_id))
					.setSmallIcon(R.drawable.ic_notification_system_update_24dp)
					.setContentTitle(context.getString(R.string.notification_check_update))
					.setPriority(NotificationCompat.PRIORITY_DEFAULT)
					.setProgress(100, 0, true)
					.setColor(ContextCompat.getColor(context, R.color.colorPrimary))
					.setOngoing(true)
			notificationManager.notify(Constants.notificationCheckForUpdatesId, notifyBuilder.build())
			val appUpdaterUtils = AppUpdaterUtils(context)
					.setUpdateFrom(UpdateFrom.JSON)
					.setUpdateJSON(context.getString(R.string.testing_changelog_url))
					.withListener(object : AppUpdaterUtils.UpdateListener {
						override fun onSuccess(update: Update, updateAvailable: Boolean?) {
							if (update.latestVersionCode == BuildConfig.VERSION_CODE && (!updateAvailable!!)) {
								// User is running latest version
								notifyBuilder.setContentTitle(context.getString(R.string.notification_no_updates))
										.setProgress(0, 0, false)
										.setOngoing(false)
								notificationManager.notify(Constants.notificationCheckForUpdatesId, notifyBuilder.build())
							} else {
								// New update
								val intentAction = Intent(context, ActionButtonReceiver::class.java)

								intentAction.putExtra("action", Constants.actionNotificationsStartDownloadReceiver)
								intentAction.putExtra("downloadUrl", update.urlToDownload.toString())
								intentAction.putExtra("version", update.latestVersion)
								val pIntentDownload = PendingIntent.getBroadcast(context, 1, intentAction, PendingIntent.FLAG_UPDATE_CURRENT)
								notifyBuilder.setContentTitle(context.getString(R.string.notification_new_update_title))
										.setContentText(context.getString(R.string.notification_new_update_text, update.latestVersion))
										.setProgress(0, 0, false)
										.setOngoing(false)
										.setChannelId(context.getString(R.string.notification_channel_update_available_id))
										.addAction(NotificationCompat.Action(R.drawable.ic_download_24dp, "Download", pIntentDownload))
								notificationManager.notify(Constants.notificationCheckForUpdatesId, notifyBuilder.build())
							}
						}

						override fun onFailed(appUpdaterError: AppUpdaterError) {
							when (appUpdaterError) {
								AppUpdaterError.NETWORK_NOT_AVAILABLE -> notifyBuilder.setContentTitle(context.getString(R.string.notification_updates_error_no_internet_title))
										.setContentText(context.getString(R.string.notification_updates_error_no_internet_text))
										.setSmallIcon(R.drawable.ic_wifi_strength_4_alert)
								AppUpdaterError.JSON_ERROR -> notifyBuilder.setContentTitle(context.getString(R.string.notification_updates_error_not_found_title))
										.setContentText(context.getString(R.string.notification_updates_error_not_found_text))
										.setSmallIcon(R.drawable.ic_file_not_found_24dp)
							}
							val intentAction = Intent(context, ActionButtonReceiver::class.java)

							//This is optional if you have more than one buttons and want to differentiate between two
							intentAction.putExtra("action", Constants.actionNotificationsRetryCheckForUpdateReceiver)
							val pIntentRetry = PendingIntent.getBroadcast(context, 2, intentAction, PendingIntent.FLAG_UPDATE_CURRENT)
							notificationManager.notify(Constants.notificationCheckForUpdatesId,
									notifyBuilder
											.setProgress(0, 0, false)
											.setOngoing(false)
											.setChannelId(context.getString(R.string.notification_channel_update_error_id))
											.setColor(ContextCompat.getColor(context, R.color.colorWarn))
											.addAction(NotificationCompat.Action(R.drawable.ic_refresh_24dp, "Retry", pIntentRetry))
											.setStyle(NotificationCompat.BigTextStyle())
											.build())
						}
					})
			appUpdaterUtils.start()
		}

		/**
		 * Checks if the permission is granted.
		 * Returns false if the permission isn't granted, true otherwise.
		 *
		 * @param permission The permission to check
		 * @param context    The context
		 * @return A boolean
		 */
		fun checkPermission(permission: String, context: Context): Boolean {
			return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
		}

		/**
		 * Helper method to launch a URI
		 *
		 * @param context       The context
		 * @param uri           The URI to launch
		 * @param useCustomTabs Whether to use Chrome Custom Tabs
		 */
		fun launchUri(context: Context, uri: Uri, useCustomTabs: Boolean) {
			if (useCustomTabs) {
				val builder = CustomTabsIntent.Builder()
				builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))
						.addDefaultShareMenuItem()
						.setShowTitle(true)
				builder
						.build()
						.launchUrl(context, uri)
			} else {
				val browserIntent = Intent(Intent.ACTION_VIEW, uri)
				context.startActivity(browserIntent)
			}
		}

		/**
		 * A newer implementation of the former `sendNotificationToUser` method.
		 *
		 *
		 * This implementation reinforces that notifications are sent as requests to Cloud Firestore
		 * (which gets saved as a document under the `notificationRequests` collection) and are automatically sent to the associated topic or username.
		 *
		 *
		 * This implementation also uses only 2 parameters to save on the amount of characters required to call the former method.
		 *
		 * @param fs      An instance of [FirebaseFirestore] (TIP: Use [FirebaseFirestore.getInstance] to get the instance)
		 * @param request The notification request to send to Cloud Firestore
		 * @return A reference of the task
		 */
		fun sendNotificationRequest(fs: FirebaseFirestore, request: NotificationRequest): Task<DocumentReference> {
			return fs.collection("notificationRequests").add(request)
		}
	}
}
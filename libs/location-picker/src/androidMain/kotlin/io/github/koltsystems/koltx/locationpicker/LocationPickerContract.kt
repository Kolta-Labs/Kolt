package io.github.koltsystems.koltx.locationpicker

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Standard [ActivityResultContract] for [LocationPickerActivity] — use with
 * `registerForActivityResult(LocationPickerContract())` from either View-system or Compose
 * code (Compose callers will usually prefer `LocationPicker.rememberLauncher` instead).
 */
class LocationPickerContract : ActivityResultContract<LocationPickerConfig, LocationPickerResult?>() {
    override fun createIntent(context: Context, input: LocationPickerConfig): Intent =
        Intent(context, LocationPickerActivity::class.java)
            .putExtra(EXTRA_CONFIG, Json.encodeToString(input))

    override fun parseResult(resultCode: Int, intent: Intent?): LocationPickerResult? {
        if (resultCode != Activity.RESULT_OK) return null
        val json = intent?.getStringExtra(EXTRA_RESULT) ?: return null
        return Json.decodeFromString<LocationPickerResult>(json)
    }

    companion object {
        internal const val EXTRA_CONFIG = "io.github.koltsystems.koltx.locationpicker.CONFIG"
        internal const val EXTRA_RESULT = "io.github.koltsystems.koltx.locationpicker.RESULT"
    }
}

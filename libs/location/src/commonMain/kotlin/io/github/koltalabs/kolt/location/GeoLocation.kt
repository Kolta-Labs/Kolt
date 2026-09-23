package io.github.koltalabs.kolt.location

/**
 * A best-effort current-location reading. [label] and [timezoneId] are guesses
 * (reverse-geocoded city / device timezone) — callers should let the user confirm
 * or edit them before persisting.
 */
data class GeoLocation(
    val latitude: Double,
    val longitude: Double,
    val label: String? = null,
    val timezoneId: String? = null,
    /** Meters above sea level, when the platform location fix reports one — null if unavailable
     * (e.g. no vertical fix yet, or a non-GPS lookup like desktop's IP-based provider). */
    val altitudeMeters: Double? = null,
)

sealed interface LocationResult {
    data class Success(val location: GeoLocation) : LocationResult
    data object PermissionDenied : LocationResult
    data class Unavailable(val message: String) : LocationResult
}

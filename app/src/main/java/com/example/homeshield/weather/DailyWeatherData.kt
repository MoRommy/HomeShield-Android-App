package com.example.homeshield.weather

import com.google.gson.annotations.SerializedName

data class DailyWeatherData(
    @SerializedName("Headline") val headline: Headline,
    @SerializedName("DailyForecasts") val dailyForecasts: List<DailyForecast>
)

data class Headline(
    @SerializedName("EffectiveDate") val effectiveDate: String,
    @SerializedName("EffectiveEpochDate") val effectiveEpochDate: Long,
    @SerializedName("Severity") val severity: Int,
    @SerializedName("Text") val text: String,
    @SerializedName("Category") val category: String,
    @SerializedName("EndDate") val endDate: String,
    @SerializedName("EndEpochDate") val endEpochDate: Long,
    @SerializedName("MobileLink") val mobileLink: String,
    @SerializedName("Link") val link: String
)

data class DailyForecast(
    @SerializedName("Date") val date: String,
    @SerializedName("EpochDate") val epochDate: Long,
    @SerializedName("Temperature") val temperature: Temperature,
    @SerializedName("Day") val day: Day,
    @SerializedName("Night") val night: Night,
    @SerializedName("Sources") val sources: List<String>,
    @SerializedName("MobileLink") val mobileLink: String,
    @SerializedName("Link") val link: String
)

data class Temperature(
    @SerializedName("Minimum") val minimum: TemperatureDetail,
    @SerializedName("Maximum") val maximum: TemperatureDetail
)

data class TemperatureDetail(
    @SerializedName("Value") val value: Double,
    @SerializedName("Unit") val unit: String,
    @SerializedName("UnitType") val unitType: Int
)

data class Day(
    @SerializedName("Icon") val icon: Int,
    @SerializedName("IconPhrase") val iconPhrase: String,
    @SerializedName("HasPrecipitation") val hasPrecipitation: Boolean,
    @SerializedName("PrecipitationType") val precipitationType: String?,
    @SerializedName("PrecipitationIntensity") val precipitationIntensity: String?
)

data class Night(
    @SerializedName("Icon") val icon: Int,
    @SerializedName("IconPhrase") val iconPhrase: String,
    @SerializedName("HasPrecipitation") val hasPrecipitation: Boolean
)

package com.example.homeshield.weather

import com.google.gson.annotations.SerializedName
data class LocationResponse(
    @SerializedName("Version") val version: Int,
    @SerializedName("Key") val key: String,
    @SerializedName("Type") val type: String,
    @SerializedName("Rank") val rank: Int,
    @SerializedName("LocalizedName") val localizedName: String,
    @SerializedName("EnglishName") val englishName: String,
    @SerializedName("PrimaryPostalCode") val primaryPostalCode: String,
    @SerializedName("Region") val region: Region,
    @SerializedName("Country") val country: Country,
    @SerializedName("AdministrativeArea") val administrativeArea: AdministrativeArea,
    @SerializedName("TimeZone") val timeZone: TimeZone,
    @SerializedName("GeoPosition") val geoPosition: GeoPosition,
    @SerializedName("IsAlias") val isAlias: Boolean,
    @SerializedName("SupplementalAdminAreas") val supplementalAdminAreas: List<Any>,
    @SerializedName("DataSets") val dataSets: List<String>
)

data class Region(
    @SerializedName("Id") val id: String,
    @SerializedName("LocalizedName") val localizedName: String,
    @SerializedName("EnglishName") val englishName: String
)

data class Country(
    @SerializedName("Id") val id: String,
    @SerializedName("LocalizedName") val localizedName: String,
    @SerializedName("EnglishName") val englishName: String
)

data class AdministrativeArea(
    @SerializedName("Id")  val id: String,
    @SerializedName("LocalizedName") val localizedName: String,
    @SerializedName("EnglishName") val englishName: String,
    @SerializedName("Level") val level: Int,
    @SerializedName("LocalizedType") val localizedType: String,
    @SerializedName("EnglishType") val englishType: String,
    @SerializedName("CountryID") val countryID: String
)

data class TimeZone(
    @SerializedName("Code") val code: String,
    @SerializedName("Name") val name: String,
    @SerializedName("GmtOffset") val gmtOffset: Double,
    @SerializedName("IsDaylightSaving") val isDaylightSaving: Boolean,
    @SerializedName("NextOffsetChange") val nextOffsetChange: String
)

data class GeoPosition(
    @SerializedName("Latitude") val latitude: Double,
    @SerializedName("Longitude") val longitude: Double,
    @SerializedName("Elevation") val elevation: Elevation
)

data class Elevation(
    @SerializedName("Metric") val metric: Metric,
    @SerializedName("Imperial") val imperial: Imperial
)

data class Metric(
    @SerializedName("Value") val value: Double,
    @SerializedName("Unit") val unit: String,
    @SerializedName("UnitType") val unitType: Int
)

data class Imperial(
    @SerializedName("Value")val value: Double,
    @SerializedName("Unit")val unit: String,
    @SerializedName("UnitType")val unitType: Int
)

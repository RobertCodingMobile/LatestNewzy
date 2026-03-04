package com.robertcoding.domain.model


data class LatestNewsModel(
    val section: String,
    val subsection: String? = null,
    val title: String,
    val abstract: String? = null,
    val url: String,
    val uri: String? = null,
    val byline: String? = null,
    val itemType: String? = null,
    val updatedDate: String? = null,
    val createdDate: String? = null,
    val publishedDate: String? = null,
    val materialTypeFacet: String? = null,
    val kicker: String? = null,
    val desFacet: List<String> = emptyList(),

    val orgFacet: List<String> = emptyList(),

    val perFacet: List<String> = emptyList(),

    val geoFacet: List<String> = emptyList(),

    val multimedia: List<MultimediaModel>? = emptyList()
)

data class MultimediaModel(
    val url: String?,

    val format: String? = null,

    val height: Int? = null,

    val width: Int? = null,

    val type: String? = null,

    val subtype: String? = null,

    val caption: String? = null,

    val copyright: String? = null
)

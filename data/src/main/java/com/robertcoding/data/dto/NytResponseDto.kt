package com.robertcoding.data.dto

import kotlinx.collections.immutable.ImmutableList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NytResponseDto(val response: NytDataDto)

@Serializable
data class NytDataDto(val docs: List<ArticleDto>, val meta: NytMetaDto? = null)

@Serializable
data class NytMetaDto(val hits: Int, val offset: Int)

@Serializable
data class ArticleDto(
    @SerialName("_id") val id: String,
    val abstract: String,
    @SerialName("web_url")
    val webUrl: String
)

/**
 * Top-level response object from the NYT Top Stories API
 */
@Serializable
data class LatestNewsResponse(
    @SerialName("status")
    val status: String,

    @SerialName("copyright")
    val copyright: String,

    @SerialName("section")
    val section: String,

    @SerialName("last_updated")
    val lastUpdated: String,

    @SerialName("num_results")
    val numResults: Int,

    @SerialName("results")
    val results: List<LatestNewsArticleDto>
)

/**
 * Individual article object
 */
@Serializable
data class LatestNewsArticleDto(

    @SerialName("status")
    val status: String? = null,
    @SerialName("copyright")
    val copyright: String? = null,
    @SerialName("last_updated")
    val lastUpdated: String? = null,
    @SerialName("num_results")
    val numResults: Int? = null,

    @SerialName("section")
    val section: String,

    @SerialName("subsection")
    val subsection: String? = null,

    @SerialName("title")
    val title: String,

    @SerialName("abstract")
    val abstract: String? = null,

    @SerialName("url")
    val url: String,

    @SerialName("uri")
    val uri: String? = null,

    @SerialName("byline")
    val byline: String? = null,

    @SerialName("item_type")
    val itemType: String? = null,

    @SerialName("updated_date")
    val updatedDate: String? = null,

    @SerialName("created_date")
    val createdDate: String? = null,

    @SerialName("published_date")
    val publishedDate: String? = null,

    @SerialName("material_type_facet")
    val materialTypeFacet: String? = null,

    @SerialName("kicker")
    val kicker: String? = null,

    @SerialName("des_facet")
    val desFacet: List<String> = emptyList(),

    @SerialName("org_facet")
    val orgFacet: List<String> = emptyList(),

    @SerialName("per_facet")
    val perFacet: List<String> = emptyList(),

    @SerialName("geo_facet")
    val geoFacet: List<String> = emptyList(),

    @SerialName("multimedia")

    val multimedia: List<Multimedia>? = emptyList()
)

/**
 * Multimedia object for article images
 */
@Serializable
data class Multimedia(
    @SerialName("url")
    val url: String? = null,

    @SerialName("format")
    val format: String? = null,

    @SerialName("height")
    val height: Int? = null,

    @SerialName("width")
    val width: Int? = null,

    @SerialName("type")
    val type: String? = null,

    @SerialName("subtype")
    val subtype: String? = null,

    @SerialName("caption")
    val caption: String? = null,

    @SerialName("copyright")
    val copyright: String? = null
)


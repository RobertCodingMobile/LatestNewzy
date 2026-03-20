package com.robertcoding.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "newzy")
data class NewzyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
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
)

@Entity(tableName = "multimedia")
data class MultiMediaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val url: String?,

    val format: String? = null,

    val height: Int? = null,

    val width: Int? = null,

    val type: String? = null,

    val subtype: String? = null,

    val caption: String? = null,

    val copyright: String? = null

)
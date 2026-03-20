package com.robertcoding.data.room

import com.robertcoding.data.dto.LatestNewsArticleDto
import com.robertcoding.data.dto.Multimedia
import com.robertcoding.domain.model.LatestNewsModel
import com.robertcoding.domain.model.MultimediaModel

fun LatestNewsArticleDto.toEntity(): NewzyEntity =
    NewzyEntity(
        section = section,
        subsection = subsection,
        title = title,
        abstract = abstract,
        url = url,
        uri = uri,
        byline = byline,
        itemType = itemType,
        updatedDate = updatedDate,
        createdDate = createdDate,
        publishedDate = publishedDate,
        materialTypeFacet = materialTypeFacet,
        kicker = kicker
    )

fun NewzyEntity.toDomain(): LatestNewsModel =
    LatestNewsModel(
        section = section,
        subsection = subsection,
        title = title,
        abstract = abstract,
        url = url,
        uri = uri,
        byline = byline,
        itemType = itemType,
        updatedDate = updatedDate,
        createdDate = createdDate,
        publishedDate = publishedDate,
        materialTypeFacet = materialTypeFacet,
        kicker = kicker
    )

fun Multimedia.toEntity(): MultiMediaEntity =
    MultiMediaEntity(
        url = url,
        format = format,
        height = height,
        width = width,
        type = type,
        subtype = subtype,
        caption = caption,
        copyright = copyright
    )

fun MultiMediaEntity.toDomainModel(): MultimediaModel =
    MultimediaModel(
        url = url,
        format = format,
        height = height,
        width = width,
        type = type,
        subtype = subtype,
        caption = caption,
        copyright = copyright
    )
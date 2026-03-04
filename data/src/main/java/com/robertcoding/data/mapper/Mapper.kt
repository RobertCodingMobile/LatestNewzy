package com.robertcoding.data.mapper

import com.robertcoding.data.dto.ArticleDto
import com.robertcoding.data.dto.LatestNewsArticleDto
import com.robertcoding.data.dto.Multimedia
import com.robertcoding.domain.model.Article
import com.robertcoding.domain.model.LatestNewsModel
import com.robertcoding.domain.model.MultimediaModel

fun ArticleDto.toDomain(): Article {
    return Article(
        id = id,
        title = abstract,
        url = webUrl
    )
}

fun LatestNewsArticleDto.toDomain(): LatestNewsModel =
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
        multimedia = this.multimedia?.map { it.toDomain() }
    )

fun Multimedia.toDomain(): MultimediaModel =
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
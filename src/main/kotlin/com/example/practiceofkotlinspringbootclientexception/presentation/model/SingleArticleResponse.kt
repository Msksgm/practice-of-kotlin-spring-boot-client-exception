package com.example.practiceofkotlinspringbootclientexception.presentation.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.Valid

/**
 * 単一記事のレスポンスモデル
 *
 * @property article
 */
data class SingleArticleResponse(
    @field:Valid
    @field:JsonProperty("article", required = true) val article: Article
)

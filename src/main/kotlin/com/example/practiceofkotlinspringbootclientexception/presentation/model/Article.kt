package com.example.practiceofkotlinspringbootclientexception.presentation.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Size

/**
 * 記事のモデル
 *
 * 全ての null のバリデーションパターンにおいてレスポンスは共通
 *
 * @property slug スラッグ
 * @property title タイトル
 * @property description 説明
 * @property body 本文
 */
data class Article(
    @get:Size(max = 32)
    @field:JsonProperty("slug", required = true) val slug: String,

    @get:Size(max = 64)
    @field:JsonProperty("title", required = true) val title: String,

    @get:Size(max = 1024)
    @field:JsonProperty("description", required = true) val description: String,

    @get:Size(max = 4096)
    @field:JsonProperty("body", required = true) val body: String
)

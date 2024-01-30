package com.example.practiceofkotlinspringbootclientexception.presentation

import com.example.practiceofkotlinspringbootclientexception.presentation.model.Article
import com.example.practiceofkotlinspringbootclientexception.presentation.model.NewArticleRequest
import com.example.practiceofkotlinspringbootclientexception.presentation.model.SingleArticleResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

/**
 * 例外ハンドリングのためのサンプルコントローラ
 *
 */
@RestController
@Validated
class ArticleController {
    /**
     * 記事作成エンドポイント
     *
     * @param newArticleRequest
     * @return
     */
    @PostMapping(
        value = ["/articles"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    @Suppress("UnsafeCallOnNullableType") // サンプルプロジェクトのため、安全じゃない null 非許容型の呼び出すを許可する
    fun createArticle(
        @Valid @RequestBody newArticleRequest: NewArticleRequest
    ): ResponseEntity<SingleArticleResponse> {
        return ResponseEntity(
            SingleArticleResponse(
                article = Article(
                    slug = "new-slug",
                    title = newArticleRequest.article!!.title!!,
                    body = newArticleRequest.article.body!!,
                    description = newArticleRequest.article.description!!,
                )
            ),
            HttpStatus.OK
        )
    }

    /**
     * 記事取得エンドポイント
     *
     * @param slug
     * @return
     */
    @GetMapping(
        value = ["/articles/{slug}"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun getArticle(
        @PathVariable("slug") slug: String,
    ): ResponseEntity<SingleArticleResponse> {
        return ResponseEntity(
            SingleArticleResponse(
                article = Article(
                    slug = slug,
                    title = "already-exists-title",
                    body = "already-exists-body",
                    description = "already-exists-description",
                )
            ),
            HttpStatus.OK
        )
    }
}

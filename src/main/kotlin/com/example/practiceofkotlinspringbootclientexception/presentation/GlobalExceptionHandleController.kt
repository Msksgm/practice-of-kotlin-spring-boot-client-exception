package com.example.practiceofkotlinspringbootclientexception.presentation

import com.example.practiceofkotlinspringbootclientexception.presentation.model.GenericErrorModel
import com.example.practiceofkotlinspringbootclientexception.presentation.model.GenericErrorModelErrors
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.resource.NoResourceFoundException

/**
 * グローバルに例外ハンドリグし、エラーレスポンスを実現するコントローラ
 *
 * 予期しない例外が発生した際に、以下を実施する。
 * - 秘密情報をレスポンスに含めないためのエラーハンドリング
 * - 原因調査のためのログ出力
 *
 */
@RestControllerAdvice
class GlobalExceptionHandleController {
    /**
     * 存在しないエンドポイントにリクエストされた時のエラーレスポンスを作成するメソッド
     *
     * @param e
     * @return 404 エラーのレスポンス
     */
    @ExceptionHandler(NoResourceFoundException::class)
    @Suppress("UnusedParameter") // NoResourceFoundException は理由が明確なので、e を使わずに linter を抑制する
    fun noResourceFoundExceptionHandler(e: NoResourceFoundException): ResponseEntity<GenericErrorModel> {
        return ResponseEntity<GenericErrorModel>(
            GenericErrorModel(
                errors = GenericErrorModelErrors(
                    body = listOf("該当するエンドポイントがありませんでした")
                ),
            ),
            HttpStatus.NOT_FOUND
        )
    }
}

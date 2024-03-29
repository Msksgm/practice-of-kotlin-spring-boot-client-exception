package com.example.practiceofkotlinspringbootclientexception.api.integration

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GlobalExceptionHandeControllerTest(
    @Autowired val mockMvc: MockMvc,
) {
    @Test
    fun `NoResourceFoundException 該当するエンドポイントがなかった`() {
        /**
         * given:
         */
        val url = "/not_found"

        /**
         * when:
         */
        val response = mockMvc.get(url) {
            contentType = MediaType.APPLICATION_JSON
        }.andReturn().response
        val actualStatus = response.status
        val actualResponseBody = response.contentAsString

        /**
         * then:
         */
        val expectedStatus = HttpStatus.NOT_FOUND.value()
        val expectedResponseBody = """
            {
                "errors": {
                    "body": [
                        "該当するエンドポイントがありませんでした"
                    ]
                }
            }
        """.trimIndent()
        assertThat(actualStatus).isEqualTo(expectedStatus)
        JSONAssert.assertEquals(expectedResponseBody, actualResponseBody, JSONCompareMode.STRICT)
    }

    @Test
    fun `HttpRequestMethodNotSupportedException 該当するエンドポイントに許可されていないリクエストを送った`() {
        /**
         * given:
         */
        val url = "/articles"

        /**
         * when:
         */
        val response = mockMvc.put(url) {
            contentType = MediaType.APPLICATION_JSON
        }.andReturn().response
        val actualStatus = response.status
        val actualResponseBody = response.contentAsString

        /**
         * then:
         */
        val expectedStatus = HttpStatus.METHOD_NOT_ALLOWED.value()
        val expectedResponseBody = """
            {
                "errors": {
                    "body": [
                        "該当エンドポイントでPUTメソッドの処理は許可されていません"
                    ]
                }
            }
        """.trimIndent()
        assertThat(actualStatus).isEqualTo(expectedStatus)
        JSONAssert.assertEquals(expectedResponseBody, actualResponseBody, JSONCompareMode.STRICT)
    }

    @Test
    fun `HttpMediaTypeNotSupportedException サポートされていない content-type でリクエストを送った`() {
        /**
         * given:
         */
        val url = "/articles"

        /**
         * when:
         * - サポートされていない content-type（text/plain） でリクエストを送る
         */
        val response = mockMvc.get("$url/already-exists-slug") {
            contentType = MediaType.TEXT_PLAIN
        }.andReturn().response
        val actualStatus = response.status
        val actualResponseBody = response.contentAsString

        /**
         * then:
         */
        val expectedStatus = HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()
        val expectedResponseBody = """
            {
                "errors": {
                    "body": [
                        "該当エンドポイントでtext/plainのリクエストはサポートされていません"
                    ]
                }
            }
        """.trimIndent()
        assertThat(actualStatus).isEqualTo(expectedStatus)
        JSONAssert.assertEquals(expectedResponseBody, actualResponseBody, JSONCompareMode.STRICT)
    }

    @Test
    fun `HttpMessageNotReadableException エンドポイントが想定していない形式または型のリクエストが送られた`() {
        /**
         * given:
         * - JSON のフォーマットが不正（閉じ括弧が欠けている）
         */
        val url = "/articles"
        val requestBody = """
            {
                "article": {
                    "title": "new-title",
                    "body": "new-body",
                    "description": "new-description"
            }
        """.trimIndent()

        /**
         * when:
         */
        val response = mockMvc.post(url) {
            contentType = MediaType.APPLICATION_JSON
            content = requestBody
        }.andReturn().response
        val actualStatus = response.status
        val actualResponseBody = response.contentAsString

        /**
         * then:
         */
        val expectedStatus = HttpStatus.BAD_REQUEST.value()
        val expectedResponseBody = """
            {
                "errors": {
                    "body": [
                        "エンドポイントが想定していない形式または型のリクエストが送られました"
                    ]
                }
            }
        """.trimIndent()
        assertThat(actualStatus).isEqualTo(expectedStatus)
        JSONAssert.assertEquals(expectedResponseBody, actualResponseBody, JSONCompareMode.STRICT)
    }

    @Test
    fun `MethodArgumentNotValidException リクエストのバリデーションエラー`() {
        /**
         * given:
         * - バリデーションエラーのリクエストボディ
         */
        val url = "/articles"
        val invalidTitle = "a".repeat(33)
        val invalidDescription = "a".repeat(1025)
        val invalidBody = "a".repeat(2049)
        val requestBody = """
            {
                "article": {
                    "title": "$invalidTitle",
                    "body": "$invalidBody",
                    "description": "$invalidDescription"
                }
            }
        """.trimIndent()

        /**
         * when:
         */
        val response = mockMvc.post(url) {
            contentType = MediaType.APPLICATION_JSON
            content = requestBody
        }.andReturn().response
        val actualStatus = response.status
        val actualResponseBody = response.contentAsString

        println(actualResponseBody)

        /**
         * then:
         */
        val expectedStatus = HttpStatus.BAD_REQUEST.value()
        val expectedResponseBody = """
            {
                "errors": {
                    "body": [
                        "article.descriptionは0文字以上1024文字以下です",
                        "article.bodyは0文字以上2048文字以下です",
                        "article.titleは0文字以上32文字以下です"
                    ]
                }
            }
        """.trimIndent()

        assertThat(actualStatus).isEqualTo(expectedStatus)
        JSONAssert.assertEquals(expectedResponseBody, actualResponseBody, JSONCompareMode.NON_EXTENSIBLE)
    }
}

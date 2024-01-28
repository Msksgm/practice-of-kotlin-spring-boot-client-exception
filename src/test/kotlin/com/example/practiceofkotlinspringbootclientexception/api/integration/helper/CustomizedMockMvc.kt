package com.example.practiceofkotlinspringbootclientexception.api.integration.helper

import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcBuilderCustomizer
import org.springframework.stereotype.Component
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder

/**
 * MockMvc のカスタムクラス
 *
 * 文字化けを防ぐために Response の Content-Type に "charset=UTF-8" を付与する
 * @Component アノテーションによって自動的に設定が上書きされる
 *
 */
@Component
class CustomizedMockMvc : MockMvcBuilderCustomizer {
    override fun customize(builder: ConfigurableMockMvcBuilder<*>?) {
        builder?.alwaysDo { result -> result.response.characterEncoding = "UTF-8" }
    }
}

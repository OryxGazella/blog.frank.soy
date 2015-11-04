package soy.frank.blog

import com.fasterxml.jackson.databind.SerializationFeature
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.orm.jpa.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

@SpringBootApplication
@EntityScan(basePackageClasses = arrayOf(Application::class, Jsr310JpaConverters::class))
open class Application {
    companion object {
        @JvmStatic fun main(args: Array<String>) {
            SpringApplication.run(Application::class.java, *args)
        }
    }

    @Bean
    open fun objectMapperBuilder() =
            Jackson2ObjectMapperBuilder()
                    .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
}

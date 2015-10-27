package soy.frank.blog

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
public open class Application

public fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}
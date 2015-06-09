package soy.frank.blog.controllers

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

RestController
public class ApplicationController {
    RequestMapping("/")
    public fun greeting(): String {
        return "Hello from Kotlin"
    }
}
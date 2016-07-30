import static ratpack.groovy.Groovy.ratpack

ratpack {
    handlers {
        get {
            render "Frank's Blog"
        }
    }
}

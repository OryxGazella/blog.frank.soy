class StaticPagesController < ApplicationController

  FakePost = Struct.new(:title)

  def i_code_java_2016
    @post = FakePost.new("I Code Java 2016")
  end
end

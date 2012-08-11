class BlogController < ApplicationController
  def index
    @entries = BlogEntry.all
  end
end

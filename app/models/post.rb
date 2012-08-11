class Post < ActiveRecord::Base
  attr_accessible :body, :rendered_body, :title
  before_save :render_body

  private
  def render_body
    require 'redcarpet'
    renderer = Redcarpet::Render::HTML.new
    extensions = {fenced_code_blocks: true}
    redcarpet = Redcarpet::Markdown.new(renderer, extensions)
    self.rendered_body = redcarpet.render self.body
  end
end

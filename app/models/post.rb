class Post < ActiveRecord::Base
  attr_accessible :body, :rendered_body, :title, :abstract
  before_save :render_body

  private
  def render_body
    require 'redcarpet'
    renderer = PygmentizeHTML 
    extensions = {fenced_code_blocks: true}
    redcarpet = Redcarpet::Markdown.new(renderer, extensions)
    self.rendered_body = redcarpet.render self.body
    self.abstract ||= self.body[0,900]
    self.rendered_abstract = redcarpet.render self.abstract
  end
end

class PygmentizeHTML < Redcarpet::Render::HTML
  def block_code(code, language)
    require 'pygmentize'
    Pygmentize.process(code, language)
  end
end

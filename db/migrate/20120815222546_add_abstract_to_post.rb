class AddAbstractToPost < ActiveRecord::Migration
  def change
    add_column :posts, :abstract, :text
    add_column :posts, :rendered_abstract, :text
    Post.all.each do
      |post|
      post.abstract = post.body[0,100]
      post.save
    end
  end
end

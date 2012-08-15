class UpdatePreview < ActiveRecord::Migration
  def up
    Post.all.each do
      |post|

      post.abstract = post.body[0,900]
      post.save
    end
  end

  def down
  end
end

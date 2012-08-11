class AddRenderedBodyToBlogEntry < ActiveRecord::Migration
  def change
    add_column :blog_entries, :rendered_body, :text
  end
end

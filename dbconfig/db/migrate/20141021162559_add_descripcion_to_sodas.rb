class AddDescripcionToSodas < ActiveRecord::Migration
  def change
    add_column :sodas, :descripcion, :text
  end
end

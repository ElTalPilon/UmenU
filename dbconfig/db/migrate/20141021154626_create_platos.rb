class CreatePlatos < ActiveRecord::Migration
  def change
    create_table :platos do |t|
      t.string :nombre
      t.integer :precio

      t.timestamps
    end
  end
end

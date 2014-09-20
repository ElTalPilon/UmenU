class CreatePlatos < ActiveRecord::Migration
  def change
    create_table :platos do |t|

      t.belongs_to :soda
      t.string :nombre
      t.integer :precio
      t.string :categoria
      t.string :tipo
      t.float :puntuacion

      t.timestamps
    end
  end
end

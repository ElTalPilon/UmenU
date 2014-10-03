class CreateComentarios < ActiveRecord::Migration
  def change
    create_table :comentarios do |t|
      t.belongs_to :usuario
      t.belongs_to :plato
      t.string :comentario
      t.integer :puntos

    end
  end
end

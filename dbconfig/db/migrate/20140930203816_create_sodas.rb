class CreateSodas < ActiveRecord::Migration
  def change
    create_table :sodas do |t|
      t.string :nombre
      t.time :abre
      t.time :cierra
      t.time :iDesayuno
      t.time :fDesayuno
      t.time :iAlmuerzo
      t.time :fAlmuerzo
      t.time :iCena
      t.time :fCena

    end
  end
end

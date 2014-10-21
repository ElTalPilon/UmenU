class RemoveOldAttributesFromSodas < ActiveRecord::Migration
  def change
    remove_column :sodas, :abre, :time
    remove_column :sodas, :cierra, :time
    remove_column :sodas, :iDesayuno, :time
    remove_column :sodas, :fDesayuno, :time
    remove_column :sodas, :iAlmuerzo, :time
    remove_column :sodas, :fAlmuerzo, :time
    remove_column :sodas, :iCena, :time
    remove_column :sodas, :fCena, :time
  end
end

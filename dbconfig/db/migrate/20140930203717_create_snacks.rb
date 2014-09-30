class CreateSnacks < ActiveRecord::Migration
  def change
    create_table :snacks do |t|
      t.belongs_to :soda
      t.string :nombre
      t.integer :precio

      t.timestamps
    end
  end
end

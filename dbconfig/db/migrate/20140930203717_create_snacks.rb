class CreateSnacks < ActiveRecord::Migration
  def change
    create_table :snacks do |t|
      t.belongs_to :soda
      t.string :nombre
      t.integer :precio

    end
  end
end

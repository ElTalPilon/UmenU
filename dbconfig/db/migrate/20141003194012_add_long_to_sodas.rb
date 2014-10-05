class AddLongToSodas < ActiveRecord::Migration
  def change
    add_column :sodas, :long, :double
  end
end

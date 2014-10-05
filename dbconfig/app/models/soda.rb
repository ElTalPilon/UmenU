class Soda < ActiveRecord::Base

   has_many :plato
   has_many :snack

end

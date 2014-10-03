class Soda < ActiveRecord::Base

   has_many :platos
   has_many :snacks

end

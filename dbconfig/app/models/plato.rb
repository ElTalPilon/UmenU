class Plato < ActiveRecord::Base

   belongs_to :soda
   has_many :comentario

end

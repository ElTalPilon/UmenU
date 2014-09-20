class Plato < ActiveRecord::Base

	has_many :comentarios, dependent: :destroy
	belongs_to :soda

end

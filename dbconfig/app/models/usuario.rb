class Usuario < ActiveRecord::Base

    has_many :comentarios, dependent: :destroy
	validates_uniqueness_of :nombre

end

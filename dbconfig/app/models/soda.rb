class Soda < ActiveRecord::Base

	has_many :platos, dependent: :destroy
	has_many :snacks, dependent: :destroy

end

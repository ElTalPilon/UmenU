json.array!(@snacks) do |snack|
  json.extract! snack, :id, :nombre, :precio, :soda_id
  json.url snack_url(snack, format: :json)
end

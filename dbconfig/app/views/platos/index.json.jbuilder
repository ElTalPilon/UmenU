json.array!(@platos) do |plato|
  json.extract! plato, :id, :nombre, :precio, :categoria, :tipo, :calificaciones, :total, :soda_id
  json.url plato_url(plato, format: :json)
end

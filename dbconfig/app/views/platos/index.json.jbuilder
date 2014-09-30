json.array!(@platos) do |plato|
  json.extract! plato, :id, :nombre, :precio, :categoria, :tipo, :calificaciones, :total
  json.url plato_url(plato, format: :json)
end

json.array!(@platos) do |plato|
  json.extract! plato, :id, :nombre, :precio, :categoria, :tipo, :puntuacion
  json.url plato_url(plato, format: :json)
end

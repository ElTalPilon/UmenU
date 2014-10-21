json.array!(@sodas) do |soda|
  json.extract! soda, :id, :nombre, :long, :lat, :descripcion
  json.url soda_url(soda, format: :json)
end

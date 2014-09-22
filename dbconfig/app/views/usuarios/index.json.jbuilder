json.array!(@usuarios) do |usuario|
  json.extract! usuario, :id, :nombre, :direccion
  json.url usuario_url(usuario, format: :json)
end

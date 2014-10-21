json.array!(@comentarios) do |comentario|
  json.extract! comentario, :id, :comentario, :puntos, :usuario_id, :plato_id
  json.url comentario_url(comentario, format: :json)
end

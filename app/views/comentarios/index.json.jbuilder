json.array!(@comentarios) do |comentario|
  json.extract! comentario, :id, :comentario, :puntos
  json.url comentario_url(comentario, format: :json)
end

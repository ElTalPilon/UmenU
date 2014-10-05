json.array!(@sodas) do |soda|
  json.extract! soda, :id, :nombre, :abre, :cierra, :iDesayuno, :fDesayuno, :iAlmuerzo, :fAlmuerzo, :iCena, :fCena
  json.url soda_url(soda, format: :json)
end

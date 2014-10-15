class ComentariosController < ApplicationController
  # GET /comentarios
  # GET /comentarios.json
  def index
    @comentarios = Comentario.all

    render json: @comentarios
  end

  # GET /comentarios/1
  # GET /comentarios/1.json
  def show
    @comentario = Comentario.find(params[:id])

    render json: @comentario.as_json(only: [:id, :comentario, :puntos, :plato_id, :usuario_id])
  end

  # POST /comentarios
  # POST /comentarios.json
  def create
    @comentario = Comentario.new(comentario_params)

    if @comentario.save
      render json: @comentario, status: :created, location: @comentario
    else
      render json: @comentario.errors, status: :unprocessable_entity
    end
  end

  # PATCH/PUT /comentarios/1
  # PATCH/PUT /comentarios/1.json
  def update
    @comentario = Comentario.find(params[:id])

    if @comentario.update(comentario_params)
      head :no_content
    else
      render json: @comentario.errors, status: :unprocessable_entity
    end
  end

  # DELETE /comentarios/1
  # DELETE /comentarios/1.json
  def destroy
    @comentario = Comentario.find(params[:id])
    @comentario.destroy

    head :no_content
  end

  # Never trust parameters from the scary internet, only allow the white list through. 
  def comentario_params 
    params.permit(:comentario, :puntos, :plato_id, :usuario_id)
  end

end

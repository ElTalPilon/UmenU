class PlatosController < ApplicationController
  # GET /platos
  # GET /platos.json
  def index
    @platos = Plato.all

    render json: @platos
  end

  # GET /platos/1
  # GET /platos/1.json
  def show
    @plato = Plato.find(params[:id])

    render json: @plato
  end

  # POST /platos
  # POST /platos.json
  def create
    @plato = Plato.new(params[:plato])

    if @plato.save
      render json: @plato, status: :created, location: @plato
    else
      render json: @plato.errors, status: :unprocessable_entity
    end
  end

  # PATCH/PUT /platos/1
  # PATCH/PUT /platos/1.json
  def update
    @plato = Plato.find(params[:id])

    if @plato.update(params[:plato])
      head :no_content
    else
      render json: @plato.errors, status: :unprocessable_entity
    end
  end

  # DELETE /platos/1
  # DELETE /platos/1.json
  def destroy
    @plato = Plato.find(params[:id])
    @plato.destroy

    head :no_content
  end
end
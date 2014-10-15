class SodasController < ApplicationController
  # GET /sodas
  # GET /sodas.json
  def index
    @sodas = Soda.all

    render json: @sodas
  end

  # GET /sodas/1
  # GET /sodas/1.json
  def show
    @soda = Soda.find(params[:id])

	if !@soda.plato.first.nil?
        render json: @soda.as_json(only:[:id, :nombre, :abre, :cierra, :iDesayuno, :fDesayuno, :iAlmuerzo, :fAlmuerzo, :iCena, :fCena, :long, :lat], include: [plato:{only: [:id]}])
    else
        render json: @soda.as_json(only:[:id, :nombre, :abre, :cierra, :iDesayuno, :fDesayuno, :iAlmuerzo, :fAlmuerzo, :iCena, :fCena, :long, :lat])
    end

  end
  # POST /sodas
  # POST /sodas.json
  def create
    @soda = Soda.new(soda_params)

    if @soda.save
      render json: @soda, status: :created, location: @soda
    else
      render json: @soda.errors, status: :unprocessable_entity
    end
  end

  # PATCH/PUT /sodas/1
  # PATCH/PUT /sodas/1.json
  def update
    @soda = Soda.find(params[:id])

    if @soda.update(soda_params)
      head :no_content
    else
      render json: @soda.errors, status: :unprocessable_entity
    end
  end

  # DELETE /sodas/1
  # DELETE /sodas/1.json
  def destroy
    @soda = Soda.find(params[:id])
    @soda.destroy

    head :no_content
  end

  def soda_params
    params.permit(:nombre, :abre, :cierra, :iDesayuno, :fDesayuno, :iAlmuerzo, :fAlmuerzo, :iCena, :fCena)
  end
end

class SnacksController < ApplicationController
  # GET /snacks
  # GET /snacks.json
  def index
    @snacks = Snack.all

    render json: @snacks
  end

  # GET /snacks/1
  # GET /snacks/1.json
  def show
    @snack = Snack.find(params[:id])

    render json: @snack.as_json(only: [:id, :nombre, :precio, :id_soda])
  end

  # POST /snacks
  # POST /snacks.json
  def create
    @snack = Snack.new(snack_params)

    if @snack.save
      render json: @snack, status: :created, location: @snack
    else
      render json: @snack.errors, status: :unprocessable_entity
    end
  end

  # PATCH/PUT /snacks/1
  # PATCH/PUT /snacks/1.json
  def update
    @snack = Snack.find(params[:id])

    if @snack.update(snack_params)
      head :no_content
    else
      render json: @snack.errors, status: :unprocessable_entity
    end
  end

  # DELETE /snacks/1
  # DELETE /snacks/1.json
  def destroy
    @snack = Snack.find(params[:id])
    @snack.destroy

    head :no_content
  end

  def snack_params
    params.permit(:nombre, :precio, :id_soda)
  end
end

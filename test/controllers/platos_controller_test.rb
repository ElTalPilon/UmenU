require 'test_helper'

class PlatosControllerTest < ActionController::TestCase
  setup do
    @plato = platos(:one)
  end

  test "should get index" do
    get :index
    assert_response :success
    assert_not_nil assigns(:platos)
  end

  test "should create plato" do
    assert_difference('Plato.count') do
      post :create, plato: { categoria: @plato.categoria, nombre: @plato.nombre, precio: @plato.precio, puntuacion: @plato.puntuacion, tipo: @plato.tipo }
    end

    assert_response 201
  end

  test "should show plato" do
    get :show, id: @plato
    assert_response :success
  end

  test "should update plato" do
    put :update, id: @plato, plato: { categoria: @plato.categoria, nombre: @plato.nombre, precio: @plato.precio, puntuacion: @plato.puntuacion, tipo: @plato.tipo }
    assert_response 204
  end

  test "should destroy plato" do
    assert_difference('Plato.count', -1) do
      delete :destroy, id: @plato
    end

    assert_response 204
  end
end

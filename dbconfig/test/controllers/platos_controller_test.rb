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
      post :create, plato: { calificaciones: @plato.calificaciones, categoria: @plato.categoria, nombre: @plato.nombre, precio: @plato.precio, tipo: @plato.tipo, total: @plato.total }
    end

    assert_response 201
  end

  test "should show plato" do
    get :show, id: @plato
    assert_response :success
  end

  test "should update plato" do
    put :update, id: @plato, plato: { calificaciones: @plato.calificaciones, categoria: @plato.categoria, nombre: @plato.nombre, precio: @plato.precio, tipo: @plato.tipo, total: @plato.total }
    assert_response 204
  end

  test "should destroy plato" do
    assert_difference('Plato.count', -1) do
      delete :destroy, id: @plato
    end

    assert_response 204
  end
end

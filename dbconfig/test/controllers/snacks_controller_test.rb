require 'test_helper'

class SnacksControllerTest < ActionController::TestCase
  setup do
    @snack = snacks(:one)
  end

  test "should get index" do
    get :index
    assert_response :success
    assert_not_nil assigns(:snacks)
  end

  test "should create snack" do
    assert_difference('Snack.count') do
      post :create, snack: { nombre: @snack.nombre, precio: @snack.precio }
    end

    assert_response 201
  end

  test "should show snack" do
    get :show, id: @snack
    assert_response :success
  end

  test "should update snack" do
    put :update, id: @snack, snack: { nombre: @snack.nombre, precio: @snack.precio }
    assert_response 204
  end

  test "should destroy snack" do
    assert_difference('Snack.count', -1) do
      delete :destroy, id: @snack
    end

    assert_response 204
  end
end

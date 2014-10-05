require 'test_helper'

class SodasControllerTest < ActionController::TestCase
  setup do
    @soda = sodas(:one)
  end

  test "should get index" do
    get :index
    assert_response :success
    assert_not_nil assigns(:sodas)
  end

  test "should create soda" do
    assert_difference('Soda.count') do
      post :create, soda: { abre: @soda.abre, cierra: @soda.cierra, fAlmuerzo: @soda.fAlmuerzo, fCena: @soda.fCena, fDesayuno: @soda.fDesayuno, iAlmuerzo: @soda.iAlmuerzo, iCena: @soda.iCena, iDesayuno: @soda.iDesayuno, nombre: @soda.nombre }
    end

    assert_response 201
  end

  test "should show soda" do
    get :show, id: @soda
    assert_response :success
  end

  test "should update soda" do
    put :update, id: @soda, soda: { abre: @soda.abre, cierra: @soda.cierra, fAlmuerzo: @soda.fAlmuerzo, fCena: @soda.fCena, fDesayuno: @soda.fDesayuno, iAlmuerzo: @soda.iAlmuerzo, iCena: @soda.iCena, iDesayuno: @soda.iDesayuno, nombre: @soda.nombre }
    assert_response 204
  end

  test "should destroy soda" do
    assert_difference('Soda.count', -1) do
      delete :destroy, id: @soda
    end

    assert_response 204
  end
end

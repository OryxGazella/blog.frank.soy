require 'test_helper'

class StaticPagesControllerTest < ActionController::TestCase
  test "should get i_code_java_2016" do
    get :i_code_java_2016
    assert_response :success
  end

end

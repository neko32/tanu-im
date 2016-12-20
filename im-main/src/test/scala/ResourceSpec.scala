
import org.tanuneko.im.util.Resource

/**
  * Created by neko32 on 2016/09/10.
  */
class ResourceSpec extends FeatureSpec with GivenWhenThen {

  info("Resource is a class which administrate all Resource IO")

  val testUserProp = "conf/tanuim_test.properties"

  feature("Resource") {
    scenario("Load user properties sucessfully") {
      Given(s"Load user properties from src/test/resources/${testUserProp}")
      Resource.initAppProperty(testUserProp)

      Then("Loaded properties should be available through getProperty()")
      assert(Resource.getAppProperty(Resource.RES_USER_NAME) == "testman")
      assert(Resource.getAppProperty(Resource.RES_GROUP_NAME) == "TEST_GRP")
    }
  }
}
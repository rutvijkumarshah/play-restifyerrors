import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "restify_errors"
  val appVersion      = "0.0.7"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )
   

}

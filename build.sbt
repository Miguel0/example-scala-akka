enablePlugins(JavaServerAppPackaging)

name := "hotel-service"

version := "0.1"

organization := "com.miguelisasmendi"

scalaVersion := "2.11.6"

resolvers ++= Seq("Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
                  Resolver.bintrayRepo("hseeberger", "maven"))

libraryDependencies ++= {
  val AkkaVersion           = "2.3.9"
  val AkkaHttpVersion       = "2.0.1"
  val Json4sVersion         = "3.2.11"
  val SlickVersion          = "3.1.1"
  val MysqlConnectorVersion = "5.1.36"
  Seq(
    "com.typesafe.akka" %%  "akka-slf4j"             % AkkaVersion,
    "com.typesafe.akka" %%  "akka-http-experimental" % AkkaHttpVersion,
    "ch.qos.logback"    %   "logback-classic"        % "1.1.2",
    "org.json4s"        %%  "json4s-native"          % Json4sVersion,
    "org.json4s"        %%  "json4s-ext"             % Json4sVersion,
    "de.heikoseeberger" %%  "akka-http-json4s"       % "1.4.2",
    "mysql"              %  "mysql-connector-java"   % MysqlConnectorVersion,
    "com.typesafe.slick" %% "slick-hikaricp"         % SlickVersion,
    "com.typesafe.slick" %% "slick"                  % SlickVersion
  )
}

// Assembly settings
mainClass in Global := Some("com.miguelisasmendi.Main")

jarName in assembly := "hotel-server.jar"

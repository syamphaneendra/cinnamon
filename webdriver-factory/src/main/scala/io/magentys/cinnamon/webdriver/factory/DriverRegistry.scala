package io.magentys.cinnamon.webdriver.factory

import io.appium.java_client.remote.{MobileCapabilityType, MobilePlatform}
import org.openqa.selenium.WebDriver
import org.openqa.selenium.remote.server.{DefaultDriverFactory, DefaultDriverProvider}
import org.openqa.selenium.remote.{BrowserType, DesiredCapabilities}

import scala.util.Try

object DriverRegistry {

  val capabilitiesToDriverProvider = Map(
    BrowserType.CHROME -> (DesiredCapabilities.chrome, "org.openqa.selenium.chrome.ChromeDriver"),
    BrowserType.FIREFOX -> (DesiredCapabilities.firefox, "org.openqa.selenium.firefox.FirefoxDriver"),
    BrowserType.IE -> (DesiredCapabilities.internetExplorer, "org.openqa.selenium.ie.InternetExplorerDriver"),
    BrowserType.EDGE -> (DesiredCapabilities.edge, "org.openqa.selenium.edge.EdgeDriver"),
    BrowserType.OPERA_BLINK -> (DesiredCapabilities.operaBlink, "org.openqa.selenium.opera.OperaDriver"),
    BrowserType.SAFARI -> (DesiredCapabilities.safari, "org.openqa.selenium.safari.SafariDriver"),
    BrowserType.PHANTOMJS -> (DesiredCapabilities.phantomjs, "org.openqa.selenium.phantomjs.PhantomJSDriver"),
    BrowserType.HTMLUNIT -> (DesiredCapabilities.htmlUnit, "org.openqa.selenium.htmlunit.HtmlUnitDriver"),
    MobilePlatform.ANDROID -> (new DesiredCapabilities(new java.util.HashMap[String, Any] {
      put(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID)
    }), "io.appium.java_client.android.AndroidDriver"),
    MobilePlatform.IOS -> (new DesiredCapabilities(new java.util.HashMap[String, Any] {
      put(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.IOS)
    }), "io.appium.java_client.ios.IOSDriver")
  )

  val locals = new DefaultDriverFactory
  capabilitiesToDriverProvider foreach { case (k, v) => locals.registerDriverProvider(new DefaultDriverProvider(v._1, v._2)) }

  def getDriverClass(capabilities: DesiredCapabilities): Option[Class[_ <: WebDriver]] = {
    val driverClassNames = capabilitiesToDriverProvider.filter(p => p._1 == capabilities.getBrowserName || p._1 == capabilities.getCapability(MobileCapabilityType.PLATFORM_NAME)).values
    Try(Class.forName(driverClassNames.head._2).asSubclass(classOf[WebDriver])).toOption
  }

  def addDriverProvider(capabilities: DesiredCapabilities, providerClass: String) {
    locals.registerDriverProvider(new DefaultDriverProvider(capabilities, providerClass))
  }
}
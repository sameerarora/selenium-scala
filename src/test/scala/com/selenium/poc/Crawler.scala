package com.selenium.poc

import org.openqa.selenium.WebElement
import org.scalatest.FlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.selenium.Chrome
import org.scalatest.time.{Seconds, Span}

import scala.annotation.tailrec
import scala.collection.JavaConverters._


class Crawler extends FlatSpec with Chrome with Matchers {
  @tailrec
  private def foo(elements: List[WebElement], result: List[(String, String)] = Nil): List[(String, String)] = {
    elements match {
      case a +: b +: c => foo(c, (a.getText, b.getText) +: result)
      case _ +: Nil => result
      case Nil => result
    }
  }

  "The blog app home page" should "have the correct title" in {
    go to "http://jewels.brahmakumaris.org/"
    //webDriver.findElementById("videoChkBox").click()
    webDriver.findElementById("fileChkBox").click()
    webDriver.findElementByCssSelector(".ui-multiselect").click()
    webDriver.findElementById("ui-multiselect-languageSelect-option-1").click()
    webDriver.findElementByCssSelector("..ui-multiselect-close").click()
    //$('#ui-multiselect-languageSelect-option-1').click()
    val tuples = compute(aggregate())

    println(s"Total Distinct records : ${tuples.distinct.size}")
    print(tuples)
  }

  private def print(tuples: List[(String, String)]) = {
    println(s"Total records scanned : ${tuples.size}")
    println(s"Distinct Hindi Audio ${tuples.distinct.filter(_._2.toLowerCase.contains("hindi")).size}")
  }

  def aggregate(l: List[(String, String)] = Nil): List[(String, String)] = {
    val elements: List[WebElement] = webDriver.findElementsByCssSelector(".speaker span").asScala.toList
    l ++ foo(elements)
  }

  @tailrec
  private def compute(result: List[(String, String)] = Nil): List[(String, String)] = {
    val hrefs: List[WebElement] = webDriver.findElementsByCssSelector("a[href^='https://drive.google']").asScala.toList
    hrefs.foreach(x => println(x.getAttribute("href")))
    val links = webDriver.findElementsByCssSelector(".Prev img").asScala.toList
    val next = links.find(_.getAttribute("src").equalsIgnoreCase("http://jewels.brahmakumaris.org/images/next.gif"))
    next match {
      case Some(link) => {
        if (result.length < 100) {
          link.click()
          implicitlyWait(Span(2, Seconds))
          val tuples = result ++ aggregate()
          print(tuples)
          compute(tuples)
        } else {
          result
        }
      }
      case None => result
    }
  }

}

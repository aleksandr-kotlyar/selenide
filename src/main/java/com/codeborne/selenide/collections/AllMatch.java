package com.codeborne.selenide.collections;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.ex.MatcherError;
import com.codeborne.selenide.impl.WebElementsCollection;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.function.Predicate;

public class AllMatch extends CollectionCondition {
  private static final String MATCHER = "all";
  protected final String description;
  protected final Predicate<WebElement> predicate;

  public AllMatch(String description, Predicate<WebElement> predicate) {
    this.description = description;
    this.predicate = predicate;
  }

  @Override
  public boolean apply(List<WebElement> elements) {
    if (elements.isEmpty()) {
      return false;
    }
    return elements.stream().allMatch(predicate);
  }

  @Override
  public void fail(WebElementsCollection collection, List<WebElement> elements, Exception lastError, long timeoutMs) {
    if (elements == null || elements.isEmpty()) {
      ElementNotFound elementNotFound = new ElementNotFound(collection, toString(), lastError);
      elementNotFound.timeoutMs = timeoutMs;
      throw elementNotFound;
    } else {
      throw new MatcherError(MATCHER, description, explanation, collection, elements, lastError, timeoutMs);
    }
  }

  @Override
  public boolean applyNull() {
    return false;
  }

  @Override
  public String toString() {
    return String.format("%s match [%s] predicate", MATCHER, description);
  }
}

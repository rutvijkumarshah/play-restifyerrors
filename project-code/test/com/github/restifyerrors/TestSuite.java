package com.github.restifyerrors;

import org.junit.runners.Suite;
import org.junit.runner.RunWith;

@RunWith(Suite.class)
@Suite.SuiteClasses({ExceptionRegistryTest.class, HTTPExceptionTest.class})
public class TestSuite {
 /***
  * RESTErrorHandler's Test is not possible inside plugin project
  * sample application has functional tests for RESTErrorHandler
  */
}
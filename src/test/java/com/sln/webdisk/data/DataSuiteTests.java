package com.sln.webdisk.data;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * This is a bootstrap suite that will run tests in {@code UserDBTest.class}
 *<br>
 * <p>The use of @ClassRule is described here: https://github.com/junit-team/junit4/wiki/Rules#classrule
 * <p>and here: https://garygregory.wordpress.com/2011/09/25/understaning-junit-method-order-execution/
 *
 * @author  Sln
 * @version 1.0
 */
@RunWith(Suite.class)
@SuiteClasses({ UserDBTest.class })		// Suite.class runner will run classes listed here
public class DataSuiteTests {
	
	//@ClassRule
	//public static final ExternalResource resource = new MyExternalResource();

}
	
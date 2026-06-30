package com.orangehrm.utilities;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
	private int retryCount = 0; // number of retries
	private static final int MaxRetryCount = 2;// Maximum no of retries

	@Override
	public boolean retry(ITestResult result) {
		if (retryCount < MaxRetryCount) {
			retryCount++;
			return true;
		}
		return false;
	}
}

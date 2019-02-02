# Tests
This project contains automated user acceptance and capacity tests.

# Performance tests
JMeter is used for the implementation of performance tests. Changes of the performance tests requires at least [JMeter 5.0](https://jmeter.apache.org/download_jmeter.cgi)

# Execution

## Local execution
The following maven command will run all the cucumber tests, JMeter tests and it will generate a graph out of the performance results:

```
mvn clean verify jmeter-graph:create-graph
```

## Remote execution
Travis CI app is used to build the application. The build needs the following environment variables set in order to work:
* TESTINGBOT_CREDENTIALS: This token is used to connect to [Testingbot](https://testingbot.com) to execute the Selenium tests 
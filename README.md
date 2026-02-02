# Payroll

This repository has been created as a learning tool for getting to grips with spring boot.

## Schema - infra

![Infra](./docs/infra.png)

## First build

After cloning this repository:

* Resolve dependency (without running anything)

```bash
    mvn dependency:resolve
```

```
    //expected result
    [...]
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time:  10.070 s
    [INFO] Finished at: 2026-01-25T12:57:28+01:00
    [INFO] ------------------------------------------------------------------------
```

* Run the api with the dev profile (inject test data)

```bash
    mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

```
    //expected result
    [...]
    2026-01-25T12:58:03.846+01:00  INFO 420156 --- [payroll] [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path ''
    2026-01-25T12:58:03.853+01:00  INFO 420156 --- [payroll] [           main] ch.etml.es.payroll.PayrollApplication    : Started PayrollApplication in 3.167 seconds (process running for 3.436)
    2026-01-25T12:58:03.906+01:00  INFO 420156 --- [payroll] [           main] c.e.e.payroll.Repositories.LoadDatabase  : Preloading Employee{id=1, name='Bilbo Baggins', role='BURGLAR'}
    2026-01-25T12:58:03.908+01:00  INFO 420156 --- [payroll] [           main] c.e.e.payroll.Repositories.LoadDatabase  : Preloading Employee{id=2, name='Frodo Baggins', role='THIEF'}
```

* Run the api in with de prod profile (default)

```bash
    mvn spring-boot:run
```

```
    //expected result
    2026-01-25T12:58:56.193+01:00  INFO 420296 --- [payroll] [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path ''
    2026-01-25T12:58:56.200+01:00  INFO 420296 --- [payroll] [           main] ch.etml.es.payroll.PayrollApplication    : Started PayrollApplication in 3.046 seconds (process running for 3.317)
```


* To retrieve the dependencies, compile and run the program with a single command

```bash
    mvn clean spring-boot:run
```

## Test using http requests manually

* GET the full list of employees (expected HTTP 200). Pipe response to jq to pretty-print the JSON.
```bash
    curl.exe -s -X GET http://localhost:8080/api/v1/employees | jq
```
* GET the employee with id 1. Pipe response to jq to pretty-print the JSON object for that employee.
```bash
    curl.exe -s -X GET http://localhost:8080/api/v1/employees/1 | jq
```
## Run the tests

* For all tests classes

```
mvn test
```

* Only a specific class test

```
mvn -Dtest=EmployeeGetTest test
```


## Run the api with Docker

//TODO

* How to run the test inside the container ?

//TODO

* Run the image

//TODO
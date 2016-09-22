[![Build Status](https://travis-ci.org/intesens/kinto-http-java.svg?branch=master)](https://travis-ci.org/intesens/kinto-http-java)

# Kinto java client

A Java HTTP Client for the [Kinto](http://kinto-storage.org/) API.

Based of the Python library [kinto-http.py](https://github.com/Kinto/kinto-http.py) and its JavaScript equivalent 
[kinto-http.js](https://github.com/Kinto/kinto-http.js).

## Usage
1. The library is published on maven central, just add the following to your pom.xml:
    ```xml
    <dependency>
        <groupId>com.intesens</groupId>
        <artifactId>kinto-http-java</artifactId>
        <version>0.2.2</version>
    </dependency>
    ```
      
2. Create an instance of `KintoClient`, several constructors are available (see javadoc)
3. Auth to kinto is only supported via headers at the moment:
    ```java
    Map<String, String> headers = new HashMap<String, String>();
    headers.put("Authorization", "Basic <token>");
    headers.put("Accept", "application/json");
    headers.put("Content-Type", "application/json");
    
    KintoClient kintoClient = new KintoClient("https://module-type-repo.herokuapp.com/v1", headers);
    ```

## Contributing
Contribution is open to all.

Intesens team will review the PRs but if you wish to take a leading role in the project, get in touch with us (opensource, intesens, com)

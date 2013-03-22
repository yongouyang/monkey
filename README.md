The Monkey Web Framework
================

Monkey is designed to be a light-weight web framework (HTTP+JSON) that is easy to use and extend. It uses 

- **Jetty** as its servlet container, 
- **Spring** to manage its beans and dependencies, 
- **Spring MVC** to provide a flexible way to create your own controller to handle request and response
- **MongoDB** as its persistent layer
- **HornetQ** as its messaging layer
- **Gradle** as its build tool
- **Groovy** for writing tests
- **JUnit** as a generic testing framework
- **Mockito** as its mocking framework
- **Selenium & Cucumber** as its UI testing framework

Setup of a Deployment Environment (unix / linux)
================
1. **Create common folders**
    
        mkdir -p /app/monkey/pkgs
        mkdir -p /app/monkey/logs    
        mkdir -p /app/monkey/tools

2. **Setup Java JDK**

    After copyping java jdk to /app/monkey/tools, e.g. /app/monkey/tools/jdk1.6.0_33, create a symbolic link "java" `ln -s /app/monkey/tools/jdk1.6.0_33 java`
    
3. **Setup Mongo DB**

    ensure the mongodb folder has been created `mkdir -p /app/monkey/storage/mongodb`
    
    download the binary `youyang@monkey-dev-01:/app/monkey/storage/mongodb> wget http://fastdl.mongodb.org/linux/mongodb-linux-x86_64-2.4.0.tgz`
    
    extract the compressed file to current folder `youyang@monkey-dev-01:/app/monkey/storage/mongodb> tar xvf mongodb-linux-x86_64-2.4.0.tgz`
    
    ensure data folders are created:
    
        youyang@monkey-dev-01:/app/monkey/storage/mongodb> mkdir -p /app/monkey/storage/mongodb/data/db
        youyang@monkey-dev-01:/app/monkey/storage/mongodb> mkdir -p /app/monkey/storage/mongodb/data/log
        youyang@monkey-dev-01:/app/monkey/storage/mongodb> mkdir -p /app/monkey/storage/mongodb/data/config
    
    create a symbolic link to the mongodb binary `youyang@monkey-dev-01:/app/monkey/storage/mongodb> ln -s mongodb-linux-x86_64-2.4.0 mongodb-distribution`       
        
(to be updated ...)
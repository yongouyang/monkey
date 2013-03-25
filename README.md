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
    
    download the start/stop script for mongodb `youyang@monkey-dev-01:/app/monkey/storage/mongodb> wget http://github.com/yongouyang/monkey/blob/master/deployment/mongodb/mongo.sh`
    
    change mongo.sh permission `youyang@monkey-dev-01:/app/monkey/storage/mongodb> chmod 700 mongo.sh`
    
    create a private key file used for cluster authentication
    
        youyang@monkey-dev-01:/app/monkey/storage/mongodb> echo "not for production" >>  keyFile.txt
        youyang@monkey-dev-01:/app/monkey/storage/mongodb> chmod 600 keyFile.txt 
    
    start up a MongoDB data node in replica set mode `youyang@monkey-dev-01:/app/monkey/storage/mongodb> ./mongo.sh start DEVAPAC dataNode` , which will by default start up on port 6646, unless otherwise specified
    
    initialise the replica set config and add an admin user to the admin DB:
    
        youyang@monkey-dev-01:/app/monkey/storage/mongodb> mongodb-distribution/bin/mongo --port 6646
        MongoDB shell version: 2.4.0
        connecting to: 127.0.0.1:6646/test
        > rs.status()
        {
                "startupStatus" : 3,
                "info" : "run rs.initiate(...) if not yet done for the set",
                "ok" : 0,
                "errmsg" : "can't get local.system.replset config from self or any seed (EMPTYCONFIG)"
        }
        > rs.initiate()
        {
                "info2" : "no configuration explicitly specified -- making one",
                "me" : "monkey-dev-01:6646",
                "info" : "Config now saved locally.  Should come online in about a minute.",
                "ok" : 1
        }
        > rs.status()
        {
                "set" : "DEVAPAC",
                "date" : ISODate("2013-03-25T04:55:52Z"),
                "myState" : 1,
                "members" : [
                        {
                                "_id" : 0,
                                "name" : "monkey-dev-01:6646",
                                "health" : 1,
                                "state" : 1,
                                "stateStr" : "PRIMARY",
                                "uptime" : 197,
                                "optime" : {
                                        "t" : 1364187312,
                                        "i" : 1
                                },
                                "optimeDate" : ISODate("2013-03-25T04:55:12Z"),
                                "self" : true
                        }
                ],
                "ok" : 1
        }
        DEVAPAC:PRIMARY> use admin
        switched to db admin
        DEVAPAC:PRIMARY> db.addUser("adminUser","adminPass")
        {
                "user" : "adminUser",
                "readOnly" : false,
                "pwd" : "873a9323c57ae73c0c7dd7da7efa1d12",
                "_id" : ObjectId("514fd95a90fd44ce267505d1")
        }
        > db.auth("adminUser","adminPass")
        1
        
    continue to add readWrite and readOnly users to the DB of the application, named `monkey-youyang-DEVAPAC`
    
        DEVAPAC:PRIMARY> use monkey-youyang-DEVAPAC
        switched to db monkey-youyang-DEVAPAC
        DEVAPAC:PRIMARY> db.addUser("writeUser","writePass")
        {
                "user" : "writeUser",
                "readOnly" : false,
                "pwd" : "0a237881a3f575ee4d34d94e63a55832",
                "_id" : ObjectId("514fda1290fd44ce267505d2")
        }
        DEVAPAC:PRIMARY> db.addUser({user:"readUser",pwd:"readPass",roles:["read"]})
        {
                "user" : "readUser",
                "pwd" : "10413e245468b315d524d9db3025a2e7",
                "roles" : [
                        "read"
                ],
                "_id" : ObjectId("514fdcab90fd44ce267505d3")
        }


    
    
        
(to be updated ...)
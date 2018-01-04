# overdelivery-mgmt
A simplified proof-of-concept project for managing ads over-delivery utilizing Spring Cloud and Apache Kafka. Based on the following article:
- https://medium.com/@Pinterest_Engineering/using-kafka-streams-api-for-predictive-budgeting-9f58d206c996

## Requirements to build this project

1.    Java 8
2.    Maven

## Requirements to run the examples

1.    [Apache Kafka](https://kafka.apache.org/downloads). See section with binary downloads and the recommended stable version. At the moment of 
writing the version used is: kafka_2.11-0.10.1.0
2.    A modified version of [JSON Data Generator](https://github.com/kmandalas/json-data-generator) originally provided by [ACES,Inc]
(http://acesinc.net/)

## Setup Instructions

#### Extract the kafka_2.11-0.10.1.0.tgz file ####
    tar -xvzf kafka_2.11-0.10.1.0.tgz


#### Start zookeeper and kafka
```
      kafka-install-dir/bin/zookeeper-server-start.sh kafka-install-dir/conf/zookeeper.properties
      kafka-install-dir/bin/kafka-server-start.sh kafka-install-dir/conf/server.properties
```

#### Install the Json-Data-Generator  
Clone/fork the modified version of [JSON Data Generator](https://github.com/kmandalas/json-data-generator) and follow the instructions provided 
[here](https://github.com/kmandalas/json-data-generator#running-the-generator)

#### Setup the overdelivery-mgmt repo
Clone or fork the repo
```
     git clone git@github.com:kmandalas/overdelivery-mgmt    
     cd overdelivery-mgmt
```     
Then copy the json config files to json generator conf directory
```
    cp streaming-workflows/* <dir>/json-data-generator-1.2.0/conf
```    
    
Create all the topics required by the examples
```
     kafka-install-dir/bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic ad-insertion-input
     kafka-install-dir/bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 2 --topic predicted-spend-output
     kafka-install-dir/bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 2 --topic impressions
```     

### Running the Infrastructure services ###
Run each service in different console/terminal. The recommended order is the following:
#### 1. Config Service
     cd <dir>/config/
     mvn spring-boot:run
     
#### 2. Registry (Service Discovery using Eureka)
     cd <dir>/registry/
     mvn spring-boot:run     

#### 3. API Gateway (Zuul)
     cd <dir>/gateway/
     mvn spring-boot:run    
          
### Running the Functional services ###
#### 1. Budget Service
     cd <dir>/budget/
     mvn spring-boot:run

#### 2. Inventory Service
     cd <dir>/inventory/
     mvn spring-boot:run

### Running the kafka consumers and stream ###
#### 1. predicted-spend-consumer-0
     cd <dir>/predicted-spend-consumer/
     mvn spring-boot:run -Drun.arguments="--partition-no=0"
     
#### 2. predicted-spend-consumer-1
     cd <dir>/predicted-spend-consumer/
     mvn spring-boot:run -Drun.arguments="--partition-no=1"
     
#### 3. impressions-consumer-0
     cd <dir>/impressions-consumer/
     mvn spring-boot:run -Drun.arguments="--partition-no=0"
     
#### 4. impressions-consumer-1
     cd <dir>/impressions-consumer/
     mvn spring-boot:run -Drun.arguments="--partition-no=1"
     
#### 5. spend-aggregator (kafka stream)
     cd <dir>/spend-aggregator/
     mvn spring-boot:run    

### Eureka Dashboard ###
Once you have started all the services, check the [Eureka dashboard] (http://localhost:8761) 

Keep in mind that the Service Discovery mechanism needs some time after all applications startup. 
Any service is not available for discovery by clients until the instance, the Eureka server and the client all have the same metadata in their local cache, 
so it could take 3 heartbeats. Default heartbeat period is 30 seconds.

If everything is started OK, you should have a view similar to the following one:

![alt text](https://github.com/kmandalas/overdelivery-mgmt/raw/master/assets/eureka.png "eureka dashboard")

## Status
In progress...
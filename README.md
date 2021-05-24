# GAME SHOP

## What's in here?
This is a basic CRUD api for managing the games inventory and shopping carts.

## How to run it?
To run only the gameshop service without any docker-compose functionallities, please run
(note that only Swagger, Actuator and Prometheus endpoints will be available then):
> java -jar compose-config/gameshop-0.0.1-SNAPSHOT.jar --spring.profiles.active=prd


This software uses docker-compose being able to deal with version 3.3 of the compose file.
To start the gameshop service alone, please run the command below:
> sudo docker-compose up -d --build gameshop

To be able to run elasticsearch docker container on your machine and use Kibana for further analytics, please run the command below
(if not - just skip this command):
> sudo sysctl -w vm.max_map_count=262144

To start the gameshop service and its monitoring-analytics services
(filebeat, elasticsearch and kibana), just  
go to the project root directory and run the following commands:
> sudo docker-compose up -d --build

## Endpoints 
* [Swagger](http://localhost:8080/swagger-ui.html)
  
  ### Monitoring and analytics
  
  * [Actuator](http://localhost:8080/actuator)
  * [Prometheus](http://localhost:8080/actuator/prometheus)
  * [Elasticsearch](http://localhost:9200)
  * [Kibana](http://localhost:5601)
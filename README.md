# GAME SHOP

## What's in here?
This is a basic CRUD api for managing the games inventory and shopping carts.

## How to run it?
This software requires docker-compose being able to deal with version 3.3 of the compose file.
To start the gameshop service and its monitoring-analytics services
(filebeat, elasticsearch and kibana), just  
go to the project root directory and run the following commands:
> sudo sysctl -w vm.max_map_count=262144
>
> docker-compose up -d
## Endpoints 
* [Swagger](http://localhost:8080/swagger-ui.html)
  
  ### Monitoring and analytics
  
  * [Actuator](http://localhost:8080/actuator)
  * [Prometheus](http://localhost:8080/actuator/prometheus)
  * [Elasticsearch](http://localhost:9200)
  * [Kibana](http://localhost:5601)
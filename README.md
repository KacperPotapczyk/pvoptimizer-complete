# Photovoltaic and energy storage optimizer backend services
This repository contains all required backend services to crate optimization tasks, send it to computation and receive results. Communication between services is realized via Kafka using Avro messages.

## backend
This module is responsible for creation and persistane of optimization tasks and all task components. Backend API is available via apigateway on url path "/pvoptimizer/**".

## calculationpreprocesor
Validates tasks and translates them from bussiness domain of backend service to pvoptimizer calculation tasks.

## resultpostprocessor
Translates pvoptimizer calculation results to bussiness domain of backend service.

## pvoptimizer
Optimization engine available [here](https://github.com/KacperPotapczyk/pvoptimizer).

## utils
docker-compose.yml contains configuration to start required environment containers and abovementioned services. Apigateway, eurekaserver and configserver are additional services that supports microservices architecture.

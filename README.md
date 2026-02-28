# ms-core-data

This microservice is a fundamental part of **RIntellix** system. 

**RIntellix**  is a credit risk assessment platform designed to simplify the process of analyzing the risks associated with granting products within a specific bank. 

The functionality of this microservice **ms-core-data** is to serve a DAL infrastructure to communicate with the microservices for specific purposes within the existing data hub , offering a protection layer on both sides and also a centralized repository where we can read and write new data.

This microservice is a temporary solution while planning the new approach of creating independent microservices with their own databases in order to reduce the dependencies between microservices and between this DAL.

## OPERATIONS: 

### GET REQUESTS:

For this endpoint we need the following classes or objects in our domain-model: 

+ Request: 
+ RequestDetails:
+ PropertyCollateral:
+ RequestStatus:
+ Purpose:
+ RequestType:
+ Money:
+ Party:
+ Person:

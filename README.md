# My fancy app

## Table of Contents
+ [About](#about)
+ [Requirements](#requirements)
+ [Usage](#usage)
+ [TODO](#todo)
+ [Questions](#questions)

## About <a name = "about"></a>
My fancy app

## Requirements <a name = "requirements"></a>
- Java 11
- Gradle
- Docker

## Usage <a name = "usage"></a>

1. Execute `start.sh` script
2. Wait for Spring Boot to start
3. Visit `http://localhost:8080/swagger-ui.html`

Alternative way:
1. Run command `docker-compose -f docker-compose-postgres.yaml up`
2. Run application using Intellij
3. Visit `http://localhost:8080/swagger-ui.html`

# TODO <a name = "todo"></a>
- [ ] Add logging
- [ ] Add the Liquibase
- [ ] Use an in-memory database for testing
- [ ] Fix price precision
- [ ] Add different currencies

# Questions <a name = "questions"></a>
1. Propose a protocol / method and justify your choice
    - I would use OAuth2 and JWT token - safe and easy to use with Spring. 
    - Network ACL for AWS application - it's controller traffic in and out of one or more subnets, according to the provided rules. It is a firewall ensuring network security. Read more here: https://www.netstar.co.uk/why-do-i-need-a-firewall-business

2. How can you make the service redundant? What considerations should you do?
   - Amazon EC2 Auto scale to handle more requests. It requires load balancer like AWS Elastic Load Balancing.
   - AWS Cluster Autoscaler automatically adjusts the number of kubernetes nodes in cluster when pods fail or are rescheduled onto other nodes
   - Kubernetes Horizontal Pod Autoscaler (HPA) - `The Horizontal Pod Autoscaler changes the shape of your Kubernetes workload by automatically increasing or decreasing the number of Pods in response to the workload's CPU or memory consumption, or in response to custom metrics reported from within Kubernetes or external metrics from sources outside of your cluster.` ~ https://cloud.google.com/kubernetes-engine/docs/concepts/horizontalpodautoscaler 
   - Kubernetes Vertical Pod Autoscaling - https://cloud.google.com/kubernetes-engine/docs/concepts/verticalpodautoscaler
   - Kubernetes work like load balancer
   - Last time I heard about Circuit Breaker pattern (https://martinfowler.com/bliki/CircuitBreaker.html). It is used to detect failures and encapsulates the logic of preventing a failure from constantly recurring, during maintenance, temporary external system failure or unexpected system difficulties.
   - A few tips from Netflix which I would consider - https://netflixtechblog.com/fault-tolerance-in-a-high-volume-distributed-system-91ab4faae74a
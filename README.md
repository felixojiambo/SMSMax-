# **SMSMax: High-Performance Bulk SMS Delivery System**

**SMSMax** is a scalable and reliable bulk SMS delivery system designed to handle sending up to 14,000,000 messages efficiently. Built using **Spring Boot**, **Kafka**, **PostgreSQL**, **Spring Security**, and other essential technologies, SMSMax supports high throughput, fault tolerance, and minimal downtime. It includes message queuing, delivery tracking, retries for failed deliveries, rate limiting, and real-time monitoring.

---

## **Key Features**

- **Scalable SMS Delivery**: Capable of delivering up to 14 million messages with ease using Apache Kafka for message queuing.
- **Asynchronous Processing**: Leverages Kafka to queue messages, ensuring efficient processing by workers.
- **Retry Mechanism**: Automatically retries failed SMS deliveries using exponential backoff.
- **Rate Limiting**: Controls the flow of SMS requests to avoid system overload using Bucket4j.
- **Security**: Implements JWT-based authentication with Spring Security for securing API endpoints.
- **Monitoring & Logging**: Integrated with Prometheus, Grafana, and ELK Stack for metrics collection and centralized logging.
- **Minimal Downtime**: Uses Docker and Kubernetes for containerization and orchestration to ensure high availability and scalability.

---

## **System Architecture Overview**

### **Components:**

1. **API Layer**:
   - **Spring Boot** application exposing RESTful endpoints to accept SMS sending requests and status updates.

2. **Message Queue**:
   - **Apache Kafka** acts as the message broker for queuing SMS tasks for workers to process asynchronously.

3. **Worker Services**:
   - Kafka consumers read messages from the queue and send them to the SMS gateway. They also update the database with delivery statuses.

4. **Database**:
   - **PostgreSQL** is used for storing user information, SMS metadata, and delivery statuses with Spring Data JPA and Hibernate.

5. **Security**:
   - **Spring Security** with JWT secures all API endpoints.

6. **Rate Limiting**:
   - **Bucket4j** is used for enforcing rate limits on the number of requests.

7. **Retry Mechanism**:
   - **Spring Retry** manages retries for failed SMS deliveries with exponential backoff.

8. **Monitoring & Logging**:
   - **Prometheus** and **Grafana** for real-time system metrics and dashboards.
   - **ELK Stack** (Elasticsearch, Logstash, Kibana) for centralized logging and monitoring.

---

## **Technology Stack**

- **Spring Boot**: Backend framework for building REST APIs.
- **Spring Data JPA** and **Hibernate**: ORM for interacting with PostgreSQL.
- **Spring Security**: For securing API endpoints with JWT authentication.
- **Apache Kafka**: Asynchronous message broker for queuing tasks.
- **PostgreSQL**: Database for storing user and SMS data.
- **Bucket4j**: Rate limiting library.
- **Spring Retry**: Handles retries for failed operations.
- **Docker & Kubernetes**: Containerization and orchestration for deployment.
- **Prometheus & Grafana**: Monitoring.
- **ELK Stack (Elasticsearch, Logstash, Kibana)**: Centralized logging.

---

## **How it Works**

1. **Client Interaction**:
   - Clients authenticate using JWT tokens and send SMS requests through the API.

2. **Request Handling**:
   - The API validates the request, enforces rate limiting, and pushes the task into a Kafka queue.

3. **Message Processing**:
   - Kafka consumers read tasks from the queue, send SMS messages via the gateway, and update the database with delivery status.

4. **Delivery Tracking**:
   - Delivery receipts are tracked via callbacks or polling, with statuses updated in the database.

5. **Retries**:
   - If SMS delivery fails, the system automatically retries based on the configured retry strategy.

---

## **Installation Instructions**

### **Prerequisites**

- **Java 17+**
- **Maven** for building the project.
- **PostgreSQL** as the database.
- **Kafka** for message queuing.
- **Docker & Kubernetes** for containerization and orchestration.

### **Steps**

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/felixojiambo/SMSMax.git
   cd SMSMax
   ```

2. **Build the Application**:
   ```bash
   mvn clean install
   ```

3. **Configure the Application**:
   - Update the `application.yml` file with your PostgreSQL and Kafka configurations.

4. **Run the Application**:
   ```bash
   mvn spring-boot:run
   ```

5. **Run with Docker**:
   - Ensure Docker is running, and start the services using the provided `docker-compose.yml` file:
   ```bash
   docker-compose up -d
   ```

---

## **REST API Endpoints**

### **1. Send SMS**

**POST** `/api/sms/send`
  
**Request**:
```json
{
  "phoneNumber": "+1234567890",
  "message": "Hello, this is a test message!"
}
```

**Response**:
```json
{
  "status": "SMS Queued successfully",
  "trackingId": "12345"
}
```

### **2. Check SMS Status**

**GET** `/api/sms/status/{trackingId}`

**Response**:
```json
{
  "trackingId": "12345",
  "status": "DELIVERED"
}
```

---

## **Monitoring**

- **Prometheus** exposes system metrics at `/actuator/prometheus`.
- **Grafana** can be used to visualize metrics.
- Logs are centralized using the **ELK Stack** and accessible via **Kibana**.

---

## **Deployment & Scalability**

The application can be containerized using **Docker** and orchestrated with **Kubernetes** for automated scaling, fault tolerance, and zero-downtime deployments.

1. **Docker**:
   - Build and run the application with Docker:
     ```bash
     docker build -t smsmax .
     docker run -p 8080:8080 smsmax
     ```

2. **Kubernetes**:
   - Deploy the application using Kubernetes:
     ```bash
     kubectl apply -f kubernetes/deployment.yml
     ```

3. **Scaling**:
   - **Kubernetes Horizontal Pod Autoscaler** can be used to automatically scale based on CPU usage or queue length.

---

## **Contributing**

We welcome contributions! Feel free to submit a pull request or open an issue for discussion.

---

## **License**

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.

---

## **Contact**

For more information, contact us at:
- **Email**: support@smsmax.com
- **Website**: [www.smsmax.com](https://www.smsmax.com)

---


# Application-Form-Management-with-Vert.x-and-MongoDB
Description

This project is a simple web application for managing application forms using the Vert.x framework and MongoDB. It provides RESTful APIs for creating, retrieving, updating, and deleting application forms.
Features

    Create Application Form: Allows users to submit application forms with relevant information.
    Retrieve Application Form: Retrieves application forms based on their unique IDs.
    Update Application Form: Modifies existing application forms using their unique IDs.
    Delete Application Form: Deletes application forms based on their unique IDs.

Technologies Used

    Java
    Vert.x
    MongoDB
    Maven

    Prerequisites

Before running the project, ensure that the following are installed:

    Java Development Kit (JDK)
    Maven
    MongoDB
    Access the application at http://localhost:8080.

Configuration

Update the MongoDB connection details in the application.properties file.
API Endpoints

    Create Application Form:
        Method: POST
        Endpoint: /api/createform

    Retrieve Application Form:
        Method: GET
        Endpoint: /api/getform/:id

    Update Application Form:
        Method: PUT
        Endpoint: /api/updateform/:id

    Delete Application Form:
        Method: DELETE
        Endpoint: /api/deleteform/:id

Folder Structure


.
├── src
│   ├── main
│   │   ├── java
│   │   │   └── ie
│   │   │       └── test
│   │   │           ├── CreateHandler.java
│   │   │           ├── UpdateHandler.java
│   │   │           ├── AggregationHandler.java
│   │   │           └── Starter.java
│   │   └── resources
│   │       ├── application.properties
│   │       └── logback.xml
│   └── test
│       └── java
│           └── ie
│               └── test
│                   └── HandlerTest.java
├── target
├── .gitignore
├── pom.xml
└── README.md


Contributions

Contributions are welcome! Please follow the Contribution Guidelines.
License

This project is licensed under the MIT License.
Acknowledgments

    Vert.x Documentation
    MongoDB Documentation

Contact

For issues or inquiries, please contact Joyal Saji.

Feel free to customize this template based on the specific details of your project. Include more sections if needed, such as a Troubleshooting section or Deployment instructions. The goal is to provide enough information for users to understand and use your project effectively.


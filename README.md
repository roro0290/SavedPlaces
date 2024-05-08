# SavedPlaces

An API that provides random places to visit by utilizing the Google Places API to fetch diverse locations and MongoDB to store them.

## Features

1. **Google Places API Integration**: Learn how to use the Google Places API to form requests and process responses.
2. **RestTemplate Usage**: Utilize RestTemplate to make HTTP requests to the Google Places API.
3. **JSON Response Processing**: Learn how to use Jackson ObjectMapper to parse JSON response objects.
4. **Enum for Response Codes**: Implement an Enum for response codes and handle exceptions effectively.
5. **Configuration Management**: Store variable names using YAML configuration files for better management
6. **MongoDB Integration**: Store fetched location information in MongoDB for persistence.
7. **Random Place Retrieval**: Retrieve a random entry from MongoDB and return it as a travel destination.
8. **Mocking the Google API**: Use Wiremock to mock out Google API
9. **Testing Controller Layer**: Use MockMvc to test just the controller layer
10. **Docker Containers instead of MongoDB for Test Cases**: Use TestContainers to provide the MongoDB during test cases instead of connecting to the actual MongoDB

## Usage

To use the SavedPlaces API, follow these steps:

1. Clone the repository:

    ```bash
    git clone https://github.com/roro0290/SavedPlaces.git
    ```

2. Set up your Google API key in the application properties file.

3. Run the application.

4. Use the provided endpoints to fetch random travel destinations.

## Endpoints

- **POST /add/location**: Look up the given name using the Google Places API and store in MongoDB.
- **GET /get/random**: Get any one of the places saved in MongoDB.

## Contributing

Contributions are welcome! If you have any suggestions, improvements, or feature requests, feel free to open an issue or submit a pull request.

---

Feel free to customize and expand upon this draft to better suit your project's specifics and style preferences.

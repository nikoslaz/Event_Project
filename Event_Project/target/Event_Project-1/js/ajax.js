/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

function RegisterPOST() {
    let myForm = document.getElementById('form');
    let formData = new FormData(myForm);
    console.log("AJAX");
    
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                const responseData = JSON.parse(xhr.responseText);
                //$('#ajaxContent').html("Successful Registration. Now please log in!<br> Your Data");
                $('#ajaxContent').append(createClientTableJSON(responseData));
            } else {
                $('#ajaxContent').html('Request failed. Returned status of ' + xhr.status + "<br>");
                if (xhr.responseText) {
                    const responseData = JSON.parse(xhr.responseText);
                    for (const x in responseData) {
                        $('#ajaxContent').append("<p style='color:red'>" + x + "=" + responseData[x] + "</p>");
                    }
                } else {
                    $('#ajaxContent').append("<p>There was an error processing your request.</p>");
                }
            }
        }
    };
    
    xhr.onerror = function () {
        // Handle network errors
        alert("Network Error. Please try again.");
    };
    
    const data = {};
    formData.forEach((value, key) => (data[key] = value));
    const jsonData = JSON.stringify(data);
    console.log(jsonData);
    xhr.open('POST', 'RegistrationClient');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(jsonData);
}

function createClientTableJSON(data) {
    let tableContent = `
        <table border="1">
            <thead>
                <tr>
                    <th>Username</th>
                    <th>Password</th>
                    <th>FirstName</th>
                    <th>Last Name</th>
                    <th>Email</th>
                    <th>Phone</th>
                    <th>Balance</th>
                    <th>Card Number</th>
                    <th>Card Expiry Date</th>
                    <th>Card CVV</th>
                </tr>
            </thead>
            <tbody>
    `;

    // Loop through data to generate table rows
    data.forEach(function(client, index) {
        console.log(`Processing client ${index}:`, client); // Debugging

        tableContent += `
            <tr>
                <td>${client.client_username || 'N/A'}</td>
                <td>${client.client_password || 'N/A'}</td>
                <td>${client.client_firstname || 'N/A'}</td>
                <td>${client.client_lastname || 'N/A'}</td>
                <td>${client.client_email || 'N/A'}</td>
                <td>${client.client_phone || 'N/A'}</td>
                <td>${client.client_balance || 0}</td>
                <td>${client.card_number || 'N/A'}</td>
                <td>${client.card_expdate || 'N/A'}</td>
                <td>${client.card_cvv || 'N/A'}</td>
            </tr>
        `;
    });
    
     tableContent += `
            </tbody>
        </table>
    `;

    return tableContent;
}

function createEventTableJSON(data) {
        let tableContent = `
        <table border="1">
            <thead>
                <tr>
                    <th>Event ID</th>
                    <th>Event Name</th>
                    <th>Event Date</th>
                    <th>Event Time</th>
                    <th>Event Type</th>
                    <th>Event Capacity</th>
                    <th>Event Status</th>
                </tr>
            </thead>
            <tbody>
    `;

    data.forEach(event => {
        tableContent += `
            <tr>
                <td>${event.event_id || 'N/A'}</td>
                <td>${event.event_name || 'N/A'}</td>
                <td>${event.event_date || 'N/A'}</td>
                <td>${event.event_time || 'N/A'}</td>
                <td>${event.event_type || 'N/A'}</td>
                <td>${event.event_capacity !== null ? event.event_capacity : 'N/A'}</td>
                <td>${event.event_status || 'N/A'}</td>
            </tr>
        `;
    });

    tableContent += `
            </tbody>
        </table>
    `;

    return tableContent;
}

function loginPOST() {
    var username = document.getElementById('username_log').value;
    var password = document.getElementById('password_log').value;

    console.log("Username:", username, "Password:", password);  
    
    
    // Check for administrator credentials
    if (username === 'admin' && password === 'admin123') {
        window.location.href = 'admin.html'; // Redirect the administrator
        return; // Terminate the function
    }

    // AJAX request for regular users
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                var response = JSON.parse(xhr.responseText);
                if (response.success) {
                    // Assuming 'userId' and 'userType' are keys in your response object
                    sessionStorage.setItem('userId', response.userId);
                    sessionStorage.setItem('userType', response.userType);

                    $("#ajaxContent").html("Successful Login");
                    redirect();
                } else {
                    $("#error").html("Wrong Credentials");
                }
            } else {
                $("#error").html("Login Failed");
            }
        }
    };

    var data = $('#loginForm').serialize();
    xhr.open('POST', 'Login');
    xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    xhr.send(data);
}



function loadClients() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function() {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                const responseData = xhr.responseText;
                console.log("Response data:", responseData); // Log the response data

                try {
                    const parsedResponse = JSON.parse(responseData); // Attempt to parse as JSON
                    let tableContent = createClientTableJSON(parsedResponse, 'clients'); // Include 'petkeeper' type
                    document.getElementById('clientsContent').innerHTML = tableContent; // Update 'keepersContent' div
                } catch (error) {
                    console.error("JSON parsing error:", error); // Log JSON parsing error
                    document.getElementById('clientsContent').innerHTML = 'Invalid JSON response.';
                }
            } else {
                // Error handling for non-200 responses
                document.getElementById('clientsContent').innerHTML = 'Request failed. Returned status of ' + xhr.status;
            }
        }
    };
    xhr.onerror = function() {
        // Handle network errors
        alert("Network Error. Please try again.");
    };

    // Setting the query parameter for pet keepers
    var typeParam = "type=all";
    xhr.open('GET', 'LoadClients?' + typeParam);
    xhr.send();
}

function loadEvents() {
    var xhr = new XMLHttpRequest();


    xhr.onload = function () {
        if (xhr.status === 200) {
            console.log("Response data:", xhr.responseText)

            try {
                // Parse the JSON response
                const parsedResponse = JSON.parse(xhr.responseText);
                console.log("Parsed response:", parsedResponse);

                // Generate the table from event data
                let tableContent = createEventTableJSON(parsedResponse);

                // Update the DOM with the generated table
                document.getElementById('eventsContent').innerHTML = tableContent;

            } catch (error) {
                console.error("Error parsing JSON:", error); // Log JSON parsing error
                document.getElementById('eventsContent').innerHTML = '<p>Invalid JSON response from the server.</p>';
            }
        } else {
            console.error("Request failed with status:", xhr.status);
            document.getElementById('eventsContent').innerHTML = `<p>Failed to load events. Server responded with status ${xhr.status}.</p>`;
        }
    };

    // Define what happens in case of error
    xhr.onerror = function () {
        console.error("Network error occurred");
        document.getElementById('eventsContent').innerHTML = '<p>Network error occurred. Please try again later.</p>';
    };

    // Open a GET request to the server endpoint
    xhr.open('GET', 'LoadEvents'); 
    xhr.send(); // Send the request
}

function loadTickets() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function() {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                const responseData = xhr.responseText;
                console.log("Response data:", responseData); // Log the response data

                try {
                    const parsedResponse = JSON.parse(responseData); // Attempt to parse as JSON
                    let tableContent = createClientTableJSON(parsedResponse, 'ticket'); // Include 'petkeeper' type
                    document.getElementById('ticketsContent').innerHTML = tableContent; // Update 'keepersContent' div
                } catch (error) {
                    console.error("JSON parsing error:", error); // Log JSON parsing error
                    document.getElementById('ticketsContent').innerHTML = 'Invalid JSON response.';
                }
            } else {
                // Error handling for non-200 responses
                document.getElementById('ticketsContent').innerHTML = 'Request failed. Returned status of ' + xhr.status;
            }
        }
    };
    xhr.onerror = function() {
        // Handle network errors
        alert("Network Error. Please try again.");
    };

    // Setting the query parameter for pet keepers
    var typeParam = "type=all";
    xhr.open('GET', 'AdminTickets?' + typeParam);
    xhr.send();
}

function loadReservations() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function() {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                const responseData = xhr.responseText;
                console.log("Response data:", responseData); // Log the response data

                try {
                    const parsedResponse = JSON.parse(responseData); // Attempt to parse as JSON
                    let tableContent = createClientTableJSON(parsedResponse, 'reservation'); // Include 'petkeeper' type
                    document.getElementById('reservationsContent').innerHTML = tableContent; // Update 'keepersContent' div
                } catch (error) {
                    console.error("JSON parsing error:", error); // Log JSON parsing error
                    document.getElementById('reservationsContent').innerHTML = 'Invalid JSON response.';
                }
            } else {
                // Error handling for non-200 responses
                document.getElementById('reservationsContent').innerHTML = 'Request failed. Returned status of ' + xhr.status;
            }
        }
    };
    xhr.onerror = function() {
        // Handle network errors
        alert("Network Error. Please try again.");
    };

    // Setting the query parameter for pet keepers
    var typeParam = "type=all";
    xhr.open('GET', 'AdminReservations?' + typeParam);
    xhr.send();
}

// Show the Add Event Form
function addEvent() {
    document.getElementById('addEventForm').classList.remove('hidden');
}

// Hide the Add Event Form
function hideEventForm() {
    document.getElementById('addEventForm').classList.add('hidden');
    clearEventForm(); // Optional: Clear form fields when hiding the form
}

// Clear the Event Form Fields
function clearEventForm() {
    document.getElementById('event_id').value = '';
    document.getElementById('event_name').value = '';
    document.getElementById('event_date').value = '';
    document.getElementById('event_time').value = '';
    document.getElementById('event_type').value = '';
    document.getElementById('event_capacity').value = '';
}

function submitEventForm() {
    let myForm = document.getElementById('eventForm');
    let formData = new FormData(myForm);
    console.log("AJAX");
    
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                const responseData = JSON.parse(xhr.responseText);
                $('#ajaxContent').append(createEventTableJSON(responseData));
            } else {
                $('#ajaxContent').html('Request failed. Returned status of ' + xhr.status + "<br>");
                if (xhr.responseText) {
                    const responseData = JSON.parse(xhr.responseText);
                    for (const x in responseData) {
                        $('#ajaxContent').append("<p style='color:red'>" + x + "=" + responseData[x] + "</p>");
                    }
                } else {
                    $('#ajaxContent').append("<p>There was an error processing your request.</p>");
                }
            }
        }
    };
    
    xhr.onerror = function () {
        // Handle network errors
        alert("Network Error. Please try again.");
    };
    
    const data = {};
    formData.forEach((value, key) => (data[key] = value));
    const jsonData = JSON.stringify(data);
    console.log(jsonData);
    xhr.open('POST', 'AddEvent');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(jsonData);
}

function createTables() {
    const xhr = new XMLHttpRequest();
    xhr.open("POST", "CreateTables", true);
    xhr.setRequestHeader("Content-Type", "application/json");
    console.log("Tables created!");
    xhr.send();
}

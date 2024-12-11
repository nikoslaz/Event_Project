let globalUsername;
let globalID;

//=================================================================================================
// Register and Login

function RegisterPOST() {
    let myForm = document.getElementById('form');
    let formData = new FormData(myForm);
    console.log("AJAX");
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                const responseData = JSON.parse(xhr.responseText);
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
    xhr.onerror = function () { alert("Network Error. Please try again."); };
    const data = {};
    formData.forEach((value, key) => (data[key] = value));
    const jsonData = JSON.stringify(data);
    console.log(jsonData);
    xhr.open('POST', 'RegistrationClient');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(jsonData);
}

function loginPOST() {
    let username = document.getElementById('username_log').value;
    var password = document.getElementById('password_log').value;
    
    if (username === 'admin' && password === 'admin123') {
        window.location.href = 'admin.html';
        return;
    }
    const xhr = new XMLHttpRequest();
    xhr.open('POST', 'Login', true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                var response = JSON.parse(xhr.responseText);
                if (response.success) {
                    sessionStorage.setItem('userType', 'client');
                    sessionStorage.setItem('globalUsername', username);
                    window.location.href = 'client.html';
                } else {
                    console.log("Wrong Credentials");
                }
            } else {
                console.log("Login Failed");
            }
        }
    };
    const data = `username=${encodeURIComponent(username)}&password=${encodeURIComponent(password)}`;
    console.log("Sending request with data:", data);
    xhr.send(data);
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
    xhr.onerror = function () { alert("Network Error. Please try again."); };
    const data = {};
    formData.forEach((value, key) => (data[key] = value));
    const jsonData = JSON.stringify(data);
    console.log(jsonData);
    xhr.open('POST', 'AddEvent');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(jsonData);
}

//=================================================================================================
// Create Tables for Admin

function createTables() {
    const xhr = new XMLHttpRequest();
    xhr.open("POST", "CreateTables", true);
    xhr.setRequestHeader("Content-Type", "application/json");
    console.log("Creating Tables...");
    xhr.send();
}

function refreshTables() {
    loadClients();
    loadEvents();
    loadTickets();
    loadReservations();
}

function loadClients() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function() {
        if (xhr.status === 200) {
            console.log("Response data:", xhr.responseText);
            try {
                const parsedResponse = JSON.parse(xhr.responseText);
                console.log("Parsed response:", parsedResponse);
                let tableContent = createClientTableJSON(parsedResponse);
                document.getElementById('clientsContent').innerHTML = tableContent;
            } catch (error) {
                console.error("JSON parsing error:", error);
                document.getElementById('clientsContent').innerHTML = '<p>Invalid JSON response.</p>';
            }
        } else {
            console.error("Request failed with status:", xhr.status);
            document.getElementById('clientsContent').innerHTML = 'Request failed. Returned status of ' + xhr.status;
        }
    };
    xhr.onerror = function() { alert("Network Error. Please try again."); };
    xhr.open('GET', 'LoadClients');
    xhr.send();
}

function loadEvents() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.status === 200) {
            console.log("Response data:", xhr.responseText);
            try {
                const parsedResponse = JSON.parse(xhr.responseText);
                console.log("Parsed response:", parsedResponse);
                let tableContent = createEventTableJSON(parsedResponse);
                document.getElementById('eventsContent').innerHTML = tableContent;
            } catch (error) {
                console.error("Error parsing JSON:", error);
                document.getElementById('eventsContent').innerHTML = '<p>Invalid JSON response.</p>';
            }
        } else {
            console.error("Request failed with status:", xhr.status);
            document.getElementById('eventsContent').innerHTML = 'Request failed. Returned status of ' + xhr.status;
        }
    };
    xhr.onerror = function () { alert("Network Error. Please try again."); };
    xhr.open('GET', 'LoadEvents'); 
    xhr.send();
}

function loadTickets() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.status === 200) {
            console.log("Response data:", xhr.responseText);
            try {
                const parsedResponse = JSON.parse(xhr.responseText);
                console.log("Parsed response:", parsedResponse);
                let tableContent = createTicketTableJSON(parsedResponse);
                document.getElementById('ticketsContent').innerHTML = tableContent;
            } catch (error) {
                console.error("Error parsing JSON:", error);
                document.getElementById('ticketsContent').innerHTML = '<p>Invalid JSON response.</p>';
            }
        } else {
            console.error("Request failed with status:", xhr.status);
            document.getElementById('ticketsContent').innerHTML = 'Request failed. Returned status of ' + xhr.status;
        }
    };
    xhr.onerror = function () { alert("Network Error. Please try again."); };
    xhr.open('GET', 'LoadTickets');
    xhr.send();
}

function loadReservations() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.status === 200) {
            console.log("Response data:", xhr.responseText);
            try {
                const parsedResponse = JSON.parse(xhr.responseText);
                console.log("Parsed response:", parsedResponse);
                let tableContent = createReservationTableJSON(parsedResponse);
                document.getElementById('reservationsContent').innerHTML = tableContent;
            } catch (error) {
                console.error("Error parsing JSON:", error);
                document.getElementById('reservationsContent').innerHTML = '<p>Invalid JSON response.</p>';
            }
        } else {
            console.error("Request failed with status:", xhr.status);
            document.getElementById('reservationsContent').innerHTML = 'Request failed. Returned status of ' + xhr.status;
        }
    };
    xhr.onerror = function () { alert("Network Error. Please try again."); };
    xhr.open('GET', 'LoadReservations');
    xhr.send();
}

//=================================================================================================
// JSON Tables

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
    data.forEach(client => {
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
                <td>${event.event_capacity || 'N/A'}</td>
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

function createTicketTableJSON(data) {
        let tableContent = `
        <table border="1">
            <thead>
                <tr>
                    <th>Ticket ID</th>
                    <th>Ticket Type</th>
                    <th>Ticket Price</th>
                    <th>Ticket Availabilty</th>
                    <th>Event ID</th>
                    <th>Reservation ID</th>
                </tr>
            </thead>
            <tbody>
    `;
    data.forEach(ticket => {
        tableContent += `
            <tr>
                <td>${ticket.ticket_id || 'N/A'}</td>
                <td>${ticket.ticket_type || 'N/A'}</td>
                <td>${ticket.ticket_price || 'N/A'}</td>
                <td>${ticket.ticket_availability === 1 ? 'Available' : ticket.ticket_availability === 0 ? 'Not Available' : 'N/A'}</td>
                <td>${ticket.event_id || 'N/A'}</td>
                <td>${ticket.reservation_id || 'N/A'}</td>
            </tr>
        `;
    });
    tableContent += `
            </tbody>
        </table>
    `;
    return tableContent;
}

function createReservationTableJSON(data) {
        let tableContent = `
        <table border="1">
            <thead>
                <tr>
                    <th>Reservation ID</th>
                    <th>Reservation Tickets</th>
                    <th>Reservation Date</th>
                    <th>Reservation Payment Amount</th>
                    <th>Reservation Status</th>
                    <th>Client Username</th>
                    <th>Event ID</th>
                </tr>
            </thead>
            <tbody>
    `;
    data.forEach(reservations => {
        tableContent += `
            <tr>
                <td>${reservations.reservation_id || 'N/A'}</td>
                <td>${reservations.reservation_tickets || 'N/A'}</td>
                <td>${reservations.reservation_date || 'N/A'}</td>
                <td>${reservations.reservation_payment_amount || 'N/A'}</td>
                <td>${reservations.reservation_status || 'N/A'}</td>
                <td>${reservations.client_username || 'N/A'}</td>
                <td>${reservations.event_id || 'N/A'}</td>
            </tr>
        `;
    });
    tableContent += `
            </tbody>
        </table>
    `;
    return tableContent;
}

//=================================================================================================
// Client Functions

function refreshClientEvents() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.status === 200) {
            console.log("Response data:", xhr.responseText)
            try {
                const parsedResponse = JSON.parse(xhr.responseText);
                console.log("Parsed response:", parsedResponse);
                let tableContent = loadClientEventTableJSON(parsedResponse);
                document.getElementById('eventsContent').innerHTML = tableContent;
            } catch (error) {
                console.error("Error parsing JSON:", error);
                document.getElementById('eventsContent').innerHTML = '<p>Invalid JSON response.</p>';
            }
        } else {
            console.error("Request failed with status:", xhr.status);
            document.getElementById('eventsContent').innerHTML = 'Request failed. Returned status of ' + xhr.status;
        }
    };
    xhr.onerror = function () { alert("Network Error. Please try again."); };
    xhr.open('GET', 'LoadClientEvents'); 
    xhr.send();
}

function loadClientEventTableJSON(data) {
    let tableContent = `
        <table border="1">
            <thead>
                <tr>
                    <th>Select</th>
                    <th>Event ID</th>
                    <th>Event Name</th>
                    <th>Event Date</th>
                    <th>Event Time</th>
                    <th>Event Type</th>
                </tr>
            </thead>
            <tbody>
    `;
    data.forEach(event => {
        tableContent += `
            <tr>
                <td><button onclick="selectRegularEventTickets('${event.event_id}')">Select</button></td>
                <td>${event.event_id || 'N/A'}</td>
                <td>${event.event_name || 'N/A'}</td>
                <td>${event.event_date || 'N/A'}</td>
                <td>${event.event_time || 'N/A'}</td>
                <td>${event.event_type || 'N/A'}</td>
            </tr>
        `;
    });
    tableContent +=     `
            </tbody>
        </table>
    `;
    return tableContent;
}

function submitTickets(){
    const regular = parseInt(document.getElementById('regularTickets').value) || 0;
    const vip = parseInt(document.getElementById('vipTickets').value) || 0;
    const balcony = parseInt(document.getElementById('balconyTickets').value) || 0;
    const globalUsername = sessionStorage.getItem('globalUsername'); 
    const globalEventID = sessionStorage.getItem('globalID');
    
    console.log(`Regular Tickets: ${regular}, VIP Tickets: ${vip}, Balcony Tickets: ${balcony}, Username: ${globalUsername}, Event ID: ${globalEventID} `);
    
    const xhr = new XMLHttpRequest();
    xhr.open('POST', 'UpdateTickets', true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    
    const ticketData = JSON.stringify({
        regularTickets: regular,
        vipTickets: vip,
        balconyTickets: balcony,
        clientUsername: globalUsername,
        eventID:globalEventID
    });
    
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                const response = JSON.parse(xhr.responseText);
                console.log('Response from servlet:', response);
            } else {
                console.error('Error:', xhr.responseText);
            }
        }
    };
    
    xhr.send(ticketData);
}

function selectRegularEventTickets(id) {
    console.log(`Event ID: ${id}`);
    sessionStorage.setItem('globalID', id);
    const xhr = new XMLHttpRequest();
    xhr.open('POST', 'CountEventTickets', true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    const eventData = JSON.stringify({
        eventId: id
    });
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                const response = JSON.parse(xhr.responseText);
                console.log('Response from servlet:', response);
                document.getElementById('showTicketCount').innerHTML = '<h2>Available Tickets for Event '+id+'<br>Regular:'+response.regular+' Balcony:'+response.balcony+' VIP:'+response.vip+' </h2>';
            } else {
                console.error('Error:', xhr.responseText);
            }
        }
    };
    xhr.send(eventData);
}

//=================================================================================================
// Cancel Event 

function cancelEventForm(){
    const eventId = document.getElementById('event_id').value;

    if (!eventId) {
        console.log("No event id");
        return;
    }
    
    const xhr = new XMLHttpRequest();
    xhr.open('POST', 'CancelEvent', true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    
    const data = JSON.stringify({
        eventID:eventId 
    });
    
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                const response = JSON.parse(xhr.responseText);
                console.log('Response from servlet:', response);
            } else {
                console.error('Error:', xhr.responseText);
            }
        }
    };
    
    xhr.send(data);
}

//=================================================================================================
// Cancel Reservation

function cancelReserv(){
    const id = document.getElementById('cancelRes').value;
    console.log(`Reservation ID: ${id}`);
    const xhr = new XMLHttpRequest();
    xhr.open('POST', 'CancelReservation', true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    const data = JSON.stringify({
        reservation_ID:id
    });
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                const response = JSON.parse(xhr.responseText);
                console.log('Response from servlet:', response);
            } else {
                console.error('Error:', xhr.responseText);
            }
        }
    };
    xhr.send(data);
}

//=================================================================================================
// Cancel Reservation

function showProfits() {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', 'ShowEventsProfit', true);
    xhr.setRequestHeader('Content-Type', 'application/json');

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                const response = JSON.parse(xhr.responseText);
                console.log('Response from servlet:', response);

                displayProfits(response);
            } else {
                console.error('Error:', xhr.responseText);
            }
        }
    };

    xhr.send();
}

function displayProfits(profits) {
    // Create a container element
    const container = document.getElementById('profits-container');
    container.innerHTML = ''; // Clear previous content

    // Create a table for the profits
    const table = document.createElement('table');
    table.style.width = '100%';
    table.style.borderCollapse = 'collapse';

    // Add table header
    const header = table.createTHead();
    const headerRow = header.insertRow(0);
    const headerCell1 = headerRow.insertCell(0);
    const headerCell2 = headerRow.insertCell(1);

    headerCell1.textContent = 'Event ID';
    headerCell2.textContent = 'Total Payment Amount';

    headerCell1.style.border = '1px solid #ddd';
    headerCell1.style.padding = '8px';
    headerCell2.style.border = '1px solid #ddd';
    headerCell2.style.padding = '8px';

    // Add table body
    const tbody = table.createTBody();

    // Populate table rows with profits
    profits.forEach(profit => {
        const row = tbody.insertRow();
        const cell1 = row.insertCell(0);
        const cell2 = row.insertCell(1);

        cell1.textContent = profit.event_id;
        cell2.textContent = profit.total_payment_amount;

        cell1.style.border = '1px solid #ddd';
        cell1.style.padding = '8px';
        cell2.style.border = '1px solid #ddd';
        cell2.style.padding = '8px';
    });

    // Append table to the container
    container.appendChild(table);
}

//=================================================================================================
// Most popular event

function showMostPopularEvent() {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', 'PopularEvent', true);
    xhr.setRequestHeader('Content-Type', 'application/json');

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                const response = JSON.parse(xhr.responseText);
                console.log('Response from servlet:', response);

                displayMostPopularEvent(response);
            } else {
                console.error('Error:', xhr.responseText);
            }
        }
    };

    xhr.send();
}

// Function to display the most popular event
function displayMostPopularEvent(eventData) {
    const container = document.getElementById('most-popular-event-container');
    container.innerHTML = ''; // Clear previous content

    if (eventData.event_id) {
        // If a popular event is found
        const heading = document.createElement('h2');
        heading.textContent = 'Most Popular Event';

        const eventDetails = document.createElement('p');
        eventDetails.textContent = `Event ID: ${eventData.event_id}, Reservations: ${eventData.reservation_count}`;

        container.appendChild(heading);
        container.appendChild(eventDetails);
    } else if (eventData.message) {
        // If no active reservations found
        const noEventMessage = document.createElement('p');
        noEventMessage.textContent = eventData.message;
        container.appendChild(noEventMessage);
    } else {
        // Handle unexpected response
        const errorMessage = document.createElement('p');
        errorMessage.textContent = 'An error occurred while fetching the most popular event.';
        container.appendChild(errorMessage);
    }
}

//=================================================================================================
// Show VIP profit

function showtotalProfitVIP() {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', 'ProfitVIP', true); // URL to the servlet
    xhr.setRequestHeader('Content-Type', 'application/json');

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                const response = JSON.parse(xhr.responseText);
                console.log('Total VIP Profit:', response.total_payment_amount);

                // Display the total profit in the webpage
                displayVIPProfit(response.total_payment_amount);
            } else {
                console.error('Error fetching VIP profit:', xhr.responseText);
            }
        }
    };

    xhr.send();
}

// Function to display the total profit on the webpage
function displayVIPProfit(totalProfit) {
    const container = document.getElementById('vip-profit-container');
    container.innerHTML = ''; // Clear previous content

    const heading = document.createElement('h2');
    heading.textContent = 'Total Profit from VIP Tickets';

    const profitText = document.createElement('p');
    profitText.textContent = `$${totalProfit.toFixed(2)}`; // Format as currency

    container.appendChild(heading);
    container.appendChild(profitText);
}   
//=================================================================================================
// Show General profit

function showtotalProfitRegular() {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', 'ProfitRegular', true); // URL to the servlet for Regular tickets
    xhr.setRequestHeader('Content-Type', 'application/json');

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                const response = JSON.parse(xhr.responseText);
                console.log('Total Regular Profit:', response.total_payment_amount);

                // Display the total profit in the webpage
                displayRegularProfit(response.total_payment_amount);
            } else {
                console.error('Error fetching Regular profit:', xhr.responseText);
            }
        }
    };

    xhr.send();
}

function displayRegularProfit(totalProfit) {
    const container = document.getElementById('regular-profit-container');
    container.innerHTML = ''; // Clear previous content

    const heading = document.createElement('h2');
    heading.textContent = 'Total Profit from Regular Tickets';

    const profitText = document.createElement('p');
    profitText.textContent = `$${totalProfit.toFixed(2)}`; // Format as currency

    container.appendChild(heading);
    container.appendChild(profitText);
}

//=================================================================================================
// Show Tickets profit

function showtotalProfitTickets() {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', 'ProfitTickets', true); // URL to the servlet for ticket profits
    xhr.setRequestHeader('Content-Type', 'application/json');

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                const response = JSON.parse(xhr.responseText);
                console.log('Total Ticket Profits:', response);

                // Display the profits in the webpage
                displayTicketProfits(response);
            } else {
                console.error('Error fetching ticket profits:', xhr.responseText);
            }
        }
    };

    xhr.send();
}

function displayTicketProfits(ticketProfits) {
    const container = document.getElementById('ticket-profit-container');
    container.innerHTML = ''; // Clear previous content

    const heading = document.createElement('h2');
    heading.textContent = 'Total Ticket Profits';

    container.appendChild(heading);

    const totalPayment = document.createElement('p');
    totalPayment.textContent = `Total Profit: $${ticketProfits.total_payment_amount.toFixed(2)}`;

    container.appendChild(totalPayment);
}

//=================================================================================================
// Show Tickets profit

function showtotalProfitBalcony() {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', 'ProfitBalcony', true); // URL to the servlet for Balcony tickets
    xhr.setRequestHeader('Content-Type', 'application/json');

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                const response = JSON.parse(xhr.responseText);
                console.log('Total Balcony Profit:', response.total_payment_amount);

                // Display the total profit in the webpage
                displayBalconyProfit(response.total_payment_amount);
            } else {
                console.error('Error fetching Balcony profit:', xhr.responseText);
            }
        }
    };

    xhr.send();
}

function displayBalconyProfit(totalProfit) {
    const container = document.getElementById('balcony-profit-container');
    container.innerHTML = ''; // Clear previous content

    const heading = document.createElement('h2');
    heading.textContent = 'Total Profit from Balcony Tickets';

    const profitText = document.createElement('p');
    profitText.textContent = `$${totalProfit.toFixed(2)}`; // Format as currency

    container.appendChild(heading);
    container.appendChild(profitText);
}

//=================================================================================================
// Show reservations in a time period 

function showReservationsbyTimePeriod() {
    // Get the start and end dates from the input fields
    const startDate = document.getElementById('start-date').value;
    const endDate = document.getElementById('end-date').value;

    if (!startDate || !endDate) {
        return;
    }

    const xhr = new XMLHttpRequest();
    xhr.open('GET', `EventTimePeriod?start=${startDate}&end=${endDate}`, true);
    xhr.setRequestHeader('Content-Type', 'application/json');

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                const response = JSON.parse(xhr.responseText);
                console.log('Reservations:', response);

                // Display reservations on the webpage
                displayReservationTable(response);
            } else {
                console.error('Error fetching reservations:', xhr.responseText);
            }
        }
    };

    xhr.send();
}

function displayReservationTable(data) {
    const container = document.getElementById('reservation-table-container'); // Ensure this container exists in your HTML
    container.innerHTML = ''; // Clear previous content

    if (!data || data.length === 0) {
        container.innerHTML = '<p>No reservations found.</p>';
        return;
    }

    let tableContent = `
        <table border="1" style="width: 100%; border-collapse: collapse; text-align: left;">
            <thead>
                <tr style="background-color: #f2f2f2;">
                    <th style="padding: 8px; border: 1px solid #ddd;">Reservation ID</th>
                    <th style="padding: 8px; border: 1px solid #ddd;">Reservation Date</th>
                    <th style="padding: 8px; border: 1px solid #ddd;">Reservation Payment Amount</th>
                    <th style="padding: 8px; border: 1px solid #ddd;">Client Username</th>
                    <th style="padding: 8px; border: 1px solid #ddd;">Event ID</th>
                </tr>
            </thead>
            <tbody>
    `;
    data.forEach(reservation => {
        tableContent += `
            <tr>
                <td style="padding: 8px; border: 1px solid #ddd;">${reservation.reservation_id || 'N/A'}</td>
                <td style="padding: 8px; border: 1px solid #ddd;">${reservation.reservation_date || 'N/A'}</td>
                <td style="padding: 8px; border: 1px solid #ddd;">$${reservation.reservation_payment_amount ? reservation.reservation_payment_amount.toFixed(2) : 'N/A'}</td>
                <td style="padding: 8px; border: 1px solid #ddd;">${reservation.client_username || 'N/A'}</td>
                <td style="padding: 8px; border: 1px solid #ddd;">${reservation.event_id || 'N/A'}</td>
            </tr>
        `;
    });
    tableContent += `
            </tbody>
        </table>
    `;

    container.innerHTML = tableContent;
}

//=================================================================================================
// Show profit in a time period 

function showProfitbyTimePeriod() {
    // Get the start and end dates from the input fields
    const startDate = document.getElementById('start-date-profit').value;
    const endDate = document.getElementById('end-date-profit').value;

    if (!startDate || !endDate) {
        return;
    }

    const xhr = new XMLHttpRequest();
    xhr.open('GET', `ProfitTimePeriod?start=${startDate}&end=${endDate}`, true);
    xhr.setRequestHeader('Content-Type', 'application/json');

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                const response = JSON.parse(xhr.responseText);
                console.log('Reservations:', response);

                // Display reservations on the webpage
                displayProfitTable(response);
            } else {
                console.error('Error fetching reservations:', xhr.responseText);
            }
        }
    };

    xhr.send();
}

function displayProfitTable(data) {
    const container = document.getElementById('profit-time-container'); // Ensure this container exists in your HTML
    container.innerHTML = ''; // Clear previous content

    if (!data || data.length === 0) {
        container.innerHTML = '<p>No reservations found.</p>';
        return;
    }

    let tableContent = `
        <table border="1" style="width: 100%; border-collapse: collapse; text-align: left;">
            <thead>
                <tr style="background-color: #f2f2f2;">
                    <th style="padding: 8px; border: 1px solid #ddd;">Event ID</th>
                    <th style="padding: 8px; border: 1px solid #ddd;">Profit Date</th>
                </tr>
            </thead>
            <tbody>
    `;
    data.forEach(event => {
        tableContent += `
            <tr>
                <td style="padding: 8px; border: 1px solid #ddd;">${event.event_id || 'N/A'}</td>
                <td style="padding: 8px; border: 1px solid #ddd;">${event.total_profit || 'N/A'}</td>
            </tr>
        `;
    });
    tableContent += `
            </tbody>
        </table>
    `;

    container.innerHTML = tableContent;
}

//=================================================================================================
// Show clients reservations

function loadClientReservations(){
    const globalUsername = sessionStorage.getItem('globalUsername');
    
    const url = `LoadClientReservations?username=${encodeURIComponent(globalUsername)}`;
    const xhr = new XMLHttpRequest();
    xhr.open('GET', url, true);

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                const response = JSON.parse(xhr.responseText);
                console.log('Reservations:', response);

                displayClientReservations(response);
            } else {
                console.error('Error:', xhr.responseText);
            }
        }
    };

    xhr.send();
}

function displayClientReservations(data) {
    const reservationsContainer = document.getElementById('reservationsClientContainer');
    
    let tableContent = `
        <table border="1">
            <thead>
                <tr>
                    <th>Reservation ID</th>
                    <th>Event ID</th>
                    <th>Reservation Tickets</th>
                    <th>Reservation Date</th>
                    <th>Reservation Payment Amount</th>
                </tr>
            </thead>
            <tbody>
    `;
    data.forEach(reservation => {
        tableContent += `
            <tr>
                <td>${reservation.reservation_id || 'N/A'}</td>
                <td>${reservation.event_id || 'N/A'}</td>
                <td>${reservation.reservation_tickets || 'N/A'}</td>
                <td>${reservation.reservation_date || 'N/A'}</td>
                <td>${reservation.reservation_payment_amount || 'N/A'}</td>
            </tr>
        `;
    });
    tableContent += `
            </tbody>
        </table>
    `;

    // Set the generated HTML table to the container
    reservationsContainer.innerHTML = tableContent;
}
/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

let globalUsername;
let globalID;

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
    console.log("Username:", username, "Password:", password);  
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
                <td>${reservations.status || 'N/A'}</td>
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
        if (xhr.readyState === 4 && xhr.status === 200) {
            console.log('Response from servlet:', xhr.responseText);
        } else {
            console.error('Error:', xhr.responseText);
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

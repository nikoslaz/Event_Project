/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

function RegisterPOST() {
    let myForm = document.getElementById('form');
    let formData = new FormData(myForm);
    
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                const responseData = JSON.parse(xhr.responseText);
                $('#ajaxContent').html("Successful Registration. Now please log in!<br> Your Data");
                $('#ajaxContent').append(createTableFromJSON(responseData));
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


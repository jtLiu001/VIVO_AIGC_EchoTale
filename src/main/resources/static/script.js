
document.addEventListener('DOMContentLoaded', function() {
    console.log('JavaScript file loaded successfully');
    const form = document.getElementById('messageForm');
    form.addEventListener('submit', function(event) {
        event.preventDefault();
        const message = document.getElementById('message').value;
        const jsonData = JSON.stringify({ message: message });

        fetch('/submit', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: jsonData
        })
        .then(response => response.text())
        .then(data => {
            document.getElementById('reply').innerText = data;
        })
        .catch(error => console.error('Error:', error));
    });
});

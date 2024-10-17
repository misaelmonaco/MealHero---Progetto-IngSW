document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('restaurantSearchForm');

    form.addEventListener('submit', function(event) {
        event.preventDefault(); // Questo serve a prevenire il comportamento di default di submit

        const restaurantName = document.getElementById('restaurantName').value.trim();
        const location = document.getElementById('location').value.trim();

        // Qui si costruisce l'URL del fetch in modo condizionale
        let url = '/restaurant/find';
        if (restaurantName) {
            url += `?name=${encodeURIComponent(restaurantName)}`;
        }
        if (location) {
            url += `${restaurantName ? '&' : '?'}location=${encodeURIComponent(location)}`;
        }

        fetch(url)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.text();
            })
            .then(html => {
                const resultsContainer = document.getElementById('resultsContainer');
                resultsContainer.innerHTML = html;
            })
            .catch(error => {
                console.error('There has been a problem with your fetch operation:', error);
            });
    });
});

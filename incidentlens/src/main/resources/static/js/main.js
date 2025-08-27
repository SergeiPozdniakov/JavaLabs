document.addEventListener('DOMContentLoaded', function() {
    loadWorks();

    function loadWorks() {
        axios.get('/api/works')
            .then(response => {
                const works = response.data;
                const worksList = document.getElementById('works-list');
                worksList.innerHTML = '';

                if (works.length === 0) {
                    worksList.innerHTML = `
                        <tr>
                            <td colspan="3" class="text-center py-4">
                                <p>No charts created yet. Click "New Chart" to get started!</p>
                            </td>
                        </tr>
                    `;
                    return;
                }

                works.forEach((work, index) => {
                    const date = new Date(work.registrationDate);
                    const formattedDate = date.toLocaleDateString('en-US', {
                        year: 'numeric',
                        month: 'short',
                        day: 'numeric',
                        hour: '2-digit',
                        minute: '2-digit'
                    });

                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${index + 1}</td>
                        <td><a href="/editor/${work.id}" class="text-decoration-none">${work.title}</a></td>
                        <td>${formattedDate}</td>
                    `;
                    worksList.appendChild(row);
                });
            })
            .catch(error => {
                console.error('Error loading works:', error);
                document.getElementById('works-list').innerHTML = `
                    <tr>
                        <td colspan="3" class="text-center py-4 text-danger">
                            Error loading charts. Please try again later.
                        </td>
                    </tr>
                `;
            });
    }
});
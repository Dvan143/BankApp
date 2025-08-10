fetch('/bankapp/news/latest')
    .then(resp => resp.json())
    .then(fetchedNews => {
        const newsDiv = document.getElementById('newsDiv');
        const newsList = document.createElement('ul');

        fetchedNews.forEach(news => {
            const currentNews = document.createElement('li');
            currentNews.innerHTML = `
              <h3>${news.title}</h3>
              <div>${news.content}</div>
            `;
            newsList.appendChild(currentNews);
        });

        newsDiv.appendChild(newsList);
    })
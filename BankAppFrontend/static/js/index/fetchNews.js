// TODO
const newsForm = document.getElementById('news-form');
fetch('/api/news/latest')
    .then(resp => resp.json())
        .then(resp => {

        })
document.addEventListener('DOMContentLoaded', function() {
    const articlesDiv = document.getElementById('articles');
    const refreshButton = document.getElementById('refresh');

    // Fetch and display news articles
    async function fetchNews() {
        const feeds = [
            "https://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml",
            "https://feeds.bbci.co.uk/news/world/rss.xml",
            "https://www.theguardian.com/world/rss"
        ];

        articlesDiv.innerHTML = 'Loading news...';

        const allArticles = [];
        for (const feed of feeds) {
            const response = await fetch(`https://api.rss2json.com/v1/api.json?rss_url=${encodeURIComponent(feed)}`);
            const data = await response.json();
            allArticles.push(...data.items);
        }

        displayArticles(allArticles);
    }

    // Display articles in the popup
    function displayArticles(articles) {
        articlesDiv.innerHTML = '';
        articles.slice(0, 5).forEach(article => {
            const articleDiv = document.createElement('div');
            articleDiv.classList.add('article');
            articleDiv.innerHTML = `
                <div class="title">${article.title}</div>
                <div class="summary">${article.description}</div>
                <a href="${article.link}" class="link" target="_blank">Read more</a>
            `;
            articlesDiv.appendChild(articleDiv);
        });
    }

    // Refresh button event listener
    refreshButton.addEventListener('click', fetchNews);

    // Fetch news on popup load
    fetchNews();
});

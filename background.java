chrome.alarms.create('fetchNews', { periodInMinutes: 30 });  // Fetch every 30 minutes

chrome.alarms.onAlarm.addListener((alarm) => {
    if (alarm.name === 'fetchNews') {
        fetchNewsAndNotify();
    }
});

async function fetchNewsAndNotify() {
    const feeds = [
        "https://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml",
        "https://feeds.bbci.co.uk/news/world/rss.xml",
        "https://www.theguardian.com/world/rss"
    ];

    const allArticles = [];
    for (const feed of feeds) {
        const response = await fetch(`https://api.rss2json.com/v1/api.json?rss_url=${encodeURIComponent(feed)}`);
        const data = await response.json();
        allArticles.push(...data.items);
    }

    // Show notification
    chrome.notifications.create('newsUpdate', {
        type: 'basic',
        iconUrl: 'icons/icon48.png',
        title: 'News Update',
        message: 'New top articles are available!',
        priority: 2
    });

    chrome.notifications.onClicked.addListener(() => {
        chrome.action.openPopup();  // Open popup with the latest news
    });
}

require('dotenv').config();
const express = require('express');
const mongoose = require('mongoose');
const axios = require('axios');
const path = require('path');

const app = express();
const port = process.env.PORT || 3000;

// Connect to MongoDB using Mongoose

const { MongoClient, ServerApiVersion } = require('mongodb');
const uri = "mongodb+srv://finleytupman:<db_password>@cluster0.mxmxx.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";

// Create a MongoClient with a MongoClientOptions object to set the Stable API version
const client = new MongoClient(uri, {
  serverApi: {
    version: ServerApiVersion.v1,
    strict: true,
    deprecationErrors: true,
  }
});

async function run() {
  try {
    // Connect the client to the server	(optional starting in v4.7)
    await client.connect();
    // Send a ping to confirm a successful connection
    await client.db("admin").command({ ping: 1 });
    console.log("Pinged your deployment. You successfully connected to MongoDB!");
  } finally {
    // Ensures that the client will close when you finish/error
    await client.close();
  }
}
run().catch(console.dir);


// Define Video model
const Video = mongoose.model('Video', {
    title: String,
    content: String,
    video_url: String,
    created_at: { type: Date, default: Date.now }
});

app.use(express.static('public'));

app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, 'public', 'index.html'));
});

app.get('/api/fetch-news', async (req, res) => {
    const page = parseInt(req.query.page) || 1;
    const api_key = process.env.NEWS_API_KEY;
    const url = `https://newsapi.org/v2/top-headlines?country=us&apiKey=${api_key}&page=${page}&pageSize=5`;

    try {
        const response = await axios.get(url);
        const articles = response.data.articles;

        const videos = await Promise.all(articles.map(generateVideo));

        res.json({ videos });
    } catch (error) {
        console.error('Error fetching news:', error);
        res.status(500).json({ error: error.message });
    }
});

async function generateVideo(article) {
    // Placeholder for video generation logic
    const video = new Video({
        title: article.title,
        content: article.content || '',
        video_url: '/api/placeholder/400/600' // placeholder
    });
    await video.save();

    return {
        id: video._id,
        title: video.title,
        src: video.video_url
    };
}

app.listen(port, () => {
    console.log(`Server running at http://localhost:${port}`);
});

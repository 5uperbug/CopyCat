// server.js

// BASE SETUP
// =============================================================================

// call the packages we need
var express    = require('express');        // call express
var app        = express();                 // define our app using express
var bodyParser = require('body-parser');
var Datastore = require('nedb');
var db = new Datastore({
        filename: 'datastore/data.db',
        autoload : true
    });

// configure app to use bodyParser()
// this will let us get the data from a POST
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

var port = process.env.PORT || 8080;        // set our port

// ROUTES FOR OUR API
// =============================================================================
var router = express.Router();              // get an instance of the express Router

// middleware to use for all requests
router.use(function(req, res, next) {
    // do logging
    console.log('Something is happening.');
    next(); // make sure we go to the next routes and don't stop here
});

// test route to make sure everything is working (accessed at GET http://localhost:8080/api)
router.get('/', function(req, res) {
    res.json({ message: 'Greetings earthlings!' });
});

// more routes for our API will happen here

// routes that end with /clips
// ----------------------------------------------------
router.route('/clips')
    // create a clip (accessed at POST http://localhost:8080/api/clip)
    .post(function(req, res) {
        var clip = new Object();
        clip.content = req.body.content;  // set the clip content(comes from the request)
        clip.timestamp = new Date();

        // save the clip and check for errors
        db.insert(clip, function (err, newDoc) {
            if (err)
                res.send(err);
            res.json({ message: 'Clip created!' });
        });
    })

    // get all the clips (accessed at GET http://localhost:8080/api/clips)
    .get(function(req, res) {
        db.find({}).sort({timestamp: -1}).exec(function (err, clips) {
            if (err)
                res.send(err);

            res.json(clips);
        });
    });

// on routes that end in /clips/:search_string
// ----------------------------------------------------
router.route('/clips/:search_string')

    // get the clips which contains the search_string (accessed at GET http://localhost:8080/api/clips/:search_string)
    .get(function(req, res) {
        var regex = new RegExp(req.params.search_string);   //create a new Regex object to parse the varible as a pattern
        db.find({ content: regex }, function (err, clips) {
            if (err)
                res.send(err);

            res.json(clips);
        });
    });

// REGISTER OUR ROUTES -------------------------------
// all of our routes will be prefixed with /api
app.use('/api', router);

// START THE SERVER
// =============================================================================
app.listen(port);
console.log('Magic happens on port ' + port);

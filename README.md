# pinbird-spark-backend
playing with the spark java REST framework

[![Build Status](https://travis-ci.org/xorrr/pinbird-spark-backend.png)](https://travis-ci.org/xorrr/pinbird-spark-backend)

# Run
The run script will start the server at [http://localhost:5000](http://localhost:5000)

    ./bin/run.sh

# Usage
For POST requests you need to add the "Authorizaton" header with the set pw to the request (Basic access authentication without encoding). Later on a token based approach will be used.

# Development
Run unit tests:

     ./bin/run_tests.sh

Run unit and integration tests

     ./bin/run_integration_tests.sh

# License
MIT

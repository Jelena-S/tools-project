# Simple price comparator for rent a car agencies

## About

This is simple application for comparing renting prices of cars for several rent-a-car agencies. This application scrapes data from rent-a-car agencies, formats and stores extracted data in database. You can search for desired car and see compared prices. 
* Home page contains short description of application.
* Search page contains form where you enter car name. By clicking you submit your choice and result will be shown. You can directly go to the site and see details and make reservation or get back to searching. If you submit empty car name, all car prices will be shown.

### Model
![model](https://github.com/Jelena-S/tools-project/blob/master/resources/public/pictures/model-alati.PNG?raw=true)

## Libraries used

* [Reaver](https://github.com/mischov/reaver)
* [java.jdbc](https://github.com/clojure/java.jdbc)
* [sqlite-jdbc](https://github.com/xerial/sqlite-jdbc)
* [Hiccup](https://github.com/weavejester/hiccup)
* [Ring](https://github.com/ring-clojure/ring)
* [Compojure](https://github.com/weavejester/compojure)


## Prerequisites

You will need [Leiningen](https://github.com/technomancy/leiningen) 2.0.0 or above installed.

## Running

To start a web server for the application navigate to tools-project and run: 
```bash
lein ring server
```

## License

Eclipse Public License 2.0

Copyright © 2021 Jelena Sreckovic

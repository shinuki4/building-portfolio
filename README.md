# Building portfolio

Building portfolio is a Play Framework Java application for CRUD operation and a REST api.

## Installation

After cloning the project don't forget to:
- Check out the Framework requirements https://www.playframework.com/documentation/2.8.x/Requirements
- Create a Postgres database
- Add a file named **database.properties**, it will be used to configure your database connection.

For the example it will take the following value:
```database.properties
databaseUser=building-app
databasePassword=building
databaseName=building-portfolio
portNumber=5432
serverName=localhost
```

## Run
```run
If you want to run the app
    sbt run
If you want to run the test
    sbt test
If you want to get the package to deploy your app on a webapp server
    sbt deploy
    see after the comand complete directory .../target/universal
```

## Assumptions

- There's probably some possibility to improve the API call by making them in resolve in parallel to win some time during the insertion in database
- To test what if the Geoapify API doesn't respond
- Adding an api key to secure the api and restrict the usage of it

## Issues

It was quite complicated after 6 years without using Java. It feels like a new language since the Java 8 and felt like using node.js in way by making a fully asynchronous API.
The use of Play framework as well was challenging. 
However, I've used Spring MVC and had some memories about how that kind of app are built even if it's not completely alike that experience helped me a lot.
- Configuring correctly the app was also something which take some time
- Understanding what was the completion stage and how they are resolve was a bit challenging.
- the biggest issue was probably to learn all those concept and new framework

I m really glad to have done this app and achieved it !

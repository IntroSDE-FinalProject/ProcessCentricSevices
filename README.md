# ProcessCentricSevices

The Process Centric Service is a REST web service.  This layer receives requests from User Interface and redirects them to [Business Logic Service](https://github.com/introsde-2015-FinalProject/BusinessLogicServices) or to [Storage Service](https://github.com/introsde-2015-FinalProject/StorageServices).

[API Documentation (apiary)](http://docs.processcentricservice.apiary.io/#)  
[URL of the server (heroku)](https://pcs-nameless-cove-5229.herokuapp.com/sdelab/)

### Install
In order to execute this server locally you need the following technologies (in the brackets you see the version used to develop):

* Java (jdk1.8.0)
* ANT (version 1.9.4)

Then, clone the repository. Run in your terminal:

```
git clone https://github.com/introsde-2015-FinalProject/ProcessCentricServices.git && cd ProcessCentricServices
```

and run the following command:
```
ant generate
ant install
```

`ant generate` run *xjc*. It compiles an [XML schema](xmlSchemaPCS.xsd) file into fully annotated Java classes.

### Getting Started
To run the server locally then run:
```
ant start
```

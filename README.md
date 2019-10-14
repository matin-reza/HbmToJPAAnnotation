# HbmToJPAAnnotation

How it is run.

1- Make it as jar file


2- Redirect where the jar file is and run the following command:


java -cp hbmToAnnotation-full.jar; hbmToJPAAnnotation.impl.MainClass [HbmXMLFilesPath] [outputPath] [schema]


[HbmXMLFilesPath]: it contains the path of Hbm xml files.


[outputPath]: it contains the path where the JPA JAVA class will be generated.


[schema]: Your DataBase schema name.


For example:

java -cp hbmToAnnotation-full.jar; hbmToJPAAnnotation.impl.MainClass directory1\directory2 jpaoutput\\



Note: if the application can not convert each Hbm file to JPA java class, it is shown in the console.


and this app does not support one-to-one property.

# Article Peer-Review
Java Servlet Technology including Form Data, HTTP Request Header, Response Header, Status Codes and Session Tracking. It uses JSP format as a view technology. 

## What this application is about? 
It is an online site for writers looking for their articles to be published. Authors of the articles, reviewers and redactor can log in.
Every user has different authority. Authors can create, modify, browse his/her articles and see the status and decision to the article (whether it is accepted by redactor, declined or given a second chance to be modify and sent to review again (it is called "Next Round").
Reviewers see articles with status 'Waiting for a review' and then they can write a review and give a mark to a particular article.
Redactor sees reviewed articles and can decide about the article (he/she can accept it, decline it or take it to the next round).

## How does it look like?
![alt text](https://i.imgur.com/qrljBqd.png)

![alt text](https://i.imgur.com/DwRrQxJ.png)

## How can it be improved?
Since this is my first bigger web project it has a lot to be improved. First of all, data access object (DAO) design pattern should be used. It would separate operations from high level services. What is more, I should utilize Apache Maven and Spring MVC framework.
Dependency injection used by Spring would improve the code by achieving simplification and increasing testability. The division between controllers, JavaBean models, and views would be much cleaner.

## How can I run it?
Currently you can only run it on your local server (Apache Tomcat in this case). In the future I will post the project on an online server with remote database. 

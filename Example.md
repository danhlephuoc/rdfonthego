# Introduction #

> This example shows you how to build a simple Android app with RDFOnthego.

> The source code is <a href='http://rdfonthego.googlecode.com/files/Example.rar'>here.<a></li></ul>


# Details #
> In this example, we used RDF store to store the foaf profile. Foaf profiles are converted to n-triples by sindice any23 api. To avoid type the URL,we are using QRCode scanner api. The QRCode generator plug-in for FireFox is easily found on google.com , and QRCode scanner for android at android market.

  * Adding triples to store:
```
 //Create or open a store name "example"
 graph	=	new BDBGraph("example");
 //Retrieve data after converted from any23 api.
 graph.load("http://api.sindice.com/any23/any23/?format=ntriples&uri="+contactFoaFid, contactFoaFid);
 //Save the graph
  graph.sync();
```

  * Query name of the foafid
```
 //Open the store name "example"
 graph = new BDBGraph("example");
 
 //Create the model
 Model model	=	new ModelCom(graph);
		
 //This SPARQL query, searching all the name in the store	
 String queryString = StrUtils.strjoin("\n", 
 "PREFIX foaf:    <http://xmlns.com/foaf/0.1/>" ,
 "SELECT DISTINCT ?name {" ,
 "?x foaf:name ?name.", 
 "}");
		        
 //Create query
 Query query = 	QueryFactory.create(queryString) ;
 //Create Execution query  
 QueryExecution qExec = QueryExecutionFactory.create(query, model) ;}}}
		   
 //Get the result
 for(ResultSet itr=qExec.execSelect();itr.hasNext();){ 
     QuerySolution sol=itr.next(); 
     nameListString.add(sol.get("name").toString());
 }
 qExec.close();
```
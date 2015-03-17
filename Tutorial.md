## Setup project: ##
  * Step 1:<a href='http://developer.android.com/sdk/installing.html'>Create a Android project.</a>
> RDFOnthego requires Android 2.1 or later version. In order to enable the project to write to external storage like SD card, WRITE\_EXTERNAL\_STORAGE permission has to be added.

  * Step 2: Adding rdfonthego jar files to your Android project

> - <a href='http://rdfonthego.googlecode.com/files/je-android-4.1.10.jar'>je-andoird-4.1.10.jar</a>. Berkeley DB JE for Android (Required!)

> - <a href='http://rdfonthego.googlecode.com/files/deri.org.rdfonthego.core.jar'>deri.org.rdfonthgo.core.jar</a> Jena SW toolkit with RDF store built in(Required!)

> - <a href='http://rdfonthego.googlecode.com/files/deri.org.rdfonthego.spatial.jar'>deri.org.rdfonthego.spatial.jar</a> (Optional!). The spatial indexing packages.

> - <a href='http://rdfonthego.googlecode.com/files/deri.org.rdfonthego.lucene.jar'>deri.org.rdfonthego.lucene.jar</a> (Optional!) The Lucene packages.
## Working with RDFOnthego ##

> RDFOnthego based on <a href='http://jena.sourceforge.net/'>Jena Semantic Web</a> framework, so that it works like Jena. If you are not familiar with Jena please take a look at <a href='http://jena.sourceforge.net/tutorial/RDF_API/index.html'> Jena Tutorial.</a>

## 1. BDBGraph ##
> BDBGraph is extended class of <a href='http://jena.sourceforge.net/javadoc/com/hp/hpl/jena/graph/Graph.html'>Jena Graph interface</a>. It is used as a store handler with methods working with triples such as adding, deleting, saving.
### 1.1 Create a Store ###

> Store can be handled by the BDBGraph class. The example below shows 2 ways to create a store with its name.
```
 BDBGraph graph; 
 String storeName = "Store"; 
 
 graph = Factory.createBDBGraph(storeName); 
```

> Or:
```
 BDBGraph graph;  
 String storeName = "store";
 
 graph = new BDBGraph(storeName); 
```

> _**NOTE**_ : Remember to close store when you finish your work by calling `graph.close()`. If store is not closed, it will take time when you open it again.

### 1.2 Adding Triples ###
> With created store, triples can be added as single triple, or can be loaded from text file, and can be downloaded from an URL.
  * Add a single triple to store:
```
 Node s,p,o; 
 Triple t;  

 s = Node.createURI("http://example.org");
 p = Node.createURI("http://xmlns.com/foaf/0.1/name");
 o = Node.createLiteral("Anh Le Tuan");

 t = new Triple (s,p,o);

 graph.add(t);
 graph.sync();
 graph.close();
```

  * Load Triple from file:
```
 String filePath	= android.os.Environment.getExternalStorageDirectory()+ "/Android/data/foaf.n3"; 
 String baseUri = "http://www.deri.ie/fileadmin/scripts/foaf.php?id=532";

 graph.load(String filePath, String baseUri) 
 graph.sync();
 graph.close();
```

  * Load Triple from an URL
> BDBGraph only supports to RDF n-triple and n3 so others RDF formats must be converted to n-triples before being put to graph. In case of retrieving form and URL, we suggested using any23.org api to convert to n-triple to effect the system.
```
 String baseUri = "http://www.deri.ie/fileadmin/scripts/foaf.php?id=532"; 
 String url = "http://any23.org/nt/" + baseUri;}}}
 
 graph.load(String url, String baseUri) 
 graph.sync();
 graph.close();
```


## 2 Working with store ##

### 2.1 Model ###

> The only thing is required to create a model from BDBGraph is the store name.

> The model can be create by:
```
 Model model;
 String storeName = "store";
 
 model = ModelFactory.creatModelStore("store");
```

> Or:
```
 BDBGraph graph = new BDBGraph(storeName);}}}
 model = new ModelCom(graph);
```

> And now we are able to see the statements we have in our store.
```
 // list the statements in the Model
 StmtIterator iter = model.listStatements();
 // print out the predicate, subject and object of each statement
 while (iter.hasNext()) { 
   // get next statement
    Statement stmt      = iter.nextStatement();  
    Log.d("System.out", stmt.asTriple.toString());
 } 
```

### 2.2 Query with ARQ ###

> In Rdfonthego project, <a href='http://openjena.org/ARQ/app_api.html'>JENA ARQ api.</a> has been embedded with "SELECT queries" only.

> The example below shows how to use ARQ:
```
 //Open a store
 BDBGraph graph = new BDBGraph("store");

 //Create the model
 model = new ModelCom(graph);

 //Create query String
 String queryString = StrUtils.strjoin("\n", 
 "PREFIX foaf: <http://xmlns.com/foaf/0.1/>",
 "SELECT DISTINCT  ?name {" ,
 "?uri foaf:name  ?name.", 
 "}");

 //Create a query 
 Query query = QueryFactory.create(queryString) ; 
 //Create query execution
 QueryExecution qExec = QueryExecutionFactory.create(query, model) ; 

 //Get the results
 for(ResultSet itr    = qExec.execSelect();itr.hasNext();){ 
     sol=itr.next(); 
     Log.d("System.out", sol.get("name").toString()); 
 }
 qExec.close() ; 
```

### 2.3 Integrate with Spatial ###

> The examples how to use the spatial index to work with Geo RDF.
```
 //Open the store
 BDBGraph graph	=	new BDBGraph("Map");
 //Create the model
 Model    model	=	new ModelCom(graph); 
    			
 //Setup the dimension of RTree  
 PropertySet ps2 = new PropertySet(); 
 ps2.setProperty("Dimension", 2);

 //BDBStoreManager is the handler of spatial index store
 BDBStorageManager	bdbSM = new BDBStorageManager();
 RTree rtree;
 
 //Create Rtree with the handler
 rtree = new RTree(ps2, bdbSM);

 Indexer i=	new Indexer(rtree);
 //Indexing
 i.createIndex(model);

 //Save the index
 bdbSM.flush();
    	
 //Nearby query	String
 String queryString = StrUtils.strjoin
 ("\n",
  "PREFIX spatial: <java:org.geospatialweb.arqext.>",
  "PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>",
  "SELECT DISTINCT  ?lat ?long{" ,    
  "?uri geo:lat ?lat.",
  "?uri geo:long ?long.",
  "?s spatial:nearby(53.285029 -9.081154 10)",
  "}") ;
    
 Query query = QueryFactory.create(queryString) ; 
 QuerySolution sol;
    			
 QueryExecution qExec = QueryExecutionFactory.create(query, model) ;
 
 //Apply index to the queryExecution   
 Geo.setContext(qExec, i);

 //Getting the result
 for(ResultSet itr    = qExec.execSelect();itr.hasNext();){
    sol=itr.next();
    System.out.println(sol.get("lat").toString() + "===" + sol.get("long").toString() ); 
 }
 qExec.close() ;
```

### 2.4 Integrate with Lucene ARQ ###
> LARQ is a combination of ARQ and Lucene. It gives ARQ the ability to perform free text searches. Lucene indexes are additional information for accessing the RDF graph, not storage for the graph itself. The example below shows how BDBGraph work with LARQ.
```
 String appFolder = androis.os.Environment.getExternalStorageDirector() + "/Androi/data/";
 
 //Open store
 BDBGraph graph = new BDBGraph("store");
 //Create model
 Model model = new ModelCom(graph);

 //Create the Indexbuilder
 //IndexBuilderString: This is the most commonly used index builder,and uses for indexing plain literals.
 //IndexBuilderSubject: Index the subject resource by a string literal, an store the subject resource, possibly restricted by a specified property
 IndexBuilderString larqBuilder = new IndexBuilderString(appFolder);

 //Create index based on existing statements
 larq.indexStatements(model.listStatements());
 //finish index
 larq.closeWrite();

 //Create the access index
 IndexLARQ index = larqBuilder.getIndex();

 //Query with luncene index
 String queryString = StrUtils.strjoin(
 "\n",
 "PREFIX pf: <http://jena.hpl.hp.com/ARQ/property#>",
 "SELECT * {" ,
 "?lit pf:textMatch '+Anh'",
 "}");
  
 //Index can be made globally avaiable 
 //LARQ.setDefaultIndex(index);
 //Or set on a single query execution
 Query query	        =	QueryFactory.create(queryString);
 QueryExecution qExec	=	QueryExecutionFactory.create(query, model);
 LARQ.setDefaultIndex(qExec.getContext(),index);

 QuerySolution sol;

 //Get the results
 for(ResultSet itr    = qExec.execSelect();itr.hasNext();){
     sol=itr.next();
     System.out.println(sol.get("lit").toString());
 }
   	
 qExec.close();
```



For more details please take a look at our example. How to build a simple android application with rdfonthego.


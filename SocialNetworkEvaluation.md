# Introduction #

This tutorial explains how to set up the RDF On The Go for an Android application and how we did the experiences on that.

# 1.Setting up an Android application with RDF OTG #
This new version of RDF OTG provides native RDF storage and Sparql querying engine for Android platform. Thus, all you will need to add into your Android application are just two jar files below:

- <a href='https://rdfonthego.googlecode.com/files/otg_rdf_core.jar'>otg_rdf_core.jar</a> - The native RDF store for Android platform.

- <a href='https://rdfonthego.googlecode.com/files/otg_arq.jar'>otg_arq.jar </a> - The spaql query engine which is ported from Jena ARQ with some modifications in order to pair with RDF OTG.

**Note: Requires Android 2.3.3 or later version and WRITE\_EXTERNAL\_STORAGE permisson has to be added in the Android project.**

## 2. Setting up for evaluations ##

### 2.1 Social network generation ###
> For data simulations, we used <a href='http://sourceforge.net/projects/sibenchmark/'> Social Intelligence Benchmark(SIB) tool</a> which takes the schema style from popular social networks such as Facebook as the baseline for designing an RDF-friendly, scientific benchmark. Specifically, it simulates an RDF backend of a social network site, in which users and their interactions form an social graph of social activities such as writing posts, posting comments, creating/managing groups, etc.

> The number of users could be change in the parameter file - params.ini. And we also modified name space, the number of friends, the number of photos, posts, comments in order to simulate different network. Example www.facebook.com for facebook, www.linkedin.com for Linkedin, etc. The triples will be saved into text file with n-triples format. The simulated data for our evaluation could be found <a href='https://rdfonthego.googlecode.com/files/social%20dataset.rar'>here</a>


### 2.2 Running the experiments ###
> In our framework, we provide two different Graphs to handle the data. The first one is OTGGraph, this is a single view graph, it doesn't contains the Data Consolidation or ID Resolver. The second one is the `MasterGraph`, which provides you an unified view of data when retrieving data from different sources. The "Data Consolidation" and "ID Resolver" will operate data automatically when they find two ID are matched. It's also keep your data distinct from different sources and you can trace back you data using graph named query(Example in part 3 of the evaluation).

#### 2.2.1 Update throughput evaluation ####
> After generating the simulated graph, we put the n-triples data files into SD card under folder 'Experiment'. On Galaxy and Nexus, we can put them into their virtual removable storage. The Android OS will create an virtual path to copied directory and treat it as it is in the external storage. Thus we can get access to these files by the same way on 3 devices.

```
 String loading_path = Environment.getExternalStorageDirectory() + "/Experiment/";
```

**Note:**

> You can install <a href='https://play.google.com/store/apps/details?id=org.openintents.filemanager&feature=search_result#?t=W251bGwsMSwxLDEsIm9yZy5vcGVuaW50ZW50cy5maWxlbWFuYWdlciJd'>IO File Manager</a> the check if the files are already copied into the storage.

> In RDF OTG, we also plugged  Jena Riot Loader in case of parsing RDF from n-triples format. In first experiment, for evaluating throughput of inserting data. We created a graph with about 1500 users which contains about one million triples and parse into the store, then we measure the insertion speed. The codes of experiment are in the scripts below:

```
 //TDBOID
 public static void start(){
 String loading_path = Environment.getExternalStorageDirectory() + "/Experiment/";
 String store_path   = Environment.getExternalStorageDirectory() + "/TDBOID/";
 Graph graph = TDBFactory.createGraph(store_path);
 FileUtil fileUtil = new FileUtil("TDBOID_1_Ex");
 BufferedReader in;
 ParserTurtle parser = new ParserTurtle();
 try {
	    fileUtil.start();
	    for (int i = 1; i < 100; i++) {
	        fileUtil.write(graph.size());   
	        int k = 1500000 + i;
		in = new BufferedReader(new FileReader(loading_path + "mr0_fb" + Integer.toString(k) + ".nt"));
		parser.parse(graph, " ", in);
		fileUtil.write(graph.size());
		TDB.sync(graph);
	    }
	    fileUtil.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
```

```
 //RDF OTG
 String load_path = Environment.getExternalStorageDirectory() + "/Experiment/";
 String store_Path = Environment.getExternalStorageDirectory() + "/RDF_OTG/";
 public static void start() {
        Graph graph = new OTGGraph(store_path);
	FileUtil fileUtil = new FileUtil("RDF_OTG_1_Ex");
	try {
	    fileUtil.start();
	    fileUtil.write(graph.size());
	    for (int i = 1; i < 100; i++) {
	        fileUtil.write(graph.size());
		int k = 1500000 + i;
		RiotLoader.read(loading_path + "mr0_fb" + Integer.toString(k) + ".nt", graph);
		fileUtil.write(graph.size());
		graph.sync();
	    }
	    fileUtil.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
}
```

> The experiments stopped when the loading finishes or the application crashes due to out of heap memory.

#### 2.2.2 Comparing query evaluation ####
> For evaluating the query processor, we implemented Integration application with both TDBOID and RDF OTG. The applications could be found in <a href='https://play.google.com/store/apps/details?id=org.openintents.filemanager&feature=search_result#?t=W251bGwsMSwxLDEsIm9yZy5vcGVuaW50ZW50cy5maWxlbWFuYWdlciJd'>here</a>. With each application, we set up 45 profiles(15 in each dataset) on HTC, 180 profiles(60 in each data set) on GALAXY, 120 profiles(40 in each data set) on NEXUS 7. After than we measure the response time of each query.

```
//Example code setup data for second experiment with TDBOID.
static void load_htc() {
   AdParserTurtle parser = new AdParserTurtle();
   try {
       JenaMasterGraph jmg = new JenaMasterGraph("facebook");
       BufferedReader in;
       for (int i = 1; i < 16; i++) {
          int k = 300000+i;
	  in = new BufferedReader(new FileReader(loadPath + "mr0_fb" + Integer.toString(k) + ".nt"));
	  parser.parse(jmg, " ", in);
       }
       jmg.sync();
    
       jmg = new JenaMasterGraph("linked");
       for (int i = 1; i < 16; i++) {
          int k = 300000 + i;
	  in = new BufferedReader(new FileReader(loadPath + "mr0_lk" + Integer.toString(k) + ".nt"));
	  parser.parse(jmg, " ", in);
      }
      jmg.sync();

      jmg = new JenaMasterGraph("phone");
      for (int i = 1; i < 16; i++) {
  	int k = 300000+ i ;
		in = new BufferedReader(new FileReader(loadPath + "mr0_ph" + Integer.toString(k) + ".nt"));
		parser.parse(jmg, " ", in);
      }
      jmg.close();

    } catch (IOException e) {
	    e.printStackTrace();
    }
}
```


```
//Example code setup data for second experiment with RDF_OTG.
static void load_htc() {
    try {
        Graph graph = new MasterGraph("facebook");
       	RiotLoader.read(loadPath + "mr0_fb300.nt", graph);
	graph.sync();
    
        Graph graph1 = new MasterGraph("linked");
        RiotLoader.read(loadPath + "mr0_lk300.nt", graph1);
        graph1.sync();

        Graph graph2 = new MasterGraph("phone");
	RiotLoader.read(loadPath + "mr0_fb400.nt", graph2);
	graph2.sync();

    } catch (IOException e) {
        e.printStackTrace();
    }
}
```



> The scripts below are showing the code example of using querying on TDBOID and RDF OTG in our evaluation.

```
//Example code running with the first query on TDBOID 
public static void query1(int numProfile, JenaMasterGraph JMG){
   String file = fileName + "_Q1_" + Integer.toString(numProfile);
   System.out.println(file);
   FileUtil fileUtil = new FileUtil(file);
   Query query = QueryFactory.create(query1);
   Model model = new ModelCom(JMG);
	
   QueryExecution qExec = QueryExecutionFactory.create(query, model);
   
   try{
       fileUtil.start();
       fileUtil.write(0);
       ResultSet itr = qExec.execSelect();
  
       int i = 0;
       while (itr.hasNext()){
         i++;
	 QuerySolution sol = itr.next();
	 Log.d("System.out", i + " s : "  +   sol.get("s").asNode().toString()
			       + " p : "  +   sol.get("p").asNode().toString()
			       + " o : "  +   sol.get("p").asNode().toString());
       }
       fileUtil.write(1);
       fileUtil.close();
       
    }catch(IOException e){
       e.printStackTrace();
    }
}
```

```
public static void query1(Graph graph) {
    FileUtil fileUtil = new FileUtil(fileName + "q1");

    try {
        fileUtil.start();
        fileUtil.write(1);

	Query query = QueryFactory.create(query1);
	QueryExecution qExec = QueryExecutionFactory.create(query, graph);

	ResultSet itr = qExec.execSelect();
	int i = 0;
	while (itr.hasNext()) {
            i++;
	    QuerySolution sol = itr.next();

	    Log.d("System.out", i + " : " + NodeLibrary.nodeString(sol.get("s")) +
                                     "--" + NodeLibrary.nodeString(sol.get("p")) +
                                     "--" + NodeLibrary.nodeString(sol.get("o")));
	    }
	    fileUtil.write(1);
	    fileUtil.close();
	    qExec.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
}
```

Here are the script of the queries.
```
static String query1 = StrUtils.strjoin("\n", 
  "PREFIX foaf:   <http://xmlns.com/foaf/0.1/>", 
  "SELECT ?s ?p ?o",
  "WHERE", 
  "{ ", 
     "?s foaf:mbox 'mailto:Roger151@gmx.com'.",
     "?s ?p ?o.",
  "}");
```

```
static String query2 = StrUtils.strjoin("\n", 
  "PREFIX foaf:   <http://xmlns.com/foaf/0.1/>", 
  "PREFIX db:     <http://dbpedia.org/resource/>", 
  "SELECT ?firstName ?lastName ?mbox ?friend", 
  "WHERE",  
  "{ ", 
     "?person foaf:based_near db:Peoples_Republic_of_China.",  
     "?person foaf:firstName ?firstName.", 
     "?person foaf:lastName  ?lastName.", 
     "?person foaf:knows     ?friend.", 
     "?person foaf:mbox      ?mbox.", 
   "}");
```

```
static String query3 = StrUtils.strjoin("\n", 
   "PREFIX sibv:   <http://www.ins.cwi.nl/sib/vocabulary/>", 
   "PREFIX sioc:   <http://rdfs.org/sioc/ns#>", 
   "PREFIX dbpo:   <http://dbpedia.org/ontology/>", 
   "SELECT DISTINCT ?location", 
   "WHERE", 
   "{ ", 
      "?user  sioc:account_of <http://www.facebook.com/person/p151>.", 
      "?photo sibv:usertag    ?user.", 
      "?photo dbpo:location   ?location.",
   "}");
```

```
static String query4 = StrUtils.strjoin("\n", 
   "PREFIX foaf:   <http://xmlns.com/foaf/0.1/>", 
   "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>", 
   "SELECT  DISTINCT ?predicate", 
   "WHERE", 
   "{ ", 
      "?person  rdf:type    foaf:Person.", 
      "?subject ?predicate  ?person.", 
   "}");
```

```
static String query5 = StrUtils.strjoin("\n", 
   "PREFIX foaf:   <http://xmlns.com/foaf/0.1/>", 
   "PREFIX rdf:    <http://www.w3.org/1999/02/22-rdf-syntax-ns#>", 
   "SELECT  DISTINCT ?predicate", "WHERE", 
   "{ ", 
      "{ ",	 
         "?person  rdf:type foaf:Person.", 
         "?subject ?predicate  ?person.", 
      "} UNION {", 
         "?person  rdf:type foaf:Person.", 
	 "?person  ?predicate  ?object.", 
      "}", 
   "}");
```

```
static String query6 = StrUtils.strjoin("\n", 
   "PREFIX foaf:        <http://xmlns.com/foaf/0.1/>", 
   "PREFIX rdf:         <http://www.w3.org/1999/02/22-rdf-syntax-ns#>",
   "PREFIX sioc:        <http://rdfs.org/sioc/ns#>",
   "PREFIX sibv:        <http://www.ins.cwi.nl/sib/vocabulary/>", 
   "SELECT  DISTINCT ?photo",
   "{ ", 
      "<http://www.facebook.com/person/p39> foaf:knows ?person.", 
      "?user  sioc:account_of ?person.",
      "?user  sibv:like       ?photo.",
      "?photo rdf:type sibv:Photo.",
   "}");
```

#### 2.2.3 Scalability evaluation ####
> For scalability evaluation we only doing experiment on our RDF OTG. We using data set of 900 profiles(each simulated graph contains 300 profiles) for HTC, 1200 profiles(each simulated graph contains 400 profiles) for GALAXY and NEXUS. With RDF OTG we are able to use sparql with named graph to trace back the data from source graph.

> Here are the extended queries in the last experiment:

```
static String query7 = StrUtils.strjoin("\n", 
"PREFIX foaf:        <http://xmlns.com/foaf/0.1/>", 
"PREFIX rdf:         <http://www.w3.org/1999/02/22-rdf-syntax-ns#>",
"PREFIX sioc:        <http://rdfs.org/sioc/ns#>",
"PREFIX fbph :       <http://www.facebook.com/photoalbum/>", 

"SELECT    ?person",
"{ ", 
      "GRAPH <linked>",
        "{",
           "?person rdf:type  foaf:Person.",	
        "}",
      "GRAPH <master>",
        "{",     	
        "?user sioc:account_of ?person.",
        "?user sioc:creator_of fbph:pa453.",
        "}",
"}");
```

```
static String query8 = StrUtils.strjoin("\n", 
"PREFIX foaf: <http://xmlns.com/foaf/0.1/>", 
"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>",
"PREFIX sioc: <http://rdfs.org/sioc/ns#>",
"PREFIX sibv: <http://www.ins.cwi.nl/sib/vocabulary/>", 

"SELECT ?comment",
"{ ", 
      "GRAPH <facebook>",
        "{",
           "?comment rdf:type  sibv:Comment.",	
           "?user    sioc:creator_of  ?comment.",	
        "}",
      "GRAPH <master>",
        "{",     	
        "?user sioc:account_of <http://linkedin.com/person/p174>.",
        "}",
"}");
```
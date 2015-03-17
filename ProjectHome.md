RDF On The Go is the project to build a persistent  RDF store and query processor on Android phone.


For storage of the data, RDF on The Go uses a lightweight version of the Berkeley DB that is suitable for mobile devices, which provides a B-Tree implementation for accessing the RDF graphs. For each RDF node, the system employs dictionary encoding where node values are mapped to integer identiers. This reduces the space required to store each RDF node, since the encoded version of the nodes are considerably smaller than the original ones. Moreover, dictionary encoding also allows faster processing, since integer comparisons are cheaper. Fast lookups are achieved in a two-step approach: first, each triple node is stored in multiple ways with different orderings of the triple elements, similar to Hẽxastore. Then indexes are built for every ordering of the triple pattern, as proposed in.


To encourage developers to use RDF On the Go to build their applications, we have adapted the core APIs of Jena and ARQ to the Android environment. This allows the developers to manipulate RDF graphs in the same way as they do with the desktop versions of Jena and ARQ . We also reuse some of the Jena and ARQ packages such as the RDF parser, the SPARQL query parser and Lucene indexing LARQ as well.

### <a href='http://code.google.com/p/rdfonthego/wiki/Tutorial'> Tutorial</a> ###

package indexing;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Service {

    private Indexer indexer;

    private Searcher searcher;

    public void createPdfIndex(Edition edition, File file) throws IOException {
        indexer = new Indexer(LuceneConstants.indexingPath);
        long startTime = System.currentTimeMillis();
        indexer.indexFile(file, edition.getId());
        long endTime = System.currentTimeMillis();
        indexer.close();
        System.out.println(file.getName() + " indexed, time taken: " + (endTime - startTime) + "ms");
    }


    public Set<Long> searchInPdfFiles(String searchString) throws IOException, ParseException, InvalidTokenOffsetsException {
        Set<Long> ids = new HashSet<>();

        searcher = new Searcher(LuceneConstants.indexingPath);
        TopDocs hits = searcher.search(searchString, 10);
        System.out.println(hits.totalHits + " for searchString \"" + searchString + "\" found");
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = searcher.getDocument(scoreDoc);
            Long id = Long.valueOf(doc.get(LuceneConstants.ID));
            String[] fragments = searcher.previewText(doc, scoreDoc.doc);
            System.out.println(id + "\n");
            System.out.println(fragments[0]);
            System.out.println("\n\n");
            ids.add(id);
        }

        return ids;
    }

    public void deletePdfIndex(Edition edition) throws IOException {
        System.out.println("Deleting index from: " + edition.getId());
        indexer = new Indexer(LuceneConstants.indexingPath);
        indexer.deleteIndex(edition.getId());
    }
}

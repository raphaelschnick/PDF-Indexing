package indexing;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.de.GermanAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

public class Searcher {

    private IndexSearcher indexSearcher;
    private QueryParser queryParser;
    private Query query;

    public Searcher(String indexDirectoryPath) throws IOException {
        Directory indexDirectory = FSDirectory.open(Paths.get(indexDirectoryPath));
        IndexReader reader = DirectoryReader.open(indexDirectory);
        indexSearcher = new IndexSearcher(reader);
        queryParser = new QueryParser(LuceneConstants.CONTENT, new GermanAnalyzer());
    }

    public TopDocs search(String searchQuery, Integer maxSearchResults) throws IOException, ParseException {
        query = queryParser.parse(searchQuery);
        return indexSearcher.search(query, maxSearchResults);
    }

    public Document getDocument(ScoreDoc scoreDoc) throws IOException {
        return indexSearcher.doc(scoreDoc.doc);
    }

    public String[] previewText(Document doc, int id) throws IOException, InvalidTokenOffsetsException {
        Directory dir = FSDirectory.open(Paths.get(LuceneConstants.indexingPath));
        IndexReader reader = DirectoryReader.open(dir);
        Analyzer analyzer = new StandardAnalyzer();
        QueryScorer scorer = new QueryScorer(query);
        Formatter formatter = new SimpleHTMLFormatter();
        Highlighter highlighter = new Highlighter(formatter, scorer);
        Fragmenter fragmenter = new SimpleFragmenter( 10);
        highlighter.setTextFragmenter(fragmenter);
        String text = doc.get(LuceneConstants.CONTENT);
        TokenStream stream = TokenSources.getAnyTokenStream(reader, id, LuceneConstants.CONTENT, analyzer);
        String[] fragments = highlighter.getBestFragments(stream, text, 50);
        dir.close();
        return fragments;
    }
}

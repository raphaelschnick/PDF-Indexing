package indexing;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.de.GermanAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.StopwordAnalyzerBase;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class Indexer {

    private IndexWriter writer;

    public Indexer(String indexDirectoryPath) throws IOException {
        Directory indexDirectory = FSDirectory.open(Paths.get(indexDirectoryPath));
        System.out.println(indexDirectory);
        Analyzer analyzer = new StandardAnalyzer();

        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        writer = new IndexWriter(indexDirectory, indexWriterConfig);
    }

    public void close() throws IOException {
        writer.close();
    }

    private Document getDocument(File file, Long id) throws IOException {
        Document document = new Document();

        document.add(new StoredField(LuceneConstants.ID, id));
        document.add(new TextField(LuceneConstants.TITLE, file.getName(), Field.Store.YES));
        document.add(new TextField(LuceneConstants.CONTENT, pdfContent(file), Field.Store.YES));

        return document;
    }

    private String pdfContent(File file) throws IOException {
        PDDocument document = PDDocument.load(file);
        PDFTextStripper stripper = new PDFTextStripper();

        return stripper.getText(document);
    }

    public void indexFile(File file, Long id) throws IOException {
        Document document = getDocument(file, id);
        writer.addDocument(document);
    }

    public void deleteIndex(Long id) throws IOException {
        writer.deleteDocuments(new Term("id", String.valueOf(id)));
    }
}
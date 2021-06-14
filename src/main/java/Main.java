import indexing.Edition;
import indexing.Service;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;

public class Main {

    private static final Service service = new Service();

    public static void main(String[] args) throws IOException, ParseException, InvalidTokenOffsetsException {
        System.out.println("PDF Indexer");

        File file = new File("index-store");
        if (file.exists()) {
            FileUtils.deleteDirectory(file);
        }

        Edition edition_1 = new Edition(1L, "Edition 1", "src/main/resources/pdf/1.pdf");
        Edition edition_2 = new Edition(2L, "Edition 2", "src/main/resources/pdf/2.pdf");
        Edition edition_3 = new Edition(3L, "Edition 3", "src/main/resources/pdf/3.pdf");
        Edition edition_4 = new Edition(4L, "Edition 4", "src/main/resources/pdf/4.pdf");

        indexing(edition_1);
        indexing(edition_2);
        indexing(edition_3);
        indexing(edition_4);

        search();
    }

    public static void indexing (Edition edition) throws IOException {
        File file = new File(edition.getFile());
        service.createPdfIndex(edition, file);
        System.out.println("indexing -> " + edition.getName() + " file: " + file.getAbsolutePath());
    }

    public static void search () throws ParseException, IOException, InvalidTokenOffsetsException {
        System.out.println("Suchbegriff: ");
        Scanner scanner = new Scanner(System.in);
        Set<Long> ids = service.searchInPdfFiles(scanner.nextLine());
        System.out.println("Gefundene Ausgaben: " + ids);
        search();
    }
}

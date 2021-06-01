import indexing.Edition;
import indexing.Service;

import java.io.File;
import java.io.IOException;

public class Main {

    private static final Service service = new Service();

    public static void main(String[] args) throws IOException {
        System.out.println("PDF Indexer");

        Edition edition_1 = new Edition(1L, "Edition 1", "src/main/resources/pdf/1.pdf");
        Edition edition_2 = new Edition(2L, "Edition 2", "src/main/resources/pdf/2.pdf");
        Edition edition_3 = new Edition(3L, "Edition 3", "src/main/resources/pdf/3.pdf");
        Edition edition_4 = new Edition(4L, "Edition 4", "src/main/resources/pdf/4.pdf");

        indexing(edition_1);
        indexing(edition_2);
        indexing(edition_3);
        indexing(edition_4);

    }

    public static void indexing (Edition edition) throws IOException {
        File file = new File(edition.getFile());
        service.createPdfIndex(edition, file);
        System.out.println("indexing -> " + edition.getName() + " file: " + file.getAbsolutePath());
    }
}

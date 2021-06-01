package indexing;

public class Edition {

    private Long id;

    private String name;

    private String file;

    public Edition(Long id, String name, String file) {
        this.id = id;
        this.name = name;
        this.file = file;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "Edition{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", file='" + file + '\'' +
                '}';
    }
}

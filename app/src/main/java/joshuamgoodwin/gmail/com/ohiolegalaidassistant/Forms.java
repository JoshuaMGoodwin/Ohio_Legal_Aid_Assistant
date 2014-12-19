package joshuamgoodwin.gmail.com.ohiolegalaidassistant;

public class Forms {

	private int id;
	private String formName;
	private String fileName;
    private String extension;

	public int getId() {
		return id;
	}

	public String getFormName() {
		return formName;
	}

	public String getFileName() {
		return fileName;
	}

    public String getExtension() {return extension;}

	public void setId(int i) {
		id = i;
	}

	public void setFormName(String name) {
		formName = name;
	}

	public void setFileName(String name) {
		fileName = name;
	}

    public void setExtension(String ext) {
        extension = ext;
    }

}

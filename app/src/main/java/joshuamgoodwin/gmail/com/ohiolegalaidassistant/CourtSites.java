package joshuamgoodwin.gmail.com.ohiolegalaidassistant;

public class CourtSites {

private int id;
private String courtName;
private String courtAddress;

	public int getId() {
		return id;
	}
	
	public String getCourtName() {
		return courtName;
	}
	
	public String getCourtAddress() {
		return courtAddress;
	}
	
	public void setId(int i){
		id = i;
	}
	
	public void setCourtName(String text) {
		courtName = text;
	}
	
	public void setCourtAddress(String text) {
		courtAddress = text;
	}

}

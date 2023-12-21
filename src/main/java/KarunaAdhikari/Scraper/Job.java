package KarunaAdhikari.Scraper;

public class Job {
	//features
	String title;
	String link;
	String company;
	String location;
	
	public Job() {
		this.title = "";
		this.link = "";
		this.company = "";
		this.location = "";
	}
	
	//getters and setters
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getLink() {
		return link;
	}
	
	public void setLink(String link) {
		this.link = link;
	}
	
	public String getCompany() {
		return company;
	}
	
	public void setCompany(String company) {
		this.company = company;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	//to string
	@Override
    public String toString() {
        // Assuming appropriate getters are available
        return title + "," + company + "," + location + "," + link;
    }
}

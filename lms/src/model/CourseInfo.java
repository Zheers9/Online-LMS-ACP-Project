package model;

public class CourseInfo {
	private int  id;
    private String title;
    private String description;
    private int credit;
    private String categoryName;
    String lightBlue = "\u001B[36m"; // ANSI code for light blue
    String resetColor = "\u001B[0m"; // ANSI code to reset color

    // Constructor
    public CourseInfo(int id,String title, String description, int credit, String categoryName) {
    	this.id = id;
        this.title = title;
        this.description = description;
        this.credit = credit;
        this.categoryName = categoryName;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getCredit() {
        return credit;
    }

    public String getCategoryName() {
        return categoryName;
    }
    
    
    // ToString method for easy display
    @Override
    public String toString() {
    	String truncatedDescription = description.length() > 35 ? description.substring(0, 35) + "..." : description;
    	return     lightBlue + "Title: " + title + resetColor + '\n' +
    	           "ID: " + id + '\n' +
    	           "Category: " + categoryName + '\n' +
    	           "Description: " + truncatedDescription + '\n' +
    	           "Credit: " + credit + '\n' +
    	           "_____________" + '\n';
    }
}

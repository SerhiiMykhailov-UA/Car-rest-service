package ua.foxminded.specificationJPA;

public class SearchCriteria {

	private String name;
	private String maker;
	private String category;
	private int yearMax;
	private int yearMin;

	
	public SearchCriteria(String name, String maker, String category, int yearMax, int yearMin) {
		this.name = name;
		this.maker = maker;
		this.category = category;
		this.yearMax = yearMax;
		this.yearMin = yearMin;

	}

	public String getName() {
		return name;
	}

	public String getMaker() {
		return maker;
	}

	public String getCategory() {
		return category;
	}

	public int getYearMax() {
		return yearMax;
	}

	public int getYearMin() {
		return yearMin;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMaker(String maker) {
		this.maker = maker;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setYearMax(int yearMax) {
		this.yearMax = yearMax;
	}

	public void setYearMin(int yearMin) {
		this.yearMin = yearMin;
	}

	@Override
	public String toString() {
		return "SearchCriteria [name=" + name + ", maker=" + maker + ", category=" + category + ", yearMax=" + yearMax
				+ ", yearMin=" + yearMin + "]";
	}
	
}

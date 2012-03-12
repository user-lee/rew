package cn.lijf.jdc.word;

public class WordBean {

	private String word;
	
	private String desc;
	
	private int showtime;

	public int getShowtime() {
		return showtime;
	}

	public void setShowtime(int showtime) {
		this.showtime = showtime;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public void printWord()
	{
		System.out.println(word);
		System.out.println(desc);
	}
	
	
}

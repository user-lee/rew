package cn.lijf.jdc.word;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import java.io.RandomAccessFile;

public class ShowWords {

	
	
	private static Random r1=new Random(System.currentTimeMillis());
	
	private static Random r2=new Random(System.currentTimeMillis()/2);
	
	private static Random r3=new Random(System.currentTimeMillis()/3);
	
	private static Random[] rs=new Random[]{r1,r2,r3};
	
	RememberList list;
	
	private String indexfile;
	
	private String wordfile;
	
	public ShowWords(String indexfilepathname,String wordsfilepathname)throws Exception
	{
		list=new RememberList();
		
		this.indexfile=indexfilepathname;
		this.wordfile=wordsfilepathname;
		list.unStoreList(indexfile);
	}
	
	
	public  WordBean showWord()throws Exception
	{
			
		int size=list.size();
		
		int pos=0;
		
		for(Random r:rs)
		{	   
			int rp=r.nextInt(size);
			pos+=rp;
			size-=rp;
		}
		
		DescriptionBean dbean=list.get(pos);
		
		RandomAccessFile rfile=new RandomAccessFile(this.wordfile,"r");
		
		rfile.seek(dbean.wordpos+(Constant.separator+"\n").getBytes("utf-8").length);
		
		List<String> wordlist=new LinkedList<String>();
		
		String line="";
		
		while((line=readUTFLine(rfile))!=null)
		{
			String utfline=new String(line.getBytes(),"utf-8");
			
			if(utfline.indexOf(Constant.separator)!=-1)break;
			if(!"".equals(utfline.trim()))wordlist.add(utfline.trim());
			
		}
		
		StringBuilder sb=new StringBuilder();
		
		for(int i=1;i<wordlist.size();i++)
		{
			sb.append(wordlist.get(i)).append("\n");
		}
		
		WordBean wbean=new WordBean();
		
		wbean.setDesc(sb.toString());
		wbean.setWord(wordlist.get(0));
		wbean.setShowtime(dbean.showtimes);
		
		dbean.showtimes++;
		
		list.add(dbean);
		
		return wbean;	
	}
	
	public  String readUTFLine(RandomAccessFile rfile)throws Exception
	{
		List<Byte> linelist=new LinkedList<Byte>();
		int c = -1;
		boolean eol = false;

		while (!eol) {
		    switch (c = rfile.read()) {
		    case -1:
		    case '\n':
			eol = true;
			break;
		    case '\r':
			eol = true;
			long cur = rfile.getFilePointer();
			if ((rfile.read()) != '\n') {
			    rfile.seek(cur);
			}
			break;
		    default:
			linelist.add((byte)c);
			break;
		    }
		}

		if ((c == -1) && (linelist.size() == 0)) {
		    return null;
		}
		
		byte[] result=new byte[linelist.size()];
		for(int i=0;i<result.length;i++)
		{
			result[i]=linelist.get(i);
		}
		
		return new String(result,"utf-8");
	}
	
	public void store()throws Exception
	{
		list.storeList(this.indexfile);
	}
}

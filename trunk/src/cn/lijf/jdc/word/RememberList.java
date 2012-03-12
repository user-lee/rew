package cn.lijf.jdc.word;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public   class RememberList {

	private List<DescriptionBean> datalist=new LinkedList<DescriptionBean>();
	
	
	
	public synchronized void  add(DescriptionBean bean)
	{
		if(bean.showtimes>=5)
		{
			bean.showtimes=0;
			datalist.add(0, bean);
		}
		else
		{
			int size=datalist.size()-1;
			
			for(;size>0;size--)
			{
				DescriptionBean b=datalist.get(size);	
				
				if(b.showtimes<=bean.showtimes)
				{
					datalist.add(size+1,bean);
					break;
				}
			}
			
			if(size<=0)datalist.add(bean);
		}
	}
	
	public synchronized DescriptionBean get(int i)
	{
		return datalist.remove(i);
	}
	
	public synchronized int size()
	{
		return datalist.size();
	}
	
	
	public synchronized void  storeList(String path)throws Exception
	{
		File file=new File( path);
		
		DataOutputStream out=new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
		
		try
		{
			for(DescriptionBean bean:datalist)
			{
				out.writeInt(bean.showtimes);
				out.writeLong(bean.wordpos);
			}
			
			out.flush();
			out.close();
		}catch(Exception e)
		{
			out.close();
			throw e;
		}
		
	}
	
	public  synchronized void unStoreList(String path)throws Exception
	{
		File file=new File(path);
		
		DataInputStream in=new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
		
		try
		{
			while(true)
			{	
				int showtimes=in.readInt();	
				long wordpos=in.readLong();
		
				DescriptionBean bean=new DescriptionBean();
				
				bean.showtimes=showtimes;
				bean.wordpos=wordpos;
				this.datalist.add(bean);
			}		
		}catch(EOFException eofe)
		{
			
		}
		catch(Exception e)
		{
			in.close();
			throw e;
		}
		
		
		in.close();
	}
	
	public void printlist()
	{
		for(DescriptionBean bean:datalist)
		{
			System.out.println(bean.showtimes);
			System.out.println(bean.wordpos);
		}
		
	}
	
	public static void main(String[] args)throws Exception
	{
//		RememberList rlist=new RememberList();
//		
//		rlist.unStoreList("r.txt");
//		
//		rlist.printlist();
		
		mixList();
	}
	
	public static void mixList()throws Exception
	{
		
		Random r3=new Random(System.currentTimeMillis()/3);
		
		RememberList rlist=new RememberList();
		
		rlist.unStoreList("r.txt");
		
		for(int i=0;i<rlist.size();i++)
		{
			int w=r3.nextInt(rlist.size());
			DescriptionBean b=rlist.get(w);
			rlist.add(b);
		}
		
		rlist.printlist();
		
		System.out.println(rlist.size());
		
		rlist.storeList("r.txt");
	}
	
}

package cn.lijf.jdc;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import cn.lijf.jdc.word.ShowWords;
import cn.lijf.jdc.word.WordBean;
import android.app.Activity;
import android.graphics.Typeface;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.util.Log;

import android.os.Environment;


public class JDCActivity extends Activity {
    /** Called when the activity is first created. */
	
	
	
	
	private TextView wordview;
	
	private TextView commentview;
	
	private TextView showtimeview;
	
	
	private ShowWords show;
	
	private String sdcard=Environment.getExternalStorageDirectory().getAbsolutePath();
	
	private String jdcdir=sdcard+"/jdc";
	
	private String indexfilepath=jdcdir+"/r.txt";
	
	private String wordsfilepath=jdcdir+"/words.txt";
	
	private Typeface mFace;  
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
         
        
        Log.i("test",sdcard);
        
        try
        {
        
        	this.preparation();
        	show=new ShowWords(indexfilepath,wordsfilepath);
        	    
        }catch(Exception e)
        {
        	e.printStackTrace();
        }
        
        
        
        wordview=(TextView)findViewById(R.id.ID_word);
        
        commentview=(TextView)findViewById(R.id.ID_comment);
        
        showtimeview=(TextView)findViewById(R.id.ID_showtime);
        
        mFace=Typeface.createFromAsset(getAssets(), "SEGOEUI.TTF");
        
        final Button button = (Button) findViewById(R.id.ID_btn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                
            	try{
            		WordBean bean=show.showWord();
            		wordview.setText(bean.getWord());
            		commentview.setText(bean.getDesc());
            		commentview.setTypeface(mFace);
            		showtimeview.setText(String.valueOf(bean.getShowtime()));
            		
            	}catch(Exception e)
            	{
            		e.printStackTrace();
            	}
            	
            	
            }
        });
    }
	
	@Override
	public void onStart()
	{
		super.onStart();
		try{
    		WordBean bean=show.showWord();
    		wordview.setText(bean.getWord());
    		commentview.setText(bean.getDesc());
    		commentview.setTypeface(mFace);
    		showtimeview.setText(String.valueOf(bean.getShowtime()));
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
	}
	
	
	
	@Override
	public void onPause()
	{
		super.onPause();
		try
		{
			show.store();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void onStop()
	{
		super.onStop();
		try
		{
			show.store();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	private void preparation()throws Exception
	{
		File dirinsdcart=new File(jdcdir);
		if(!dirinsdcart.exists())
		{
			boolean mk=dirinsdcart.mkdir();
			Log.i("test",String.valueOf(mk));
		}
		
		
		File wordfile=new File(wordsfilepath);
		File indexfile=new File(indexfilepath);
		if(!wordfile.exists()||!indexfile.exists())
		{
			wordfile.createNewFile();
			indexfile.createNewFile();
			copyAssetsFile2SD("words.txt",wordsfilepath);
			copyAssetsFile2SD("r.txt",indexfilepath);
		}
	}
	
	private void copyAssetsFile2SD(String filename,String pathfilename)throws Exception
	{
		InputStream in=this.getAssets().open(filename);
		
		OutputStream out=new FileOutputStream(new File(pathfilename));
		
		try
		{
		
			byte[] data=new byte[1024*4];
			int lenght=0;
			while((lenght=in.read(data))>0)
			{
				out.write(data, 0, lenght);
			}
		
		
			in.close();
			out.flush();
			out.close();
		}catch(Exception e)
		{
			in.close();
			out.close();
			throw e;
		}
		
		
	}
	
	private String printDirTree(String path)
	{
		File file=new File(path);
		
		StringBuilder sb=new StringBuilder();
		
		sb.append(file.getAbsolutePath()).append("\n");
		
		if(!file.isDirectory())
		{
			
			return sb.toString();
		}
		
		
		
		String[] indir=file.list();
		for(String in:indir)
		{
			sb.append(path).append(in).append("\n");
			
		}
		
		return sb.toString();
	}
	
}
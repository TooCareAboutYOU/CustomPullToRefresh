package com.cnlive.mepluss.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;

public class FileUtils 
{
	public static void assetToFile (Context context, String name, File file) 
	{  
        AssetManager assetManager 	= context.getAssets();  
        InputStream is;  
        try 
        {  
            is 	= assetManager.open (name);  
            java.io.ByteArrayOutputStream bout 	= new java.io.ByteArrayOutputStream ();  
  
            byte[] bufferByte 	= new byte[1024];  
            int l 	= -1;  
            while ((l = is.read(bufferByte)) > -1) 
            {  
                bout.write (bufferByte, 0, l);  
            }  
            byte[] rBytes 	= bout.toByteArray ();  
            bout.close ();  
            is.close ();  
  
            if (!file.exists ()) 
            {  
                file.createNewFile ();  
            }  
  
            DataOutputStream dos 	= new DataOutputStream (new FileOutputStream (file));  
            dos.write (rBytes);  
            dos.flush ();  
            dos.close ();  
  
        } 
        catch (IOException e) 
        {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } 
        catch (OutOfMemoryError e) 
        {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
    }  
}

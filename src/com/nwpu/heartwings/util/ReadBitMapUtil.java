package com.nwpu.heartwings.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ReadBitMapUtil {

    /**
     * 
     * @param url ����URL
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
	public static Bitmap returnBitmap(final String url) throws InterruptedException, ExecutionException {
		
		FutureTask<Bitmap> task = new FutureTask<>(new Callable<Bitmap>() {

			@Override
			public Bitmap call() throws Exception {
				
				// �����������ļ�
				if(LocalFileUtil.isFileExistInCache(LocalFileUtil.urlToLocalPath(url)))
				{
					System.out.println("read from local " + LocalFileUtil.urlToLocalPath(url));
					   return LocalFileUtil.getBitmapFromLocalPath(LocalFileUtil.urlToLocalPath(url));
				}
				URL myFileUrl = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) myFileUrl
						.openConnection();
				conn.setDoInput(true);
				conn.connect();
				InputStream is = conn.getInputStream();
				Bitmap bitmap = BitmapFactory.decodeStream(is);
				is.close();
				
				System.out.println("try to save url " + url);
				LocalFileUtil.saveImgToSD(bitmap, url);
				
				return bitmap;
			}
		});

		new Thread(task).start();
		
		return task.get();
	
	}
	
	/**
	 * 
	 * @param url ����URL
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static Bitmap getShareBitmap(final String url) throws InterruptedException, ExecutionException {
		
		    FutureTask<Bitmap> task = new FutureTask<>(new Callable<Bitmap>() {

				@Override
				public Bitmap call() throws Exception {
				
					// ������ط����Ļ�������ڣ���ֱ�Ӷ���
					if(LocalFileUtil.isShareImgInCache(LocalFileUtil.urlToLocalPath(url)))
					{
						System.out.println("read from local " + LocalFileUtil.urlToLocalPath(url));
						
						return LocalFileUtil.getShareBitmapFromLocal(LocalFileUtil.urlToLocalPath(url));
						
					}
					
					// ���ڷ���ͼƬ��·���� \xfolder\x.jpg ��ʽ�������Httpǰ׺�������������
					
					URL myFileUrl = new URL(HttpUtil.BASEURL + url.substring(1));
					
					HttpURLConnection conn = (HttpURLConnection) myFileUrl
							.openConnection();
					conn.setDoInput(true);
					conn.connect();
					InputStream is = conn.getInputStream();
					Bitmap bitmap = BitmapFactory.decodeStream(is);
					is.close();
					
					System.out.println("try to save url " + url);
					
					LocalFileUtil.saveImgWithExpire(bitmap, url);
					
					return bitmap;
				}
		    	
			});
		    
		    new Thread(task).start();
		    
		    return task.get();
	}
	
}

package com.nwpu.heartwings.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.PendingIntent.CanceledException;
import android.support.v4.app.TaskStackBuilder;

import com.alibaba.fastjson.JSONArray;
import com.heart.bean.HeartMsg;
import com.heart.bean.HeartNews;
import com.heart.bean.RegularPosition;
import com.heart.bean.RegularShare;
import com.nwpu.heartwings.app.HeartApp;
import com.tencent.tencentmap.mapsdk.a.l;

public class LoadViewDataUtil {

	
	public static long FIVE_DAY = 5 * 24*60*60*1000;
	
	public static List<RegularPosition> loadPosition() throws FileNotFoundException, IOException {

		// �ȶ�����
		if (LocalFileUtil.isFileExistInCache("positions.dat")) {

			System.out.println("run read cache...");
			return LocalFileUtil.getPositionCache();

		}

		else {

			// �������ȡ
				List<NameValuePair> params = new ArrayList<>();
				for (String phone : HeartApp.getInstance().oldmanHashMap.keySet()) {
					
					params.add(new BasicNameValuePair("oldmanPhone", phone));
				}

				String data = HttpUtil.postRawRequest(HttpUtil.BASEURL
						+ "fetchMainPosition", params);

				System.out.println("load data  of position " + data);
				
				// д�뻺���ļ�
				LocalFileUtil.savePositionCache(data);
				
				return JSONArray.parseArray(data, RegularPosition.class);
				
		
		}
	}
	
	/**
	 * ˢ�µ���λ��
	 * @return
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public static List<RegularPosition> refreshPosition() throws InterruptedException, ExecutionException{
		
		// ˢ�¾��ǣ��������磬��д�뻺�棺
		FutureTask<List<RegularPosition>> task = 
				
				 new FutureTask<>(new Callable<List<RegularPosition>>() {

					@Override
					public List<RegularPosition> call() throws Exception {
						List<NameValuePair> params = new ArrayList<>();
						for (String phone : HeartApp.getInstance().oldmanHashMap.keySet()) {
							
							params.add(new BasicNameValuePair("oldmanPhone", phone));
						}

						String data = HttpUtil.postRawRequest(HttpUtil.BASEURL
								+ "fetchMainPosition", params);

						List<RegularPosition> result = new ArrayList<>();
						result.add(new RegularPosition());
						
						if(data.equals("err"))
						{
							return result;
						}
						
						System.out.println("load data  " + data);

						LocalFileUtil.savePositionCache(data);

						result.addAll(JSONArray.parseArray(data, RegularPosition.class));
						return result;
						
					}
					 
				});
		
		new Thread(task).start();
		
		return task.get();
		
		   
	}
	
	
	public static List<RegularShare> loadShare() throws FileNotFoundException, IOException{
		
		// �ȶ�����
		if(LocalFileUtil.isFileExistInCache("share.dat"))
		{
			System.out.println("run read cache share...");
			
			return LocalFileUtil.getCacheShare();
		}
		// ��������ļ������ڣ�����Ҫ��������
		else {
			
			List<NameValuePair> params = new ArrayList<>();
			for (String phone : HeartApp.getInstance().oldmanHashMap.keySet()) {
				
				params.add(new BasicNameValuePair("oldmanPhone", phone));
			}
			
			String data = HttpUtil.postRawRequest(HttpUtil.BASEURL
					+ "fetchMainShare", params);
			
			System.out.println("load from internet of share  :" + data);
			
	
			// д�뻺��
			LocalFileUtil.saveCommonCache("share.dat", data);
			
			return JSONArray.parseArray(data, RegularShare.class);
			
		}

	}
	
	public static List<RegularShare> refreshShare() throws InterruptedException, ExecutionException{
	
		FutureTask<List<RegularShare>> task 
		  = new FutureTask<>(new Callable<List<RegularShare>>() {

			@Override
			public List<RegularShare> call() throws Exception {
			
				List<NameValuePair> params = new ArrayList<>();
				for (String phone : HeartApp.getInstance().oldmanHashMap.keySet()) {
					
					params.add(new BasicNameValuePair("oldmanPhone", phone));
				}
				
				String data = HttpUtil.postRawRequest(HttpUtil.BASEURL
						+ "fetchMainShare", params);
				
				System.out.println("load from internet :" + data);
				
				List<RegularShare> result = new ArrayList<>();
				result.add(new RegularShare());
				
				if(data.equals("err"))
				{
					return result;
				}
				
				// д�뻺��
				LocalFileUtil.saveCommonCache("share.dat", data);

				result.addAll(JSONArray.parseArray(data, RegularShare.class));
				return result;
			}
			
		});
		
		new Thread(task).start();
		
		return task.get();
	}
	
	public static List<HeartMsg> loadAgenda() throws FileNotFoundException, IOException{
		    
		if(LocalFileUtil.isFileExistInCache("agenda.dat"))
		{
			return LocalFileUtil.getCacheAgenda();
		}
		
		else {
			
			List<NameValuePair> params = new ArrayList<>();
			
			for (String phone : HeartApp.getInstance().oldmanHashMap.keySet()) {
				
				params.add(new BasicNameValuePair("oldmanphone", phone));
			}
			
			String data = HttpUtil.postRawRequest(HttpUtil.BASEURL
					+ "fetchMainAgenda", params);
			
			System.out.println("load from internet :" + data);
	
			// д�뻺��
			LocalFileUtil.saveCommonCache("agenda.dat", data);
			
			return JSONArray.parseArray(data,HeartMsg.class);
			
		}
	}
	
	
	public static List<HeartMsg> refreshAgenda() throws InterruptedException, ExecutionException{
		
		 FutureTask<List<HeartMsg>> task =
				  new FutureTask<>(new Callable<List<HeartMsg>>() {

					@Override
					public List<HeartMsg> call() throws Exception {
						
						List<NameValuePair> params = new ArrayList<>();
						for (String phone : HeartApp.getInstance().oldmanHashMap.keySet()) {
							
							params.add(new BasicNameValuePair("oldmanphone", phone));
						}
						
						String data = HttpUtil.postRawRequest(HttpUtil.BASEURL
								+ "fetchMainAgenda", params);
						
						List<HeartMsg> result = new ArrayList<>();
						result.add(new HeartMsg());
						
						if(data.equals("err")){
							
							return result;
						}
						
						LocalFileUtil.saveCommonCache("agenda.dat", data);
						
						result.addAll(JSONArray.parseArray(data,HeartMsg.class));
						
						return result;
					}
				});
				  
				new Thread(task).start();
				
				return task.get();
	}
	
	
	
	public static List<HeartNews> loadHeartNews() throws FileNotFoundException, IOException{
		
		if(LocalFileUtil.isFileExistInCache("heartnews.dat"))
		{
			return LocalFileUtil.getCacheNews();
		}
		
		else{
			
			HashMap<String, String> rawParams =
					new HashMap<>();
			rawParams.put("lastDate", "150");
			
			String data = HttpUtil.postRequest(HttpUtil.BASEURL + "fetchNews", rawParams);
			
			LocalFileUtil.saveCommonCache("heartnews.dat", data);
			
			return JSONArray.parseArray(data,HeartNews.class);
		}

	}
	
	public static List<HeartNews> refreshHeartNews() throws InterruptedException, ExecutionException{
		
		FutureTask<List<HeartNews>> task =
			  new FutureTask<>(new Callable<List<HeartNews>>() {

				@Override
				public List<HeartNews> call() throws Exception {
					
					HashMap<String, String> rawParams =
							new HashMap<>();
							
					Calendar calendar = Calendar.getInstance();
				
					
					rawParams.put("lastDate", String.valueOf(calendar.getTimeInMillis() - FIVE_DAY));
					
					String data = HttpUtil.postRequest(HttpUtil.BASEURL + "fetchNews", rawParams);
					
					LocalFileUtil.saveCommonCache("heartnews.dat", data);
					return JSONArray.parseArray(data,HeartNews.class);
					
				}
			});
			  
			 new Thread(task).start();
			 return task.get();
	}
}

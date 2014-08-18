package com.nwpu.heartwings.util;


import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.alibaba.fastjson.JSON;
import com.heart.bean.HeartMsg;
import com.heart.bean.HeartReturnBindMsg;
import com.nwpu.heartwings.app.HeartApp;

public class MinaClientHandler extends IoHandlerAdapter{

	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
	
		session.close(true);
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		
		// ���������Ϣ�߼�
		HeartMsg heartMsg = JSON.parseObject(message.toString(),HeartMsg.class);      
		switch (heartMsg.getMsgType()) {
		case MSGUtil.TYPE_POSITION:
/*		HeartPositionMsgWithWeather heartPositionMsgWithWeather = JSON.parseObject(message.toString(), HeartPositionMsgWithWeather.class);
		System.out.println(heartPositionMsgWithWeather.getArea() + " return area...");
		ShowMapActivity.handler.sendEmptyMessage(0x16);*/
			break;
		case MSGUtil.TYPE_BINDRETURN:
	
			HeartReturnBindMsg heartReturnBindMsg = JSON.parseObject(message.toString(), HeartReturnBindMsg.class);
			
			String yesorno = heartReturnBindMsg.getMsgContent();
			String title = "�󶨽��";
			String content = null;
			if(yesorno.equals("1"))
			{
				content = "��ϲ�㣬�Է��ѽ�����İ�����";
				
				// ����Ϣд�����ݿ⣬���ڱ���
				LocalFileUtil.savaBindInDB(heartReturnBindMsg);
				
			}else {
				
				content = "��Ǹ���Է��ܾ�����İ�����";
			}
			
			NotificationUtil.sendNotification(HeartApp.getInstance(), title, content);
			
		default:
			break;
		}
		
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
	  
		
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
	
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		
		 HeartMsg heartMsg = new HeartMsg();
		 heartMsg.setCreateTime(System.currentTimeMillis());
		 heartMsg.setFromUserName(session.getAttribute(MSGUtil.THISCLIENT).toString());
		 
		 heartMsg.setMsgType(MSGUtil.TYPE_HELLO);
         
		 session.write(JSON.toJSONString(heartMsg));
		 
	}

    
}

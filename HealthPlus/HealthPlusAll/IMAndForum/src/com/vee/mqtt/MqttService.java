package com.vee.mqtt;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class MqttService extends Service {
	public static final String ACTION_MESSAGE_ARRIVED = "com.vee.mqtt.action.MESSAGE_ARRIVED";
	
	private static final String NULL_TYPE = "NULL";
	private static final String TEXT_TYPE = "TEXT";
	private static final String IMAGE_TYPE = "IMAG";
	
	private MqttClient client;
	private MqttConnectOptions options;
	private String myTopic = null;
	private String host = "tcp://hp.mobifox.cn:9002";
	private String userName = "aaa";
	private String passWord = "bbb";
	private int userid = 0;
	private Handler handler;
	private ScheduledExecutorService scheduler;



	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		userid = intent.getIntExtra("userid", 0);
		myTopic = "healthplus/" + userid;

		init();

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what == 1) {
					// System.out.println((String) msg.toString());
					byte[] b = (byte[]) msg.obj;
					byte[] type = new byte[TEXT_TYPE.getBytes().length];
					System.arraycopy(b, 0, type, 0, type.length);
					String sType = NULL_TYPE;
					try {
						sType = new String(type, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("sType = " + sType);
					if (sType.compareTo(TEXT_TYPE) == 0) {
						byte[] textBody = new byte[b.length
								- TEXT_TYPE.getBytes().length];
						System.arraycopy(b, TEXT_TYPE.getBytes().length,
								textBody, 0, b.length
										- TEXT_TYPE.getBytes().length);
						try {
							String sTextBody = new String(textBody, "UTF-8");
							Intent intent = new Intent(MqttService.ACTION_MESSAGE_ARRIVED);
							intent.putExtra("textbody", textBody);
							sendOrderedBroadcast(intent, null);
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if (sType.compareTo(IMAGE_TYPE) == 0) {
						byte[] imageBody = new byte[b.length
								- IMAGE_TYPE.getBytes().length];
						System.arraycopy(b, IMAGE_TYPE.getBytes().length,
								imageBody, 0, b.length
										- IMAGE_TYPE.getBytes().length);
						Bitmap photo = BitmapFactory.decodeByteArray(imageBody,
								0, imageBody.length);
						Drawable drawable = new BitmapDrawable(photo);
					}
					/*
					 * Bitmap photo = BitmapFactory.decodeByteArray(b, 0,
					 * b.length); Drawable drawable = new BitmapDrawable(photo);
					 * iv.setBackgroundDrawable(drawable);
					 */
					/*
					 * Toast.makeText(MainActivity.this, (String) msg.obj,
					 * Toast.LENGTH_SHORT).show();
					 */
				} else if (msg.what == 2) {
					/*
					 * Toast.makeText(MainActivity.this, "连接成功",
					 * Toast.LENGTH_SHORT).show();
					 */
					try {
						client.subscribe(myTopic, 1);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (msg.what == 3) {

					/*
					 * Toast.makeText(MainActivity.this, (String)
					 * msg.toString(), Toast.LENGTH_SHORT).show();
					 */
					// Toast.makeText(MainActivity.this, "连接失败，系统正在重连",
					// Toast.LENGTH_SHORT).show();
				}
			}
		};

		startReconnect();

		return Service.START_REDELIVER_INTENT;
	}

	private void init() {
		try {
			// host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
			client = new MqttClient(host, String.valueOf(userid), new MemoryPersistence());
			// MQTT的连接设置
			options = new MqttConnectOptions();
			// 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
			options.setCleanSession(false);
			// 设置连接的用户名
			options.setUserName(userName);
			// 设置连接的密码
			options.setPassword(passWord.toCharArray());
			// 设置超时时间 单位为秒
			options.setConnectionTimeout(10);
			// 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
			options.setKeepAliveInterval(20);
			// 设置回调
			client.setCallback(new MqttCallback() {

				@Override
				public void connectionLost(Throwable cause) {
					// 连接丢失后，一般在这里面进行重连
					System.out.println("connectionLost----------");
				}

				@Override
				public void deliveryComplete(IMqttDeliveryToken token) {
					// publish后会执行到这里
					System.out.println("deliveryComplete---------"
							+ token.isComplete());
				}

				@Override
				public void messageArrived(String topicName, MqttMessage message)
						throws Exception {
					// subscribe后得到的消息会执行到这里面
					System.out.println("messageArrived----------");
					Message msg = new Message();
					msg.what = 1;
					// msg.obj = topicName + "---" + message.toString();
					msg.obj = message.getPayload();
					handler.sendMessage(msg);
				}
			});
			// connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void startReconnect() {
		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (!client.isConnected()) {
					connect();
				}
			}
		}, 0 * 1000, 10 * 1000, TimeUnit.MILLISECONDS);
	}

	private void connect() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					client.connect(options);
					Message msg = new Message();
					msg.what = 2;
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = new Message();
					msg.what = 3;
					handler.sendMessage(msg);
				}
			}
		}).start();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}

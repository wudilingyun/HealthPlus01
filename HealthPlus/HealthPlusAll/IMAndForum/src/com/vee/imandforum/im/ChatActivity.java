package com.vee.imandforum.im;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import MessageJsonBean.MessageObj;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.vee.imandforum.R;
import com.vee.mqtt.MqttService;
import com.yunfox.s4aservicetest.response.Friend;

public class ChatActivity extends Activity implements OnClickListener {
	private TextView header_text;
	private ImageView header_lbtn_img, header_rbtn_img;
	private Friend friend;
	private int myid;
	private static final String TEXT_TYPE = "TEXT";
	private static final String IMAGE_TYPE = "IMAG";
	private MqttClient client;
	private String host = "tcp://hp.mobifox.cn:9002";
	private String userName = "test";
	private String passWord = "test";
	private MqttTopic topic;
	private MqttMessage message;
	private String myTopic = "healthplus/0";

	private EditText editText_Chat_Input;
	private NewMessageReceiver newMessageReceiver = null;
	private ListView mMessageList;
	private ChatMessageListAdapter mAdapter;
	private List<MessageObj> mMessages = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);

		editText_Chat_Input = (EditText) findViewById(R.id.chat_input_input);

		Intent intent = getIntent();
		friend = (Friend) intent.getSerializableExtra("friend");
		myid = intent.getIntExtra("myid", 0);

		settitle(friend.getFriendname());
		myTopic = "healthplus/" + friend.getFriendid();
		userName = "server-" + friend.getFriendid();

		try {
			client = new MqttClient(host, userName, new MemoryPersistence());
			connect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// mqtt message receiver
		if (newMessageReceiver == null) {
			newMessageReceiver = new NewMessageReceiver();
		}
		IntentFilter filter = new IntentFilter();
		filter.addAction(MqttService.ACTION_MESSAGE_ARRIVED);
		filter.setPriority(100);
		registerReceiver(newMessageReceiver, filter);
		
		mMessageList = (ListView) findViewById(R.id.chat_message_list);

		ImageButton imageButtonSend = (ImageButton) findViewById(R.id.chat_input_send);
		imageButtonSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String text = editText_Chat_Input.getText().toString();
				message = new MqttMessage();
				message.setQos(1);
				message.setRetained(false);
				byte[] content2;
				try {
					content2 = text.getBytes("UTF-8");
					byte[] temp2 = new byte[content2.length
							+ TEXT_TYPE.getBytes("UTF-8").length];
					System.arraycopy(TEXT_TYPE.getBytes("UTF-8"), 0, temp2, 0,
							TEXT_TYPE.getBytes("UTF-8").length);
					System.arraycopy(content2, 0, temp2,
							TEXT_TYPE.getBytes("UTF-8").length, content2.length);
					message.setPayload(temp2);

					MqttDeliveryToken token;
					token = topic.publish(message);
					token.waitForCompletion();
					System.out.println(token.isComplete() + "========");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MqttPersistenceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MqttException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	private void connect() {

		MqttConnectOptions options = new MqttConnectOptions();
		options.setCleanSession(false);
		options.setUserName(userName);
		options.setPassword(passWord.toCharArray());
		// 设置超时时间
		options.setConnectionTimeout(10);
		// 设置会话心跳时间
		options.setKeepAliveInterval(20);
		try {
			client.setCallback(new MqttCallback() {

				@Override
				public void connectionLost(Throwable cause) {
					System.out.println("connectionLost-----------");
				}

				@Override
				public void deliveryComplete(IMqttDeliveryToken token) {
					System.out.println("deliveryComplete---------"
							+ token.isComplete());
				}

				@Override
				public void messageArrived(String topic, MqttMessage arg1)
						throws Exception {
					System.out.println("messageArrived----------");

				}
			});

			topic = client.getTopic(myTopic);

			client.connect(options);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	void settitle(String friendname) {
		View headView = findViewById(R.id.chat_title);
		header_text = (TextView) headView.findViewById(R.id.header_text);
		header_lbtn_img = (ImageView) headView
				.findViewById(R.id.header_lbtn_img);
		header_rbtn_img = (ImageView) headView
				.findViewById(R.id.header_rbtn_img);
		header_text.setText(friendname);
		header_lbtn_img.setOnClickListener(this);
		header_rbtn_img.setVisibility(View.GONE);

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.header_lbtn_img:
			// Toast.makeText(getApplicationContext(), "clicked",
			// Toast.LENGTH_SHORT).show();
			this.finish();
			break;

		default:
			break;
		}
	}

	private class NewMessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			byte[] textBody = intent.getByteArrayExtra("textbody");
			try {
				String sTextBody = new String(textBody, "UTF-8");
				System.out.println("broadcast message..." + sTextBody);
				MessageObj message = new MessageObj();
				message.from = "dfdf";
				message.text = sTextBody;
				addMessageToList(message);
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void addMessageToList(MessageObj message) {
		if (mMessages == null) {
			mMessages = new ArrayList<MessageObj>();
			mAdapter = new ChatMessageListAdapter(this, mMessages, String.valueOf(myid));
			mMessageList.setAdapter(mAdapter);
		}
		mMessages.add(message);
		mAdapter.notifyDataSetChanged();
		mMessageList.setSelection(mAdapter.getCount() - 1);
	}
}

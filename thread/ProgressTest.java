package org.sp.app0712.thread;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

//진행상황을 직관적으로 표현해 주는 컴포넌트인 JProgressbar를 사용해보되
//쓰레드를 이용하여 서로 다른 속도와 크기로 동작하도록 해보자(독립적으로 동작되게 처리)
public class ProgressTest extends JFrame{
	JProgressBar bar1;
	JProgressBar bar2;
	JProgressBar bar3;
	JButton bt;
	int n;
	MyThread t1,t2,t3;

	
	public ProgressTest() {
		bar1 = new JProgressBar();
		bar2 = new JProgressBar();
		bar3 = new JProgressBar();
		
		bt = new JButton("시작");
		
		
		//스타일
		bar1.setPreferredSize(new Dimension(480,50));
		bar2.setPreferredSize(new Dimension(480,50));
		bar3.setPreferredSize(new Dimension(480,50));
		
		setLayout(new FlowLayout());
		
		add(bar1);
		add(bar2);
		add(bar3);
		add(bt);
		
		setSize(500,240);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//버튼에 리스너연결
		bt.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				//3개의 쓰레드 생성 및 동작시작
				t1 = new MyThread(1,bar1);
				t2 = new MyThread(10, bar2);
				t3 = new MyThread(100, bar3);
				
				t1.start();
				t2.start();
				t3.start();
			}
		});
	}
	

	
	public void increase() {
		n++;
		bar1.setValue(n);
	
	}
	
	public static void main(String[] args) {
		new ProgressTest();
	}
}

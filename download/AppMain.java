package org.sp.app0712.download;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

public class AppMain extends JFrame implements ActionListener{
	JTextField t_url;//수집하고 싶은 자원의 주소를 넣는 영역
	JButton bt_dest;//어디로 저장할지
	JLabel la_dest;//어느 경로로 저장할지 정보를 출력할 라벨
	JButton bt_down;//다운로드 실행 버튼
	JProgressBar bar;
	JFileChooser chooser;
	Thread thread;//다운로드와 프로그래스바 처리를 담당할 쓰레드
	
	public AppMain() {
		t_url = new JTextField();
		bt_dest = new JButton("저장할 위치");
		la_dest = new JLabel("저장할 위치를 등록해 주세요");
		bt_down = new JButton("DownLoad");
		bar = new JProgressBar();
		chooser = new JFileChooser();
		
		//스타일
		t_url.setPreferredSize(new Dimension(680, 50));
		la_dest.setPreferredSize(new Dimension(550, 50));
		bar.setPreferredSize(new Dimension(680, 60));
		bar.setStringPainted(true);
		bar.setFont(new Font("돋움", Font.BOLD|Font.ITALIC ,25 ));
		
		setLayout(new FlowLayout());
		
		add(t_url);
		add(bt_dest);
		add(la_dest);
		add(bt_down);
		add(bar);
		
		
		setSize(700, 250);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		bt_dest.addActionListener(this);
		bt_down.addActionListener(this);
		
	}
	
	public void setDest() {
		int result=chooser.showSaveDialog(this);
		if(result==JFileChooser.APPROVE_OPTION) {
			File file=chooser.getSelectedFile();
			
			System.out.println(file.getAbsolutePath());
			la_dest.setText(file.getAbsolutePath());
		}
	}
	
	public int getTotalByte(URL url) {
		//넘겨받은 스트림을 이용하여 구성하고 있는 알갱이 세기
		InputStream is=null;
		int data=-1;
		int readCount=0;
		
		try {
			is = url.openStream();
			while(true) {
				try {
					data=is.read();
					if(data==-1)break;
					readCount++;
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(is!=null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		System.out.println("읽어들인 바이트 수는 "+readCount);
		return readCount;
	}
	
	//인터넷상의 자원을 대상으로 빨대를 꽂아 실행중인 프로그램으로 들이마시기
	//동시에 지정한 경로로 데이터 출력
	public void download() {
		
		InputStream is=null;
		FileOutputStream fos=null;
		try {
			URL url= new URL(t_url.getText());
			is=url.openStream();
			fos=new FileOutputStream(la_dest.getText());
			
			//1바이트씩 읽어 파일에 출력해 본다.
			int data=-1;
			//생성되 스트림으로 읽어들일 수 있는 총 바이트 수(용량)
			int total=getTotalByte(url);
			System.out.println("스트림으로 읽어들일 총 바이트 수는"+total);
			
			int readCount=0;
			double ratio=0;
			
			//백분율 계산하기
			
			while(true) {
				data=is.read();//1바이트 읽기
				if(data==-1)break;
				readCount++;
				
				ratio=(readCount/(double)total)*100;
				//System.out.println(ratio);
				bar.setValue((int)ratio);
				
				fos.write(data);//1바이트 쓰기
			}
			JOptionPane.showMessageDialog(this, "다운로드 완료");
			
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(fos!=null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(is!=null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	
	}
	
	public void actionPerformed(ActionEvent e) {
		Object obj=e.getSource();
		
		if(obj==bt_dest) {
			setDest();
		}else if(obj==bt_down) {
			thread = new Thread() {
				public void run() {
					
					download();
				}
			};
			thread.start();

		}
	}
	
	
	public static void main(String[] args) {
		new AppMain();
	}
}

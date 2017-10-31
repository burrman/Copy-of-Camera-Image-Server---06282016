package app;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import java.awt.Color;

import javax.swing.border.LineBorder;
import javax.swing.JLabel;

import java.awt.EventQueue;
import java.awt.SystemColor;
import java.awt.Font;

import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Timer;
import java.awt.ScrollPane;

import javax.swing.SwingConstants;
import javax.swing.UIManager;

import fileHandling.Result_Data;
import fileHandlingTXT.TXT_File_Data;

public class MainView extends JFrame {
	
	// class variable declarations
	private static final long serialVersionUID = 13L;

	private static String fromDirPass = "Please enter a source directory for passing images here";
	private static String fromDirFail = "Please enter a source directory for failing images here";
	private static String toDir = "Please enter a target directory for processed images here";

	private static String server = "";
	private static String user = "";
	private static String password = "";
	private static String fileName_FTP = "";

	private static Task_MonitorFolder task_FolderMonitor = new Task_MonitorFolder();

	private static Task_ArchiveCleanup task_ArchiveCleanup = new Task_ArchiveCleanup();

	private JPanel contentPane;
	private static JTextField textField;
	private static JTextField textField_1;
	private static JTextField textField_2;
	private static JTextArea textArea_log;

	private static JTextField textField_IP;
	private static JTextField textField_user;
	private static JTextField textField_password;

	private static JButton btnLoadSetup;
	private static JButton btnSaveSetup;
	private JButton btnClearLog;

	private static JPanel panel_logStatus;
	private static JLabel label_logStatus;

	private static JPanel panel_runStatus;
	private static JLabel label_runStatus;

	private static JButton button_start;

	// create a timer that will be used later to trigger a periodic task
	private static Timer timer_FolderMonitor = new Timer();

	// create a timer that will be used later to trigger a periodic task
	private static Timer timer_ArchiveCleanup = new Timer();

	// class constructor - builds the GUI interface
	public MainView() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1023, 710);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(51, 0, 153));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setForeground(Color.BLACK);
		panel.setBorder(new LineBorder(new Color(255, 255, 255)));
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setBounds(10, 11, 615, 264);
		contentPane.add(panel);

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPassword.setFont(new Font("Dialog", Font.BOLD, 12));
		lblPassword.setBackground(SystemColor.menu);
		lblPassword.setBounds(133, 198, 84, 23);
		panel.add(lblPassword);

		JLabel label = new JLabel("Source For Passing Images:");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setFont(new Font("Dialog", Font.BOLD, 12));
		label.setBackground(SystemColor.menu);
		label.setBounds(0, 39, 217, 23);
		panel.add(label);

		JLabel label_1 = new JLabel("Source For Failing Images:");
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		label_1.setFont(new Font("Dialog", Font.BOLD, 12));
		label_1.setBackground(SystemColor.menu);
		label_1.setBounds(0, 70, 217, 23);
		panel.add(label_1);

		JLabel label_2 = new JLabel("Destination For All Images:");
		label_2.setHorizontalAlignment(SwingConstants.RIGHT);
		label_2.setFont(new Font("Dialog", Font.BOLD, 12));
		label_2.setBackground(SystemColor.menu);
		label_2.setBounds(0, 99, 217, 23);
		panel.add(label_2);

		textField = new JTextField();
		textField.setText("Source For Passing Images");
		textField.setFont(new Font("Dialog", Font.BOLD, 12));
		textField.setColumns(10);
		textField.setBounds(224, 39, 381, 20);
		panel.add(textField);

		textField_1 = new JTextField();
		textField_1.setText("Source For Failing Images");
		textField_1.setFont(new Font("Dialog", Font.BOLD, 12));
		textField_1.setColumns(10);
		textField_1.setBounds(224, 70, 381, 20);
		panel.add(textField_1);

		textField_2 = new JTextField();
		textField_2.setText("Target For All Processed Images");
		textField_2.setFont(new Font("Dialog", Font.BOLD, 12));
		textField_2.setColumns(10);
		textField_2.setBounds(224, 100, 381, 20);
		panel.add(textField_2);

		JLabel label_3 = new JLabel("System Directory Paths Can Be Copied/Pasted Into The Fields Below:");
		label_3.setFont(new Font("Dialog", Font.BOLD, 12));
		label_3.setBounds(10, 11, 387, 14);
		panel.add(label_3);

		btnLoadSetup = new JButton("Load Setup");
		btnLoadSetup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadSetupFile();
			}
		});
		btnLoadSetup.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnLoadSetup.setBounds(294, 230, 152, 23);
		panel.add(btnLoadSetup);

		btnSaveSetup = new JButton("Save Setup");
		btnSaveSetup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveSetupFile();
			}
		});
		btnSaveSetup.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnSaveSetup.setBounds(456, 230, 149, 23);
		panel.add(btnSaveSetup);

		JLabel lblHmiFtpServer = new JLabel("Remote FTP Server IP Address:");
		lblHmiFtpServer.setHorizontalAlignment(SwingConstants.RIGHT);
		lblHmiFtpServer.setFont(new Font("Dialog", Font.BOLD, 12));
		lblHmiFtpServer.setBackground(SystemColor.menu);
		lblHmiFtpServer.setBounds(0, 133, 217, 23);
		panel.add(lblHmiFtpServer);

		textField_IP = new JTextField();
		textField_IP.setText("IP Address");
		textField_IP.setFont(new Font("Dialog", Font.BOLD, 12));
		textField_IP.setColumns(10);
		textField_IP.setBounds(224, 134, 381, 20);
		panel.add(textField_IP);

		JLabel lblProfaeHmiFtp = new JLabel("Remote FTP Server User Name:");
		lblProfaeHmiFtp.setHorizontalAlignment(SwingConstants.RIGHT);
		lblProfaeHmiFtp.setFont(new Font("Dialog", Font.BOLD, 12));
		lblProfaeHmiFtp.setBackground(SystemColor.menu);
		lblProfaeHmiFtp.setBounds(0, 167, 217, 23);
		panel.add(lblProfaeHmiFtp);

		textField_user = new JTextField();
		textField_user.setText("User");
		textField_user.setFont(new Font("Dialog", Font.BOLD, 12));
		textField_user.setColumns(10);
		textField_user.setBounds(224, 168, 381, 20);
		panel.add(textField_user);

		textField_password = new JTextField();
		textField_password.setText("Password");
		textField_password.setFont(new Font("Dialog", Font.BOLD, 12));
		textField_password.setColumns(10);
		textField_password.setBounds(224, 199, 381, 20);
		panel.add(textField_password);

		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBorder(new LineBorder(new Color(255, 255, 255)));
		panel_1.setBackground(Color.LIGHT_GRAY);
		panel_1.setBounds(10, 286, 987, 375);
		contentPane.add(panel_1);

		JLabel lblAlarmLog = new JLabel("Alarm Log:");
		lblAlarmLog.setFont(new Font("Dialog", Font.BOLD, 12));
		lblAlarmLog.setBackground(SystemColor.menu);
		lblAlarmLog.setBounds(10, 7, 80, 23);
		panel_1.add(lblAlarmLog);

		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setBackground(Color.LIGHT_GRAY);
		scrollPane.setBounds(10, 36, 967, 329);
		textArea_log = new JTextArea();
		scrollPane.add(textArea_log);
		panel_1.add(scrollPane);

		btnClearLog = new JButton("Clear Log");
		btnClearLog.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnClearLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearLog();
			}
		});
		btnClearLog.setBounds(852, 8, 125, 23);
		panel_1.add(btnClearLog);

		panel_logStatus = new JPanel();
		panel_logStatus.setBackground(UIManager.getColor("CheckBox.light"));
		panel_logStatus.setBounds(270, 7, 286, 23);
		panel_1.add(panel_logStatus);
		panel_logStatus.setLayout(null);

		label_logStatus = new JLabel("Log Paused");
		label_logStatus.setHorizontalAlignment(SwingConstants.CENTER);
		label_logStatus.setFont(new Font("Tahoma", Font.BOLD, 12));
		label_logStatus.setBounds(10, 5, 266, 14);
		panel_logStatus.add(label_logStatus);

		JButton btnStopLogging = new JButton("Pause Logging");
		btnStopLogging.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopLogging();
			}
		});
		btnStopLogging.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnStopLogging.setBounds(717, 8, 125, 23);
		panel_1.add(btnStopLogging);

		JButton btnStartLogging = new JButton("Start Logging");
		btnStartLogging.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startLogging();
			}
		});
		btnStartLogging.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnStartLogging.setBounds(582, 8, 125, 23);
		panel_1.add(btnStartLogging);

		panel_runStatus = new JPanel();
		panel_runStatus.setBackground(new Color(153, 51, 0));
		panel_runStatus.setBounds(635, 11, 177, 163);
		contentPane.add(panel_runStatus);
		panel_runStatus.setLayout(null);

		label_runStatus = new JLabel("Not Running");
		label_runStatus.setHorizontalAlignment(SwingConstants.CENTER);
		label_runStatus.setFont(new Font("Tahoma", Font.BOLD, 16));
		label_runStatus.setBounds(10, 73, 157, 14);
		panel_runStatus.add(label_runStatus);

		button_start = new JButton("Start");
		button_start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				operate();
			}
		});
		button_start.setFont(new Font("Tahoma", Font.BOLD, 16));
		button_start.setBounds(822, 11, 175, 163);
		contentPane.add(button_start);

		// start the program out with the activity log running
		startLogging();

	} // end MainView class constructor

	// entry point - builds the GUI thread and displays the GUI object
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {

			public void run() {

				try {
					
					MainView frame = new MainView();
					frame.setVisible(true);
					frame.setResizable(false);
					frame.setTitle("Senso Part Folder Monitor With Web Server FTP - version: "
							+ Long.toString(serialVersionUID));

					loadSetupFile();
					operate();

				}

				catch (Exception e) {
					
					e.printStackTrace();
					
				} // end try/catch

			} // end run()

		}); // end invokeLater() "runnable" method declaration

	} // end main()

	// initiate the operation of file monitoring when the start button is
	// clicked in the GUI
	private static void operate() {
		if (checkAllDirectories() == true) {
			task_FolderMonitor.setFromDirPass(fromDirPass);
			task_FolderMonitor.setFromDirFail(fromDirFail);
			task_FolderMonitor.setToDir(toDir);

			textField.setEnabled(false);
			textField_1.setEnabled(false);
			textField_2.setEnabled(false);

			btnLoadSetup.setEnabled(false);
			btnSaveSetup.setEnabled(false);
			button_start.setEnabled(false);

			textField_IP.setEnabled(false);
			textField_user.setEnabled(false);
			textField_password.setEnabled(false);

			System.out.println("Directory processing has been started! \n \n");
			textArea_log.append("Directory processing has been started! \n \n");

			label_runStatus.setText("Running");
			panel_runStatus.setBackground(Color.green);

			scheduleTask_FolderMonitor();

			// testing the folder cleanup routine
			task_ArchiveCleanup.setToDir(toDir);
			scheduleTask_ArchiveCleanup();

		} // end if check for valid directories

	} // end operate()

	// schedule the periodic task that will check the directories for files
	private static void scheduleTask_FolderMonitor() {
		
		// configure the task's parameters
		task_FolderMonitor.setServerInfo(server, user, password, fileName_FTP);
		
		// trigger the execution of the periodic task on a fixed time interval in mSeconds
		timer_FolderMonitor.schedule(task_FolderMonitor, 0, 200);

	} // end scheduleTask_FolderMonitor()

	// schedule the periodic task that will clean up the archive files
	private static void scheduleTask_ArchiveCleanup() {
				
		// calculate an interval in mSeconds for 4 hours
		int interval = 4 * 60 * 60 * 1000;
		
		// trigger the execution of the periodic task on a fixed time interval in mSeconds
		timer_ArchiveCleanup.schedule(task_ArchiveCleanup, 0, interval);

	} // end scheduleTask_FolderMonitor()

	//
	// below are supporting methods
	//

	// verify that all of the required file directories are valid before
	// starting the scheduled task
	private static boolean checkAllDirectories() {

		boolean result = false;

		Result_Data tempResultDataPass = new Result_Data();
		Result_Data tempResultDataFail = new Result_Data();
		Result_Data tempResultDataTo = new Result_Data();

		fromDirPass = textField.getText();

		tempResultDataPass = fileHandling.DirectoryManagement.CheckDirectory(fromDirPass);

		System.out.println(tempResultDataPass.text);
		textArea_log.append(tempResultDataPass.text);

		fromDirFail = textField_1.getText();

		tempResultDataFail = fileHandling.DirectoryManagement.CheckDirectory(fromDirFail);

		System.out.println(tempResultDataFail.text);
		textArea_log.append(tempResultDataFail.text);

		toDir = textField_2.getText();

		tempResultDataTo = fileHandling.DirectoryManagement.CheckDirectory(toDir);

		System.out.println(tempResultDataTo.text);
		textArea_log.append(tempResultDataTo.text);

		if (tempResultDataPass.result == true && tempResultDataFail.result == true && tempResultDataTo.result == true) {
			result = true;
		}

		else {
			System.out.println(
					"One or more of the target or source directories is not valid, please correct and verify all directories and press start to proceed.  \n");
			textArea_log.append(
					"One or more of the target or source directories is not valid, please correct and verify all directories and press start to proceed.  \n");
		}

		return result;

	} // end checkAllDirectories()

	// load the file directories from a saved TXT file on the PC
	private static boolean loadSetupFile() {
		boolean result = false;

		TXT_File_Data readSetupFileResult = new TXT_File_Data();

		readSetupFileResult = fileHandlingTXT.TXT_FileManagement.Read_TXT("Setup.txt");

		System.out.println(readSetupFileResult.text);
		textArea_log.append(readSetupFileResult.text);

		if (readSetupFileResult.result == true) {

			fromDirPass = readSetupFileResult.content[0];
			fromDirFail = readSetupFileResult.content[1];
			toDir = readSetupFileResult.content[2];

			server = readSetupFileResult.content[3];
			user = readSetupFileResult.content[4];
			password = readSetupFileResult.content[5];
			fileName_FTP = readSetupFileResult.content[6];

			textField.setText(fromDirPass);
			textField_1.setText(fromDirFail);
			textField_2.setText(toDir);

			textField_IP.setText(server);
			textField_user.setText(user);
			textField_password.setText(password);

		}
		return result;
	}

	// save/create a TXT file that contains the file directories on the PC
	private static boolean saveSetupFile() {
		boolean result = false;

		TXT_File_Data loadSetupFileData = new TXT_File_Data();
		TXT_File_Data loadSetupFileResult = new TXT_File_Data();

		fromDirPass = textField.getText();
		fromDirFail = textField_1.getText();
		toDir = textField_2.getText();

		server = textField_IP.getText();
		user = textField_user.getText();
		password = textField_password.getText();

		loadSetupFileData.content[0] = fromDirPass;
		loadSetupFileData.content[1] = fromDirFail;
		loadSetupFileData.content[2] = toDir;

		loadSetupFileData.content[3] = server;
		loadSetupFileData.content[4] = user;
		loadSetupFileData.content[5] = password;
		loadSetupFileData.content[6] = fileName_FTP;

		loadSetupFileResult = fileHandlingTXT.TXT_FileManagement.Write_TXT(loadSetupFileData, "Setup.txt");

		System.out.println(loadSetupFileResult.text);
		textArea_log.append(loadSetupFileResult.text);

		return result;
	}

	private void startLogging() {
		task_FolderMonitor.startActivityLog();
		task_ArchiveCleanup.startActivityLog();

		System.out.println("Activity Log Started! \n \n");
		textArea_log.append("Activity Log Started! \n \n");

		label_logStatus.setText("Log Running");
		panel_logStatus.setBackground(Color.green);
		
		activityLogEnabled = true;
	}

	private void stopLogging() {
		task_FolderMonitor.stopActivityLog();
		task_ArchiveCleanup.stopActivityLog();

		System.out.println("Activity Log Paused! \n \n");
		textArea_log.append("Activity Log Paused! \n \n");

		label_logStatus.setText("Log Paused");
		panel_logStatus.setBackground(UIManager.getColor("CheckBox.light"));
		
		activityLogEnabled = false;
	}

	private void clearLog() {
		System.out.println("Activity Log Cleared! \n \n");
		textArea_log.setText("");
	}

	public static void updateLog_FromFolderMonitor() {
		textArea_log.append(task_FolderMonitor.getLastResultText());
	}

	public static void updateLog_FromArchiveCleanup() {
		textArea_log.append(task_ArchiveCleanup.getLastResultText());
	}
	
	public static void updateLog(String text)
	{
		if (activityLogEnabled == true)
		{
			textArea_log.append(text);
		}
	}
	
	private static boolean activityLogEnabled = false;
}// end MainView class

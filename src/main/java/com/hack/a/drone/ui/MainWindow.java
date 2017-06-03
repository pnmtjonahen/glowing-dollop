package com.hack.a.drone.ui;

import com.hack.a.drone.CX10;
import com.hack.a.drone.io.controls.Keyboard;
import com.hack.a.drone.io.controls.XInput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MainWindow extends JFrame implements ActionListener {
    private final CX10 cx10;

    private JButton btnConnect;
    private JButton btnControls;
    private JButton btnVideo;
    private JButton btnRecord;
    private JLabel lblStatus;
    private JPanel panel;
    private JRadioButton radioController;
    private JRadioButton radioKeyboard;
    private JRadioButton radioWindows;
    private JRadioButton radioLinux;

    private boolean isConnected = false;
    private boolean isRecording = false;
    private boolean isPlaying = false;
    private boolean isControlled = false;
    private Keyboard keyboard;

    public MainWindow() {
        this.cx10 = new CX10();
        btnConnect.setEnabled(true);
        btnControls.setEnabled(false);
        btnVideo.setEnabled(false);
        btnRecord.setEnabled(false);

        btnConnect.addActionListener(this);
        btnControls.addActionListener(this);
        btnVideo.addActionListener(this);
        btnRecord.addActionListener(this);

        add(panel);
        pack();
        setTitle("CX-10WD Cockpit");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        setLocationByPlatform(true);
        panel.setVisible(true);
        setVisible(true);
    }

    public void actionPerformed(final ActionEvent e) {
        new Thread(new Runnable() {
            public void run() {
                if (e.getSource() == btnConnect) {
                    onConnectClicked();
                }
                
            }
        }).start();
    }

    private void onConnectClicked() {
        MainWindowModel model;
        if (!isConnected) {
            try {
                model = getModel();
                model.setBtnConnectEnabled(false);
                model.setBtnConnectText("Connecting...");

                setModel(model);

                cx10.connect();
                isConnected = true;

                model = getModel();
                model.setBtnConnectEnabled(true);
                model.setBtnConnectText("Disconnect");
                model.setBtnRecordEnabled(true);
                model.setBtnVideoEnabled(true);
                model.setBtnRecordEnabled(true);
                model.setBtnControlsEnabled(true);

                setModel(model);
            } catch (IOException e) {
                e.printStackTrace();

                model = getModel();
                model.setLblStatusText(e.getMessage());
                model.setBtnConnectEnabled(true);
                model.setBtnConnectText("Connect");
                setModel(model);
            }
        } else {
            cx10.disconnect();
            isConnected = false;

            model = getModel();
            model.setBtnConnectEnabled(true);
            model.setBtnConnectText("Connect");
            model.setBtnRecordEnabled(false);
            model.setBtnVideoEnabled(false);
            model.setBtnRecordEnabled(false);
            model.setBtnControlsEnabled(false);

            setModel(model);
        }

    }

    private void onRecordClicked() {
        MainWindowModel model;
        if (!isRecording) {
            try {
                model = getModel();
                model.setBtnRecordText("Init Recording...");
                model.setBtnRecordEnabled(false);
                setModel(model);

                cx10.startVideoRecorder();
                isRecording = true;

                model = getModel();
                model.setBtnRecordText("Stop Recording");
                model.setBtnRecordEnabled(true);

                setModel(model);
            } catch (IOException e) {
                lblStatus.setText(e.getMessage());

                model = getModel();
                model.setLblStatusText(e.getMessage());
                model.setBtnRecordEnabled(true);
                model.setBtnRecordText("Record Video");

                setModel(model);
            }
        } else {
            cx10.stopVideoRecorder();
            isRecording = false;

            model = getModel();
            model.setBtnRecordText("Record Video");

            setModel(model);
        }
    }

    private void onStartVideoStreamClicked() {
        MainWindowModel model;
        if (!isPlaying) {
            try {
                model = getModel();
                model.setBtnVideoText("Init Video...");
                model.setBtnVideoEnabled(false);
                setModel(model);

                cx10.startVideoStream((radioWindows.isSelected())?"win":"lin");
                isPlaying = true;

                model = getModel();
                model.setBtnVideoText("Stop Video");
                model.setBtnVideoEnabled(true);
                setModel(model);
            } catch (IOException e) {
                e.printStackTrace();
                model = getModel();
                model.setLblStatusText(e.getMessage());
                model.setBtnVideoEnabled(true);
                model.setBtnVideoText("Start Video");
                setModel(model);
            }
        } else {
            cx10.stopVideoStream();
            isPlaying = false;

            model = getModel();
            model.setBtnVideoText("Start Video");

            setModel(model);
        }
    }

    private void onControlsClicked() {
        MainWindowModel model;
        if (!isControlled) {
            try {
                if (radioKeyboard.isSelected()) {
                    keyboard = new Keyboard(KeyboardFocusManager.getCurrentKeyboardFocusManager());
                    cx10.startControls(keyboard);
                } else if (radioController.isSelected()) {
                    cx10.startControls(new XInput());
                } else {
                    return;
                }
                isControlled = true;

                model = getModel();
                model.setBtnControlsText("Stop Controller");
                setModel(model);

            } catch (IOException e) {
                e.printStackTrace();
                model = getModel();
                model.setLblStatusText(e.getMessage());
                setModel(model);
            }

        } else {
            cx10.stopControls();
            isControlled = false;

            model = getModel();
            model.setBtnControlsText("Start Controller");
            setModel(model);
        }
    }

    private MainWindowModel getModel() {
        MainWindowModel model = new MainWindowModel();
        model.setBtnConnectEnabled(btnConnect.isEnabled());

        model.setBtnConnectText(btnConnect.getText());
        model.setLblStatusText(lblStatus.getText());
        return model;
    }

    private void setModel(final MainWindowModel model) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                btnConnect.setEnabled(model.isBtnConnectEnabled());
                btnConnect.setText(model.getBtnConnectText());


                lblStatus.setText(model.getLblStatusText());
            }
        });
    }

    private void createUIComponents() {
    }
}

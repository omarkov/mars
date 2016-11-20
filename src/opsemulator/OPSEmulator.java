// -*- Mode: Java; indent-tabs-mode: nil -*-
//
// Copyright (c) 2004, Oliver Markovic <entrox@entrox.org> 
//   All rights reserved. 
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
//  o Redistributions of source code must retain the above copyright notice,
//    this list of conditions and the following disclaimer. 
//  o Redistributions in binary form must reproduce the above copyright
//    notice, this list of conditions and the following disclaimer in the
//    documentation and/or other materials provided with the distribution. 
//  o Neither the name of the author nor the names of the contributors may be
//    used to endorse or promote products derived from this software without
//    specific prior written permission. 
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

package opsemulator;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import net.*;

public class OPSEmulator extends JPanel implements ListSelectionListener {
    private NetworkFactory net;

    private JList list;
    private DefaultListModel listModel;

    private JButton leaveButton;
    private JTextField tagID;

    private void initNetwork()
    {
        // initialize network
        net = NetworkFactory.getInstance();

        ModuleDescription module = new ModuleDescription("OPS", "V1.0");
        module.setWebModule(false);

        net.setModuleDescription(module);
        net.startModule("localhost", 1234);

        // create login system "OPS"
        try {
            net.call("system", "createLogInSystem", new Object[]{"OPS"});
        } catch (NetworkException ex) {
            JOptionPane.showMessageDialog(this,
                                          ex.getMessage(),
                                          "Network error",
                                          JOptionPane.ERROR_MESSAGE);
        }
    }

    public OPSEmulator() {
        super(new BorderLayout());

        initNetwork();

        listModel = new DefaultListModel();

        // create the list and put it in a scroll pane.
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(5);
        JScrollPane listScrollPane = new JScrollPane(list);

        JButton enterButton = new JButton("Enter");
        EnterListener enterListener = new EnterListener(enterButton, this);
        enterButton.setActionCommand("Enter");
        enterButton.addActionListener(enterListener);

        leaveButton = new JButton("Leave");
        leaveButton.setActionCommand("Leave");
        leaveButton.addActionListener(new LeaveListener(this));
        leaveButton.setEnabled(false);

        tagID = new JTextField(10);
        tagID.addActionListener(enterListener);
        tagID.getDocument().addDocumentListener(enterListener);
        //String name = listModel.getElementAt(list.getSelectedIndex()).toString();

        //Create a panel that uses BoxLayout.
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                                           BoxLayout.LINE_AXIS));
        buttonPane.add(leaveButton);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(tagID);
        buttonPane.add(enterButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.PAGE_END);
    }

    class LeaveListener implements ActionListener {
        private JPanel frame;

        public LeaveListener(JPanel frame) {
            this.frame = frame;
        }

        public void actionPerformed(ActionEvent e) {
            int index = list.getSelectedIndex();
            String name = (String)listModel.getElementAt(index);

            listModel.remove(index);

            int size = listModel.getSize();

            if (size == 0) {
                leaveButton.setEnabled(false);
            } else {
                if (index == listModel.getSize())
                    index--;

                list.setSelectedIndex(index);
                list.ensureIndexIsVisible(index);
            }

            // call network
            try {
                NetworkFactory.getInstance().call("system", "exitRoom", new Object[]{"OPS", name});
            } catch (NetworkException ex) {
                JOptionPane.showMessageDialog(frame,
                                              ex.getMessage(),
                                              "Network error",
                                              JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    class EnterListener implements ActionListener, DocumentListener {
        private boolean alreadyEnabled = false;
        private JButton button;
        private JPanel frame;

        public EnterListener(JButton button, JPanel frame) {
            this.button = button;
            this.frame = frame;
        }

        // required by ActionListener.
        public void actionPerformed(ActionEvent e) {
            String name = tagID.getText();

            // check tag id
            if (name.equals("") || listModel.contains(name)) {
                Toolkit.getDefaultToolkit().beep();
                tagID.requestFocusInWindow();
                tagID.selectAll();
                return;
            }

            int index = list.getSelectedIndex(); // get selected index
            if (index == -1)
                index = 0;
            else
                index++;

            listModel.insertElementAt(tagID.getText(), index);

            // reset the text field.
            tagID.requestFocusInWindow();
            tagID.setText("");

            // select the new item and make it visible.
            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);

            // call network
            try {
                NetworkFactory.getInstance().call("system", "enterRoom", new Object[]{"OPS", name});
            } catch (NetworkException ex) {
                JOptionPane.showMessageDialog(frame,
                                              ex.getMessage(),
                                              "Network error",
                                              JOptionPane.ERROR_MESSAGE);
            }
        }

        // required by DocumentListener.
        public void insertUpdate(DocumentEvent e)
        {
            enableButton();
        }

        // required by DocumentListener.
        public void removeUpdate(DocumentEvent e)
        {
            handleEmptyTextField(e);
        }

        // required by DocumentListener.
        public void changedUpdate(DocumentEvent e)
        {
            if (!handleEmptyTextField(e))
                enableButton();
        }

        private void enableButton()
        {
            if (!alreadyEnabled)
                button.setEnabled(true);
        }

        private boolean handleEmptyTextField(DocumentEvent e)
        {
            if (e.getDocument().getLength() <= 0) {
                button.setEnabled(false);
                alreadyEnabled = false;
                return true;
            }

            return false;
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting())
            return;

        // enable leave button only if something is selected
        if (list.getSelectedIndex() == -1)
            leaveButton.setEnabled(false);
        else
            leaveButton.setEnabled(true);
    }


    private static void createAndShowGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);

        JFrame frame = new JFrame("OPSEmulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JComponent pane = new OPSEmulator();
        pane.setOpaque(true);
        frame.setContentPane(pane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        System.out.println("start");
//        javax.swing.SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
                createAndShowGUI();
//            }
//        });
    }
}

package com.testingblaze.misclib;

import com.testingblaze.register.I;
import com.testingblaze.report.LogLevel;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.SubjectTerm;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author jitendra.pisal
 */
public final class Emails {
    private Folder folder;
    private Properties properties = new Properties();

    public enum EmailFolder {
        INBOX("INBOX"),
        SPAM("SPAM");

        private String text;

        EmailFolder(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }


    public enum HostName {
        GMAIL("smtp.gmail.com"),
        OUTLOOK("imap-mail.outlook.com");

        String text;

        HostName(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }


    public enum MailClient {
        GMAIL("GMAIL"),
        OUTLOOK("OUTLOOK");

        private String text;

        MailClient(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }


    /**
     * Connects to email server with credentials provided to read from a given folder of the email application
     *
     * @param username    Email username (e.g. jitendra.pisal@gmail.com)
     * @param password    Email password
     * @param server      Email server (e.g. smtp.email.com)
     * @param mailClient  Mailing Client (GMAIL, OUTLOOK)
     * @param emailFolder Folder in email application to interact with
     * @author jitendra.pisal
     */
    public EmailMethods accessEmail(String username, String password, HostName server, MailClient mailClient, EmailFolder emailFolder) {
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, "Accessing mail " + mailClient);
        try {
            Properties props = getMailClientProp(mailClient);
            Session session = Session.getInstance(props);
            Store store = session.getStore("imaps");
            store.connect(server.getText(), username, password);
            folder = store.getFolder(emailFolder.getText());
            folder.open(Folder.READ_WRITE);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return new EmailMethods();
    }


    /**
     * @param mailClient (e.g GMAIL, OUTLOOK)
     * @return
     * @author jitendra.pisal
     */
    private Properties getMailClientProp(MailClient mailClient) {
        switch (mailClient) {
            case GMAIL:
                return getGmailClientProperties();
            case OUTLOOK:
                return getOutlookProperties();
            default:
                return getGmailClientProperties();
        }
    }


    /**
     * @return Properties of gmail client
     * @author jitendra.pisal
     */
    private Properties getGmailClientProperties() {
        properties.setProperty("mail.smtp.host", "smtp.gmail.com");
        properties.setProperty("mail.smtp.socketFactory.port", "465");
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.auth", "false");
        properties.setProperty("mail.smtp.port", "465");
        properties.setProperty("mail.transport.protocol", "smtp");
        return properties;
    }


    /**
     * @return Properties of outlook client
     */
    private Properties getOutlookProperties() {
        properties.setProperty("mail.imaps.host", "imap-mail.outlook.com");
        properties.setProperty("mail.imaps.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.imaps.socketFactory.fallback", "false");
        properties.setProperty("mail.imaps.port", "993");
        properties.setProperty("mail.imaps.socketFactory.port", "993");
        properties.setProperty("mail.store.protocol", "imaps");
        return properties;
    }


    /**
     * EmailMethods - Inner class contains all the email utils methods.
     *
     * @author jitendra.pisal
     */
    public class EmailMethods {

        //************* EMAIL ACTIONS *******************

        public void openEmail(Message message) throws Exception {
            message.getContent();
        }


        public int getNumberOfMessages() throws MessagingException {
            return folder.getMessageCount();
        }


        public int getNumberOfUnreadMessages() throws MessagingException {
            return folder.getUnreadMessageCount();
        }


        /**
         * Gets a message by its position in the folder. The earliest message is indexed at 1.
         *
         * @author jitendra.pisal
         */
        public Message getMessageByIndex(int index) throws MessagingException {
            return folder.getMessage(index);
        }


        /**
         * @return Subject of message
         * @throws MessagingException
         */
        public String getSubjectOfLatestMessage() throws MessagingException {
            return getLatestMessage().getSubject();
        }


        public Message getLatestMessage() throws MessagingException {
            return getMessageByIndex(getNumberOfMessages());
        }


        /**
         * Gets all messages within the folder
         *
         * @author jitendra.pisal
         */
        public Message[] getAllMessages() throws MessagingException {
            return folder.getMessages();
        }


        /**
         * @param maxToGet maximum number of messages to get, starting from the latest. For example, enter 100 to get the last 100 messages received.
         * @author jitendra.pisal
         */
        public Message[] getMessages(int maxToGet) throws MessagingException {
            Map<String, Integer> indices = getStartAndEndIndices(maxToGet);
            return folder.getMessages(indices.get("startIndex"), indices.get("endIndex"));
        }


        /**
         * Searches for messages with a specific subject
         *
         * @param subject     Subject to search messages for
         * @param unreadOnly  Indicate whether to only return matched messages that are unread
         * @param maxToSearch maximum number of messages to search, starting from the latest. For example, enter 100 to search through the last 100 messages.
         */
        public Message[] getMessagesBySubject(String subject, boolean unreadOnly, int maxToSearch) throws Exception {
            Map<String, Integer> indices = getStartAndEndIndices(maxToSearch);

            Message messages[] = folder.search(
                    new SubjectTerm(subject),
                    folder.getMessages(indices.get("startIndex"), indices.get("endIndex")));

            if (unreadOnly) {
                List<Message> unreadMessages = new ArrayList<Message>();
                for (Message message : messages) {
                    if (isMessageUnread(message)) {
                        unreadMessages.add(message);
                    }
                }
                messages = unreadMessages.toArray(new Message[]{});
            }

            return messages;
        }


        /**
         * Returns HTML of the email's content
         *
         * @author jitendra.pisal
         */
        public String getMessageContent(Message message) throws Exception {
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(message.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        }


        /**
         * @param max
         * @return
         * @throws MessagingException
         * @author jitendra.pisal
         */
        private Map<String, Integer> getStartAndEndIndices(int max) throws MessagingException {
            int endIndex = getNumberOfMessages();
            int startIndex = endIndex - max;

            //In event that maxToGet is greater than number of messages that exist
            if (startIndex < 1) {
                startIndex = 1;
            }

            Map<String, Integer> indices = new HashMap<String, Integer>();
            indices.put("startIndex", startIndex);
            indices.put("endIndex", endIndex);

            return indices;
        }


        /**
         * Searches an email message for a specific string
         *
         * @author jitendra.pisal
         */
        public boolean isTextInMessage(Message message, String text) throws Exception {
            String content = getMessageContent(message);

            //Some Strings within the email have whitespace and some have break coding. Need to be the same.
            content = content.replace("&nbsp;", " ");
            content = content.replace("=", "");
            return content.contains(text);
        }


        /**
         * @param message check if message is unread or not
         * @return
         * @throws Exception
         * @author jitendra.pisal
         */
        public boolean isMessageUnread(Message message) throws Exception {
            return !message.isSet(Flags.Flag.SEEN);
        }
    }
}

